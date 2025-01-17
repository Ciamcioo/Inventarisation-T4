package com.mos.inventory.presistance;

import com.mos.inventory.entity.Location;

import java.util.List;

public interface LocationDAO {

    List<Location> getLocationTable();
}
