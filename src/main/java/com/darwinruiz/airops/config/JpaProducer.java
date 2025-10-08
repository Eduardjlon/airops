package com.darwinruiz.airops.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.cfg.AvailableSettings;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class JpaProducer {

    @Produces
    @ApplicationScoped
    public EntityManagerFactory createEntityManagerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.driver", System.getProperty("DB_DRIVER"));
        props.put("jakarta.persistence.jdbc.url", System.getProperty("DB_URL"));
        props.put("jakarta.persistence.jdbc.user", System.getProperty("DB_USER"));
        props.put("jakarta.persistence.jdbc.password", System.getProperty("DB_PASSWORD"));

        props.put("hibernate.dialect", System.getProperty("HIBERNATE_DIALECT"));
        props.put("hibernate.hbm2ddl.auto", System.getProperty("HIBERNATE_DDL"));
        props.put("hibernate.show_sql", System.getProperty("HIBERNATE_SHOW_SQL"));
        props.put("hibernate.format_sql", System.getProperty("HIBERNATE_FORMAT_SQL"));
        props.put("hibernate.archive.autodetection", "class");

        // escanea el/los paquetes donde se encuetran los @Entity
        Set<Class<?>> entities = new Reflections("com.darwinruiz.airops.models")
                .getTypesAnnotatedWith(Entity.class);
        // si se tienen más paquetes, se debe repetir con otro Reflections y añadir al set

        props.put(AvailableSettings.LOADED_CLASSES, new ArrayList<>(entities));


        return Persistence.createEntityManagerFactory("airopsPU", props);
    }

    @Produces
    @RequestScoped
    public EntityManager createEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }
}
