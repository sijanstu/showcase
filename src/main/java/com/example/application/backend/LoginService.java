package com.example.application.backend;


import com.example.application.entity.UserModel;

import java.util.concurrent.ExecutionException;

/**
 * @author Sijan Bhandari
 */
public class LoginService {

    static public boolean isUserAvailable(UserModel user) throws InterruptedException, ExecutionException {
        return UserService.checkUserAvailability(user.getUsername(), user.getPassword());
    }

    static public boolean isUserNameAvailable(UserModel user) throws InterruptedException, ExecutionException {
        return UserService.checkUserNameAvailability(user.getUsername());
    }
}
