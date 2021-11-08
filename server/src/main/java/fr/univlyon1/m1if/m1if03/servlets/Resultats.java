package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.Candidat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Resultats", value = {})
public class Resultats extends HttpServlet {
    Map<String, Candidat> candidats;
    List<Bulletin> bulletins;

    @SuppressWarnings("unchecked")
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Rappel : la liste des candidats n'est initialisée qu'à la première connexion d'un utilisateur, dans la servlet Init.
        // Donc pas possible de mettre sa récupération dans la méthode init() de cette servlet, car elle peut être appelée avant.
        candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");

        Map<String, Integer> votes = new HashMap<>();
        for (String nomCandidat : candidats.keySet()) {
            votes.put(nomCandidat, 0);
        }

        for (Bulletin bulletin : bulletins) {
            int score = votes.get(bulletin.getCandidat().getNom());
            votes.put(bulletin.getCandidat().getNom(), ++score);
        }
        req.setAttribute("votes", votes);
        req.getRequestDispatcher("/WEB-INF/components/resultats.jsp").forward(req, resp);
    }
}