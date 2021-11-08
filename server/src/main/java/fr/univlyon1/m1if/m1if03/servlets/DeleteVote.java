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

@WebServlet(name = "DeleteVote", value = {})
public class DeleteVote extends HttpServlet {
    Map<String, Candidat> candidats = null;
    Map<String, Ballot> ballots;
    List<Bulletin> bulletins;
    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        candidats = (Map<String, Candidat>) config.getServletContext().getAttribute("candidats");
        bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String login = request.getParameter("user") != null ? request.getParameter("user") : ((User) request.getSession().getAttribute("user")).getLogin();
        Ballot ballot = ballots.get(login);
        Bulletin bulletin = ballot.getBulletin();
        bulletins.remove(bulletin);
        ballot.setBulletin(null);
        ballots.remove(login);

        request.getRequestDispatcher("/WEB-INF/components/electionHome.jsp").forward(request, response);
    }
}