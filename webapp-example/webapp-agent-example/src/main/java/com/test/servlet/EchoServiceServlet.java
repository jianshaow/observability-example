package com.test.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EchoServiceServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(EchoServiceServlet.class);

    @Serial
    private static final long serialVersionUID = -7766401686496991505L;

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (final InputStream in = req.getInputStream(); final PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/plain");
            final String msg = IOUtils.toString(in, StandardCharsets.UTF_8);
            logger.info("be requested to echo a message: {}", msg);
            String result = this.callAuralService(msg);
            result = this.callMindService(result);
            result = this.callSpeakService(result);
            out.print(result);
            logger.info("echo back the result: {}", result);
            in.close();
            out.close();
        }
    }

    private String callAuralService(String msg) throws IOException {
        return call(msg, "http://localhost:8080/aural");
    }

    private String callMindService(String msg) throws IOException {
        return call(msg, "http://localhost:8080/mind");
    }

    private String callSpeakService(String msg) throws IOException {
        return call(msg, "http://localhost:8080/speak");
    }

    private String call(String msg, final String uri) throws IOException {
        final HttpPost request = new HttpPost(uri);
        request.setEntity(new StringEntity(msg));
        try (ClassicHttpResponse response = httpClient.executeOpen(null, request, null)) {
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (ParseException e) {
            throw new IOException("Failed to parse response", e);
        }
    }
}
