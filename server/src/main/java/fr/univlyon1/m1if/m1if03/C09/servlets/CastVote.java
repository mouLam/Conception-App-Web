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
@SuppressWarnings("unchecked")
public class CastVote extends HttpServlet {

    Map<String, Candidat> candidats = null;
    Map<String, Ballot> ballots = new HashMap<>();
    List<Bulletin> bulletins = new ArrayList<>();
    Bulletin bulletin;
    int blancs = 0;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            /* Gestion de la session utilisateur */
            HttpSession session = req.getSession();
            if (session.getAttribute("user") == null) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN); //403
            }

            /* Récuperer le candidat selectionné */
            String selectedCandidat = req.getParameter("selectCandidat");
            if(selectedCandidat != null && selectedCandidat.equals("----") ) {
                resp.sendRedirect("vote.jsp");
            }

            if (selectedCandidat != null && !selectedCandidat.equals("") && session.getAttribute("user") != null) {
                if(!selectedCandidat.equals("blanc")){
                    /* Récupération du candidat sélectionné par l'utilisateur dans la liste de candidats */
                    candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");
                    req.getServletContext().setAttribute("selectCandidat", selectedCandidat);
                    Candidat candidat = candidats.get(selectedCandidat);

                    /* Création d'un nouveau bulletin de vote avec un attribut candidat */
                    this.bulletin = new Bulletin(candidat);
                }else{
                    /* Incrémentation du compteur de votes blancs et mise à jour de l'attribut */
                    blancs++;
                    req.getServletContext().setAttribute("selectCandidat", selectedCandidat);
                    req.getServletContext().setAttribute("blancs", blancs);

                    /* Création d'un nouveau bulletin de vote blanc */
                    this.bulletin = new Bulletin(true);
                    System.out.println("Nombre de votes blancs :"+(int) req.getServletContext().getAttribute("blancs"));
                }

                /* Ajout du bulletin à la liste des bulletins */
                req.getServletContext().setAttribute("monBulletin", bulletin);
                bulletins = (List<Bulletin>) req.getServletContext().getAttribute("bulletins");
                bulletins.add(bulletin);
                req.getServletContext().setAttribute("bulletins", bulletins);

                /* Ajout d'un ballot dans la liste */
                Ballot ballot = new Ballot(bulletin);
                User user = (User) session.getAttribute("user");
                ballots = (Map<String, Ballot>) req.getServletContext().getAttribute("ballots");
                ballots.put(user.getLogin(), ballot);
                req.getServletContext().setAttribute("ballots", ballots);

                /* Redirection interne vers la page ballot.jsp */
                req.getRequestDispatcher("ballot.jsp").forward(req, resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN); //403
        }
    }
}
