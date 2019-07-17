package com.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class SessionFactorySingleton {
    private static volatile SessionFactory sessionFactoryInstance;

    public static SessionFactory getSessionFactoryInstance() {
        SessionFactory localInstance = sessionFactoryInstance;
        if (localInstance == null) {
            synchronized (SessionFactorySingleton.class) {
                localInstance = sessionFactoryInstance;
                if (localInstance == null) {
                    sessionFactoryInstance = localInstance = new Configuration().configure().buildSessionFactory();
                }
            }
        }
        return localInstance;
    }

    public static void shutDown() {
        getSessionFactoryInstance().close();
    }
}
