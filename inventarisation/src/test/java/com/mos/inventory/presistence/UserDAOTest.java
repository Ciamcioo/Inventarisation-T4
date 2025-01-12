package com.mos.inventory.presistence;

import com.mos.inventory.entity.User;
import com.mos.inventory.presistance.UserDAO;
import com.mos.inventory.presistance.UserDAOImp;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class UserDAOTest {

    private UserDAO userDAO;
    private EntityManager entityMock;
    private User userDummy;

    @BeforeEach
    public void setup() {
        entityMock = mock(EntityManager.class);
        userDAO = new UserDAOImp(entityMock);
        userDummy = new User();
        userDummy.getContactInformation().setEmail("dummy@dummy.com");
    }

    @Test
    public void doNothing() {

    }


}
