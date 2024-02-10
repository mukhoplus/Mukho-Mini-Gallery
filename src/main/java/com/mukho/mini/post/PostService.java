package com.mukho.mini.post;

import com.mukho.mini.user.User;
import org.springframework.data.domain.Page;

public interface PostService {
    Post getPost(Integer id);
    void create(String subject, String content, User user);
    Page<Post> getPostList(int page, String keyword);
    void modify(Post post, String subject, String content);
    void delete(Post post);
    void vote(Post post, User user);
}
