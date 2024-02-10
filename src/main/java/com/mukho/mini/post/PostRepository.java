package com.mukho.mini.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findBySubject(String subject);
    Post findBySubjectAndContent(String subject, String content);
    List<Post> findBySubjectLike(String subject);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @Query("select "
            + "distinct p "
            + "from Post p "
            + "left outer join User u1 on p.author=u1 "
            + "left outer join Comment c on c.post=p "
            + "left outer join User u2 on c.author=u2 "
            + "where "
            + "   p.subject like %:keyword% "
            + "   or p.content like %:keyword% "
            + "   or u1.username like %:keyword% "
            + "   or c.content like %:keyword% "
            + "   or u2.username like %:keyword% ")
    Page<Post> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
