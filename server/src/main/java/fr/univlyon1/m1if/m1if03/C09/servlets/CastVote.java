package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.Ballot;
import fr.univlyon1.m1if.m1if03.C09.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.C09.classes.Candidat;
import fr.univlyon1.m1if.m1if03.C09.classes.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "CastVote", value = "/castVote")
public class CastVote extends HttpServlet {

    Map<String, Candidat> candidats = null;
    Map<String, Ballot> ballots = new HashMap<>();
    List<Bulletin> bulletins = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);

        try {
            //Gestion de session
            HttpSession session = req.getSession();
            if (session.getAttribute("user") == null) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN); //403
            }

            // Recuperer le candidat selectionné
            String selectedCandidat = req.getParameter("selectCandidat");
            if(selectedCandidat != null && selectedCandidat.equals("----") ) {
                resp.sendRedirect("vote.jsp");
            }

            if (selectedCandidat != null && !selectedCandidat.equals("") && session.getAttribute("user") != null) {
                candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");
                req.getServletContext().setAttribute("selectCandidat", selectedCandidat);
                Candidat candidat = candidats.get(selectedCandidat);
                Bulletin bulletin = new Bulletin(candidat);
                req.getServletContext().setAttribute("monBulletin", bulletin);
                bulletins = (List<Bulletin>) req.getServletContext().getAttribute("bulletins");
                bulletins.add(bulletin);
                req.getServletContext().setAttribute("bulletins", bulletins);
                Ballot ballot = new Ballot(bulletin);
                User user = (User) session.getAttribute("user");
                ballots = (Map<String, Ballot>) req.getServletContext().getAttribute("ballots");
                ballots.put(user.getLogin(), ballot);
                req.getServletContext().setAttribute("ballots", ballots);

                req.getRequestDispatcher("ballot.jsp").forward(req, resp);
            }

        } catch (IOException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //resp.sendRedirect("index.html");
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            //resp.sendRedirect("index.html");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN); //403
        }
    }
}
