package org.example.service;

import org.example.form.LoginForm;
import org.example.result.LoginResult;

public interface LoginService {
    public LoginResult login(LoginForm loginForm);
}
