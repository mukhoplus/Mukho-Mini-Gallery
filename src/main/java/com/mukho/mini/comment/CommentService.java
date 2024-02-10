package com.mukho.mini.comment;

import com.mukho.mini.post.Post;
import com.mukho.mini.user.User;

public interface CommentService {
    Comment create(Post post, String content, User author);
    Comment getComment(Integer id);
    void delete(Comment comment);
    void vote(Comment comment, User user);
}
