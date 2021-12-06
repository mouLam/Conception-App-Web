package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name="Ballot", urlPatterns = {})
public class ViewBallot extends HttpServlet {
    Map<String, Ballot> ballots;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        Ballot ballot = ballots.get(user.getLogin());
        req.setAttribute("ballot", ballot);
        req.getRequestDispatcher("/WEB-INF/components/ballot.jsp").forward(req, resp);
    }
}