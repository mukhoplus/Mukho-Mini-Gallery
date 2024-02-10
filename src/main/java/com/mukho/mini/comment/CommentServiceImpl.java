package com.mukho.mini.comment;

import com.mukho.mini.exception.DataNotFoundException;
import com.mukho.mini.post.Post;
import com.mukho.mini.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment create(Post post, String content, User author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);

        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("Comment not found");
        }
    }

    @Override
    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

    @Override
    public void vote(Comment comment, User user) {
        comment.getVoter().add(user);
        this.commentRepository.save(comment);
    }
}
