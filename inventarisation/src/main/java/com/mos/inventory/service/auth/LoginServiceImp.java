package com.mos.inventory.service.auth;

import com.mos.inventory.entity.ContactUserInformation;
import com.mos.inventory.entity.User;
import com.mos.inventory.presistance.ContactUserInformationDAO;
import com.mos.inventory.presistance.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImp implements  LoginService {

    private UserDAO userDAO;
    private ContactUserInformationDAO contactUserInformationDAO;


    @Autowired
    public LoginServiceImp(UserDAO userDAO, ContactUserInformationDAO contactUserInformationDAO) {
        this.userDAO = userDAO;
        this.contactUserInformationDAO = contactUserInformationDAO;
    }

    @Override
    public LoginResult login(String email) {
        User user = null;
        ContactUserInformation contactUserInformation = contactUserInformationDAO.findBy(email);

        if (contactUserInformation != null) {
            user = userDAO.findBy(contactUserInformation.getUserID());
        }

        return  user != null  ? LoginResult.SUCCESSFUL : LoginResult.UNSUCCESSFUL;
    }
}