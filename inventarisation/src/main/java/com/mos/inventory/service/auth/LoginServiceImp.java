package com.mos.inventory.service.auth;

import com.mos.inventory.dto.LoginMessage;
import com.mos.inventory.dto.LoginResult;
import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.ContactUserInformation;
import com.mos.inventory.entity.User;
import com.mos.inventory.presistance.ContactUserInformationDAO;
import com.mos.inventory.presistance.UserDAO;
import com.mos.inventory.service.UserContextHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImp implements  LoginService {
    private final UserDAO userDAO;
    private final ContactUserInformationDAO contactUserInformationDAO;
    private final UserContextHolder userContextHolder;

    private UserContext userContext;


    @Autowired
    public LoginServiceImp(UserDAO userDAO, ContactUserInformationDAO contactUserInformationDAO, UserContextHolder userContextHolder) {
        this.userDAO = userDAO;
        this.contactUserInformationDAO = contactUserInformationDAO;
        this.userContextHolder = userContextHolder;
    }

    @Override
    public LoginResult login(String email) {
        User user = null;
        ContactUserInformation contactUserInformation = contactUserInformationDAO.findBy(email);

        if (contactUserInformation != null) {
            user = userDAO.findBy(contactUserInformation.getUserID());
        }

        LoginResult loginResult = createLoginResultMessage(user, email);

        if (user != null) {
            userContext = new UserContext(user, email);
            userContextHolder.set(userContext);
        }

        return loginResult;


    }

    private LoginResult createLoginResultMessage(User user, String email) {
        if (user == null) {
            return new LoginResult(LoginMessage.UNSUCCESSFUL, email);
        }

        return new LoginResult(LoginMessage.SUCCESSFUL,
                               user.getFirstName(),
                               user.getLastName(),
                               email,
                               user.getRole().getName());
    }



}
