package com.mos.inventory.presistance;

import com.mos.inventory.entity.ContactUserInformation;

public interface ContactUserInformationDAO {

    ContactUserInformation findBy(String email);
}
