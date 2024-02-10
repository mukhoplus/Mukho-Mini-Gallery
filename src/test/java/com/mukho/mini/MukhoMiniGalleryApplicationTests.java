package com.mukho.mini;

import com.mukho.mini.comment.Comment;
import com.mukho.mini.post.Post;
import com.mukho.mini.comment.CommentRepository;
import com.mukho.mini.post.PostRepository;
import com.mukho.mini.post.PostService;
import com.mukho.mini.user.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MukhoMiniGalleryApplicationTests {

    // DI - 스프링이 객체를 대신 생성하여 주입하는 기법
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Test
    void testJpa() {
        Post p1 = new Post();
        p1.setSubject("무코 정신 안차리지");
        p1.setContent("안차려?");
        p1.setCreateDate(LocalDateTime.now());
        this.postRepository.save(p1);

        Post p2 = new Post();
        p2.setSubject("Mukho Pro Max 24");
        p2.setContent("지금은 Mukho Plus 24라네요~");
        p2.setCreateDate(LocalDateTime.now());
        this.postRepository.save(p2);
    }

    @Test
    void testFindAll() {
        List<Post> all = this.postRepository.findAll();
        assertEquals(2, all.size());

        Post p = all.get(0);
        assertEquals("무코 정신 안차리지", p.getSubject());
    }

    @Test
    void testFindById() {
        Optional<Post> op = this.postRepository.findById(1);
        if (op.isPresent()) {
            Post p = op.get();
            assertEquals("무코 정신 안차리지", p.getSubject());
        }
    }

    @Test
    void testFindBySubject() {
        Post p = this.postRepository.findBySubject("무코 정신 안차리지");
        assertEquals(1, p.getId());
    }

    @Test
    void testFindBySubjectAndContent() {
        Post p = this.postRepository.findBySubjectAndContent(
                "무코 정신 안차리지", "안차려?");
        assertEquals(1, p.getId());
    }

    @Test
    void testFindBySubjectList() {
        List<Post> pList = this.postRepository.findBySubjectLike("무코%");
        Post p = pList.get(0);
        assertEquals("무코 정신 안차리지", p.getSubject());
    }

    @Test
    void testUpdate() {
        Optional<Post> op = this.postRepository.findById(1);
        assertTrue(op.isPresent());
        Post p = op.get();
        p.setSubject("애니모리야");
        this.postRepository.save(p);
    }

    @Test
    void testDelete() {
        assertEquals(2, this.postRepository.count());
        Optional<Post> op = this.postRepository.findById(1);
        assertTrue(op.isPresent());
        Post p = op.get();
        this.postRepository.delete(p);
        assertEquals(1, this.postRepository.count());
    }

    @Test
    void testJpa2() {
        Optional<Post> op = this.postRepository.findById(2);
        assertTrue(op.isPresent());
        Post p = op.get();

        Comment c = new Comment();
        c.setContent("업그레이드 해주세요");
        c.setPost(p);
        c.setCreateDate(LocalDateTime.now());
        this.commentRepository.save(c);
    }

    @Test
    void testFindById2() {
        Optional<Comment> oc = this.commentRepository.findById(1);
        assertTrue(oc.isPresent());
        Comment c = oc.get();
        assertEquals(2, c.getPost().getId());
    }

    @Transactional
    @Test
    void testFindCommentInPost() {
        Optional<Post> op = this.postRepository.findById(2);
        assertTrue(op.isPresent());
        Post p = op.get();

        List<Comment> commentList = p.getCommentList();

        assertEquals(1, commentList.size());
        assertEquals("업그레이드 해주세요", commentList.get(0).getContent());
    }

    @Test
    void setTestData() {
        for (int i = 1; i <= 111; ++i) {
            String subject = String.format("테스트 데이터 [%03d]", i);
            String content = "쌈무";

            User user = new User();
            user.setUsername("묵호");
            this.postService.create(subject, content, null);
        }
    }
}
