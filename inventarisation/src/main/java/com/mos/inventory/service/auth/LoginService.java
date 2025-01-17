package com.mos.inventory.service.auth;

import com.mos.inventory.dto.LoginResult;

public interface LoginService {

    LoginResult login(String email);


}
