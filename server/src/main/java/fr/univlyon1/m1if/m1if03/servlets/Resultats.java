package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.Candidat;
import fr.univlyon1.m1if.m1if03.classes.ResultatCandidat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        this.bulletins = (List<Bulletin>) req.getServletContext().getAttribute("bulletins");

        for (Bulletin bulletin : bulletins) {
            int score = votes.get(bulletin.getCandidat().getNom());
            votes.put(bulletin.getCandidat().getNom(), ++score);
        }

        Map<String, List<ResultatCandidat>> results = new HashMap<>();
        String v = "Elections";
        results.put(v, new ArrayList<ResultatCandidat>());
        for (String vote : votes.keySet()) {
            ResultatCandidat resultatCandidat =
                    new ResultatCandidat(vote, votes.get(vote));
            results.get(v).add(resultatCandidat);
        }
        sendDataAsJSON(resp, results);

        req.setAttribute("votes", votes);

    }

    /**
     * Transforme une donnée en JSON avant de l'afficher dans la reponse.
     * @param response HttpServletResponse
     * @param data donnée
     * @throws IOException exception
     */
    private void sendDataAsJSON(HttpServletResponse response, Object data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String valueToString = objectMapper.writeValueAsString(data);
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(valueToString);
        printWriter.flush();
    }
}