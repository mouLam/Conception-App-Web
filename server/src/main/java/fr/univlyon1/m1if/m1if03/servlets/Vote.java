package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.Candidat;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Vote", value = {})
public class Vote extends HttpServlet {
    Map<String, Candidat> candidats = null;
    Map<String, Ballot> ballots;
    List<Bulletin> bulletins;
    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        candidats = (Map<String, Candidat>) config.getServletContext().getAttribute("candidats");
        System.out.println("Candidats : " + candidats.size());

        bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        System.out.println("Bulletins : " + bulletins.size());

        ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
        System.out.println("Ballots : " + ballots.size());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String candidat = request.getParameter("candidat");
        if (candidat == null || candidat.equals("")) {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Cette application ne prend pas encore en charge le vote blanc.");
            return;
        }

        User user = (User) request.getSession(false).getAttribute("user");
        Bulletin bulletin = new Bulletin(candidats.get(candidat));
        bulletins.add(bulletin);
        Ballot ballot = new Ballot(bulletin);
        ballots.put(user.getLogin(), ballot);

        request.getRequestDispatcher("/WEB-INF/components/electionHome.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        Ballot ballot = ballots.get(user.getLogin());
        request.setAttribute("ballot", ballot);
        request.getRequestDispatcher("/WEB-INF/components/voteForm.jsp").forward(request, response);
    }
}