package com.darwinruiz.airops.health;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet("/_health/env")
public class EnvServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String[] keys = {
                "DB_DRIVER", "DB_URL", "DB_USER", "DB_PASSWORD",
                "HIBERNATE_DIALECT", "HIBERNATE_DDL", "HIBERNATE_SHOW_SQL", "HIBERNATE_FORMAT_SQL"
        };
        for (String k : keys) out.println(k + "=" + System.getProperty(k));
    }
}
