package com.darwinruiz.airops.health;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;

import java.io.*;

@WebServlet("/_health/db")
public class DbHealthServlet extends HttpServlet {
    @Inject
    EntityManager em;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            if (em == null) {
                out.println("EM = null (inyección CDI falló)");
                return;
            }
            Object one = em.createNativeQuery("SELECT 1").getSingleResult();
            out.println("JPA OK. SELECT 1 -> " + String.valueOf(one));
        } catch (Exception ex) {
            ex.printStackTrace(resp.getWriter());
        }
    }
}
