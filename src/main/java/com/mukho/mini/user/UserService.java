package com.mukho.mini.user;

public interface UserService {
    User create(String username, String email, String password);
    User getUser(String username);
}
