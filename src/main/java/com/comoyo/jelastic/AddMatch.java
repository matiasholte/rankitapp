package com.comoyo.jelastic;

import com.google.common.base.Strings;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AddMatch extends HttpServlet {
    public static final String HTML_HEADER =
            "<html>\n"
            + "<body>\n";
    public static final String FORM_CONTENT =
            "<form action=\"add\" method=\"post\">\n"
            + "Winner: <input type=\"text\" name=\"winner\"><br />\n"
            + "Loser: <input type=\"text\" name=\"loser\"><br />\n"
            + "<input type=\"submit\" name=\"submit\" value=\"submit\">\n";
    public static final String HTML_FOOTER =
            "</body>\n"
            + "</html>";

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().print(HTML_HEADER + FORM_CONTENT + HTML_FOOTER);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {
        final String winner = req.getParameter("winner");
        final String loser = req.getParameter("loser");
        if (Strings.isNullOrEmpty(winner) || Strings.isNullOrEmpty(loser)) {
            doGet(req, resp);
            return;
        }

    }
}