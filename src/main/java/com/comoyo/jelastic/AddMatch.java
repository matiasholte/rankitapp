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
        resp.getWriter().print(HTML_HEADER + FORM_CONTENT + PersistentStorage.getRankingList()
                + HTML_FOOTER);
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
        PersistentStorage.storeMatch(winner, loser);
        resp.setContentType("text/html");
        resp.getWriter().print(HTML_HEADER);
        resp.getWriter().println("Last 6 matches: <br><pre>");
        resp.getWriter().println(PersistentStorage.getMatches(6));
        resp.getWriter().println("</pre>");
        resp.getWriter().println("<br> <a href=\"ranking\">See ranking</a><br>");
        resp.getWriter().print(FORM_CONTENT + PersistentStorage.getRankingList() + HTML_FOOTER);

        Person winnerPerson = PersistentStorage.getPerson(winner);
        Person loserPerson = PersistentStorage.getPerson(loser);
        double rankingChange = getRankingChange(winnerPerson.ranking, loserPerson.ranking);
        winnerPerson.ranking += rankingChange;
        loserPerson.ranking -= rankingChange;
        PersistentStorage.setPerson(winnerPerson);
        PersistentStorage.setPerson(loserPerson);
    }

    /**
     * @param winnerRanking Previous ranking of the winner
     * @param loserRanking Previous ranking of the loser
     * @return How much the winner should gain in ranking (and the loser lose)
     */
    private double getRankingChange(final double winnerRanking, final double loserRanking) {
        final double difference = winnerRanking - loserRanking;
        final double expectedA = 1.0/(1+Math.pow(10.0, -difference/400));
        final double K = 32;
        return K * (1-expectedA);
    }
}