package com.finance.manager.user.services;

import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UpdatePasswordRequest;
import com.finance.manager.user.model.UserAccountDetailModel;
import com.finance.manager.user.model.UserModel;

public interface UserService {
    UserEntity saveUser(UserModel userModel);
    UserAccountDetailModel getUserAccountDetail(String mail);
    UserAccountDetailModel updatePassword(UpdatePasswordRequest updatePasswordRequest);
    void deleteUserAccount(String email);
}
