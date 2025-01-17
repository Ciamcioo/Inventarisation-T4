package com.mos.inventory.service.auth;

import com.mos.inventory.dto.LoginMessage;
import com.mos.inventory.dto.LoginResult;
import com.mos.inventory.entity.Role;
import com.mos.inventory.entity.User;
import com.mos.inventory.presistance.ContactUserInformationDAO;
import com.mos.inventory.presistance.UserDAO;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class LoginServiceImplTest {
    LoginService loginService;
    UserDAO userDAO = null;
    UserContextHolder userContextHolder;

    ContactUserInformationDAO contactUserInformationDAO = null;

    LoginResult validResult;
    LoginResult invalidResult;

    User validUser = new User();
    User invalidUser;
    String email;
    Role role = new Role();


    @BeforeEach
    void setup() {
        userContextHolder = mock(UserContextHolder.class);
        loginService = new LoginServiceImp(userDAO, contactUserInformationDAO, userContextHolder);

        validUser.setFirstName("John");
        validUser.setLastName("Doe");
        email = "johndoe@example.com";
        role.setName("exampleRole");
        validUser.setRole(role);


        validResult = new LoginResult(LoginMessage.SUCCESSFUL, email);
        validResult.setUserFirstName(validUser.getFirstName());
        validResult.setUserLastName(validUser.getLastName());
        validResult.setUserEmail(email);
        validResult.setUserRoleName(validUser.getRole().getName());

        invalidResult = new LoginResult(LoginMessage.UNSUCCESSFUL, email);

    }
    @Test
    void validateLoginResultMessageForValidUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method createLoginResultMessage = LoginServiceImp.class.getDeclaredMethod("createLoginResultMessage", User.class, String.class);
        createLoginResultMessage.setAccessible(true);

        LoginResult  loginResult = (LoginResult) createLoginResultMessage.invoke(loginService, validUser, email);

        assertEquals(validResult, loginResult);
    }

    @Test
    void invalidUserPassed_WillReturnInvalidLoginResult() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method createLoginResultMessage = LoginServiceImp.class.getDeclaredMethod("createLoginResultMessage", User.class, String.class);
        createLoginResultMessage.setAccessible(true);

        LoginResult  loginResult = (LoginResult) createLoginResultMessage.invoke(loginService, invalidUser, email);

        assertEquals(invalidResult, loginResult);
    }
}
