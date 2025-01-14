package com.mos.inventory.service.auth;

import com.mos.inventory.entity.ContactUserInformation;
import com.mos.inventory.entity.User;
import com.mos.inventory.presistance.ContactUserInformationDAO;
import com.mos.inventory.presistance.UserDAO;
import com.mos.inventory.service.mediator.ServiceMediator;
import com.mos.inventory.service.session.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginServiceImplTest {
    LoginService loginService;
    ServiceMediator mediator;

    UserDAO userDAOMock;
    ContactUserInformationDAO contactDAO;

    ContactUserInformation information;

    @BeforeEach
    public void setup() {
        mediator = mock(ServiceMediator.class);
        userDAOMock = mock(UserDAO.class);
        contactDAO = mock(ContactUserInformationDAO.class);
        information = new ContactUserInformation();
        information.setUserID(UUID.randomUUID());
        loginService = new LoginServiceImp(mediator, userDAOMock, contactDAO);
    }

    @Test
    public void successfulLoginForEmail() {
        when(contactDAO.findBy("dummy@dummy.com")).thenReturn(information);
        when(contactDAO.findBy("notDummy@dummy.com")).thenReturn(information);


        when(userDAOMock.findBy(any(UUID.class))).thenReturn(new User());
        when(userDAOMock.findBy(any(UUID.class))).thenReturn(new User());

        assertEquals(LoginResult.SUCCESSFUL, loginService.login("dummy@dummy.com"));
        assertEquals(LoginResult.SUCCESSFUL, loginService.login("notDummy@dummy.com"));
    }

    @Test
    public void unsuccessfulLoginForEmail() {
        assertEquals(LoginResult.UNSUCCESSFUL, loginService.login("dummy2@dummy.com"));
        assertEquals(LoginResult.UNSUCCESSFUL, loginService.login("dummy@notDummy.com"));
    }

    @Test
    public void contactUserInformationReturnsNullForSpecifiedEmail() {
        when(contactDAO.findBy("dummy@dummy.com")).thenReturn(null);
        assertEquals(LoginResult.UNSUCCESSFUL, loginService.login("dummy@dummy.com"));
    }




}
