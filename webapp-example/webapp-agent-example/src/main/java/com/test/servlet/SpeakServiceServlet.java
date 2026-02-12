package com.test.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serial;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import java.net.InetSocketAddress;

public class SpeakServiceServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -7766401686496991505L;

    private CqlSession session;

    @Override
    public void init() throws ServletException {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("host.docker.internal", 9042))
                .withLocalDatacenter("datacenter1")
                .withKeyspace("mydb")
                .build();
    }

    @Override
    public void destroy() {
        if (session != null) {
            session.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        accessDB();
        try (final InputStream in = req.getInputStream(); final PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/plain");
            final String msg = IOUtils.toString(in, StandardCharsets.UTF_8);
            out.print(msg);
            in.close();
            out.close();
        }
    }

    private void accessDB() {
        SimpleStatement statement = SimpleStatement.builder("SELECT name FROM application WHERE id = ?")
                .addPositionalValue(1)
                .build();
        session.execute(statement);
    }
}
