package com.mukho.mini.post;

import com.mukho.mini.comment.Comment;
import com.mukho.mini.user.User;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;

import com.mukho.mini.exception.DataNotFoundException;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private Specification<Post> search(String keyword) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Post> p, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);

                Join<Post, User> u1 = p.join("author", JoinType.LEFT);
                Join<Post, Comment> c = p.join("commentList", JoinType.LEFT);
                Join<Comment, User> u2 = c.join("author", JoinType.LEFT);

                return cb.or(
                    cb.like(p.get("subject"), "%" + keyword + "%"), // 제목
                    cb.like(p.get("content"), "%" + keyword + "%"), // 내용
                    cb.like(u1.get("username"), "%" + keyword + "%"), // 게시글 작성자
                    cb.like(c.get("content"), "%" + keyword + "%"), // 댓글 내용
                    cb.like(u2.get("username"), "%" + keyword + "%") // 댓글 작성자
                );
            }
        };
    }

    @Override
    public Post getPost(Integer id) {
        Optional<Post> post = this.postRepository.findById(id);

        if (post.isPresent()) {
            return post.get();
        } else {
            throw new DataNotFoundException("Post not found");
        }
    }

    @Override
    public void create(String subject, String content, User user) {
        Post post = new Post();
        post.setSubject(subject);
        post.setContent(content);
        post.setCreateDate(LocalDateTime.now());
        post.setAuthor(user);
        this.postRepository.save(post);
    }

    @Override
    public Page<Post> getPostList(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Post> spec = search(keyword);
        return this.postRepository.findAll(spec, pageable);
        // return this.postRepository.findAllByKeyword(keyword, pageable);
    }

    @Override
    public void modify(Post post, String subject, String content) {
        post.setSubject(subject);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        this.postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        this.postRepository.delete(post);
    }

    @Override
    public void vote(Post post, User user) {
        post.getVoter().add(user);
        this.postRepository.save(post);
    }
}
