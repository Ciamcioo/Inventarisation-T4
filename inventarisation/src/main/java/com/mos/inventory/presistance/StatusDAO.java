package com.mos.inventory.presistance;

import com.mos.inventory.entity.Status;

import java.util.List;
import java.util.Optional;

public interface StatusDAO {

    List<Status> getStatusTable();
}
