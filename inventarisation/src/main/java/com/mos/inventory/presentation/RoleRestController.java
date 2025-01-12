package com.mos.inventory.presentation;

import com.mos.inventory.entity.Role;
import com.mos.inventory.presistance.RoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleRestController {
    private RoleDAO roleDAO;

    @Autowired
    public RoleRestController(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @GetMapping("/roles")
    public List<Role> getMembers() {
        return roleDAO.findAll();
    }

}
