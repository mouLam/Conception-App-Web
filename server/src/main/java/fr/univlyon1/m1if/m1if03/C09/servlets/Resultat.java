package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.C09.classes.Candidat;
import fr.univlyon1.m1if.m1if03.C09.utils.CandidatListGenerator;

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

@WebServlet(name = "Resultat", value = "/resultat")
@SuppressWarnings("unchecked")
public class Resultat extends HttpServlet {

    Map<String, Candidat> candidats = new HashMap<>();
    List<Bulletin> bulletins = new ArrayList<>();
    Map<String, Integer> votes = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("resultats.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");
            for (String nomCandidat :  candidats.keySet()) {
                votes.put(nomCandidat, 0);
            }
            bulletins = (List<Bulletin>) req.getServletContext().getAttribute("bulletins");
            for (Bulletin bulletin : bulletins) {
                int score = votes.get(bulletin.getCandidat().getNom());
                votes.put(bulletin.getCandidat().getNom(), ++score);
            }
            req.getServletContext().setAttribute("votes", votes);

            req.getRequestDispatcher("resultats.jsp").forward(req, resp);

        } catch (IOException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
