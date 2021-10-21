package org.example.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

public class BancoUpdate {

    public static void main(String[] args) {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        SchemaUpdate su = new SchemaUpdate(cfg);
        su.execute(true, true);
    }
}
