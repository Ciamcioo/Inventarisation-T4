package com.mos.inventory.fitnessefixturetest;

import com.mos.inventory.InventoryApp;
import fit.Fixture;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SetUp extends Fixture {
    private static ApplicationContext context;

    public SetUp() {
        if (context == null) {
            context = new AnnotationConfigApplicationContext(InventoryApp.class);
        }
    }
}
