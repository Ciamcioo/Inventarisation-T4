package com.mos.inventory.fitnessefixturetest;

import com.mos.inventory.dto.LoginMessage;
import fit.ColumnFixture;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@SpringBootTest
public class LoginApiTest extends ColumnFixture {
    private static final Logger logger = Logger.getLogger(LoginApiTest.class.getName());
    private final RestTemplate restTemplate = new RestTemplate();


    private String email;

    public boolean loginTest() {
        String url = "http://localhost:8080/api/login?email=" + email;
        String jsonLoginResult = restTemplate.getForObject(url, String.class);
        logger.info(jsonLoginResult);
        if (jsonLoginResult== null) {
            throw new RuntimeException("Login result is null");
        }

        if (jsonLoginResult.contains(LoginMessage.UNSUCCESSFUL.toString()))
            return false;

        return jsonLoginResult.contains(LoginMessage.SUCCESSFUL.toString());
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

}