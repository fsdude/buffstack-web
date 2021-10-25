package org.example.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory factory;

    static {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory(
                new ServiceRegistryBuilder().applySettings(
                        cfg.getProperties()).buildServiceRegistry());
    }

    public static Session getSession() {
        return factory.openSession();
    }
}
