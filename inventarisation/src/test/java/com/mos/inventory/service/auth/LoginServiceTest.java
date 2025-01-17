package com.mos.inventory.service.auth;

import com.mos.inventory.dto.LoginMessage;
import com.mos.inventory.dto.LoginResult;
import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.ContactUserInformation;
import com.mos.inventory.entity.Role;
import com.mos.inventory.entity.User;
import com.mos.inventory.presistance.ContactUserInformationDAO;
import com.mos.inventory.presistance.UserDAO;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginServiceTest {
    // Tested
    LoginService loginService;

    // Mocked
    UserContextHolder userContextHolder;
    UserDAO userDAOMock;
    ContactUserInformationDAO contactDAO;


    ContactUserInformation information;

    LoginResult successfulLogin;
    LoginResult unsuccessfulLogin;

    User validUser = new User();

    @BeforeEach
    public void setup() {
        information = new ContactUserInformation();
        information.setUserID(UUID.randomUUID());

        validUser.setRole(new Role());

        userContextHolder = mock(UserContextHolder.class);
        userDAOMock = mock(UserDAO.class);
        contactDAO = mock(ContactUserInformationDAO.class);
        loginService = new LoginServiceImp(userDAOMock, contactDAO, userContextHolder);

        successfulLogin = new LoginResult(LoginMessage.SUCCESSFUL, "");
        unsuccessfulLogin = new LoginResult(LoginMessage.UNSUCCESSFUL, "");
    }

    @Test
    public void successfulLoginForEmail() {
        when(contactDAO.findBy("dummy@dummy.com")).thenReturn(information);
        when(contactDAO.findBy("notDummy@dummy.com")).thenReturn(information);


        when(userDAOMock.findBy(any(UUID.class))).thenReturn(validUser);
        when(userDAOMock.findBy(any(UUID.class))).thenReturn(validUser);

        successfulLogin.setUserEmail("dummy@dummy.com");
        assertEquals(successfulLogin, loginService.login("dummy@dummy.com"));

        successfulLogin.setUserEmail("notDummy@dummy.com");
        assertEquals(successfulLogin, loginService.login("notDummy@dummy.com"));
    }

    @Test
    public void unsuccessfulLoginForEmail() {
        unsuccessfulLogin.setUserEmail("dummy2@dummy.com");
        assertEquals(unsuccessfulLogin, loginService.login("dummy2@dummy.com"));

        unsuccessfulLogin.setUserEmail("dummy@notDummy.com");
        assertEquals(unsuccessfulLogin, loginService.login("dummy@notDummy.com"));
    }

    @Test
    public void contactUserInformationReturnsNullForSpecifiedEmail() {
        when(contactDAO.findBy("dummy@dummy.com")).thenReturn(null);

        unsuccessfulLogin.setUserEmail("dummy@dummy.com");
        assertEquals(unsuccessfulLogin, loginService.login("dummy@dummy.com"));
    }

    @Test
    public void validUserContextObjectPass_WillPlaceTheUserContextInUserContextHolder() {
        when(contactDAO.findBy("dummy@dummy.com")).thenReturn(information);
        when(userDAOMock.findBy(any(UUID.class))).thenReturn(validUser);
        when(userContextHolder.get()).thenReturn(new UserContext(validUser, "dummy@dummy.com"));

        loginService.login("dummy@dummy.com");

        UserContext validUserContext = new UserContext(validUser, "dummy@dummy.com");
        assertEquals(validUserContext, userContextHolder.get());
    }
}







