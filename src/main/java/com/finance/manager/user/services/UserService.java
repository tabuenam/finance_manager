package com.finance.manager.user.services;

import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UserModel;

public interface UserService {
    UserEntity saveUser(UserModel userModel);

}
