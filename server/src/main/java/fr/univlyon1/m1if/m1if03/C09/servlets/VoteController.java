package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.Ballot;
import fr.univlyon1.m1if.m1if03.C09.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.C09.classes.Candidat;
import fr.univlyon1.m1if.m1if03.C09.classes.User;

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

@WebServlet(name = "VoteController", urlPatterns = "/election/vote")

public class VoteController extends HttpServlet {
    Map<String, Candidat> candidats = null;
    Map<String, Ballot> ballots = new HashMap<>();
    List<Bulletin> bulletins = new ArrayList<>();
    Bulletin bulletin;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        req.setCharacterEncoding("UTF-8");
        String selection = req.getParameter("selectCandidat");
        HttpSession session = req.getSession();

        if(selection != null && !selection.isEmpty() && !selection.equals("----") ){
            // Récupération de la liste des candidats et création d'un objet Candidat avec la selection utilisateur
            this.candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");
            req.getServletContext().setAttribute("selectCandidat", selection);
            Candidat candidat = this.candidats.get(selection);

            // Création d'un nouveau bulletin et ajout de ce bulletin en attribut de requête
            this.bulletin = new Bulletin(candidat);
            req.getServletContext().setAttribute("monBulletin", this.bulletin);

            // Ajout du bulletin à la liste des bulletins
            this.bulletins = (List<Bulletin>) req.getServletContext().getAttribute("bulletins");
            this.bulletins.add(bulletin);
            req.getServletContext().setAttribute("bulletins", this.bulletins);

            // Création d'un ballot (avec le bulletin en paramètre) et ajout à la liste des ballots
            Ballot ballot = new Ballot(this.bulletin);
            User user = (User) session.getAttribute("user");
            this.ballots = (Map<String, Ballot>) req.getServletContext().getAttribute("ballots");
            this.ballots.put(user.getLogin(), ballot);

            /* Mise à jour de la liste des ballots (attribut de requête) et redirection interne vers la page ballot.jsp */
            req.getServletContext().setAttribute("ballots", this.ballots);
            req.getRequestDispatcher("/ballot.jsp").include(req,resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        req.getRequestDispatcher("/vote.jsp").include(req,resp);
    }
}
