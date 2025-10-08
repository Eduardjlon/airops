package com.darwinruiz.airops.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@WebListener
public class EnvListener implements ServletContextListener {

    private static final String CLASSPATH_ENV = "config/.env";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        boolean loaded = loadFromClasspath(CLASSPATH_ENV);
        if (loaded) {
            System.out.println("[EnvListener] .env cargado desde classpath: " + CLASSPATH_ENV);
        } else {
            System.out.println("[EnvListener] No se encontró " + CLASSPATH_ENV);
        }
    }

    private boolean loadFromClasspath(String resourcePath) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream in = cl.getResourceAsStream(resourcePath)) {
            if (in == null) {
                return false;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                parseAndSet(br);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void parseAndSet(BufferedReader br) throws Exception {
        String line;
        while ((line = br.readLine()) != null) {
            String s = line.trim();
            if (s.isEmpty() || s.startsWith("#")) continue;
            int eq = s.indexOf('=');
            if (eq <= 0) continue;
            String key = s.substring(0, eq).trim();
            String val = s.substring(eq + 1).trim();
            if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
                val = val.substring(1, val.length() - 1);
            }
            System.setProperty(key, val);
            System.out.println("[EnvListener] " + key + " = " + val);
        }
    }
}
