package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.Candidat;
import fr.univlyon1.m1if.m1if03.classes.User;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@WebServlet(name="Ballots", urlPatterns = {"/ballots", "/ballots/*"})
public class Ballots extends HttpServlet {
    Map<String, Ballot> ballots;
    Map<String, Candidat> candidats;
    String [] pathUri;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.getRequestDispatcher("/WEB-INF/components/listBallots.jsp").forward(req, resp);
        splitPathUri(req);

        if(this.pathUri.length == 1){
            List<Ballot> ballotsList = new ArrayList<Ballot>(this.ballots.values());
            sendDataAsJSON(resp, ballotsList);
        }
        //else if(Objects.equals(this.pathUri[2], "byUser")) {}

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Rappel : la liste des candidats n'est initialisée qu'à la première connexion d'un utilisateur, dans la servlet Init.
        // Donc pas possible de mettre sa récupération dans la méthode init() de cette servlet, car elle peut être appelée avant.
        this.candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");

        splitPathUri(req);
        HttpSession session = req.getSession();
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }
        assert userSession != null;

        if(Objects.equals(this.pathUri[1], "ballots")) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>(){};
            Map<String, String > body = mapper.readValue(req.getReader(), ref);
            Candidat candidat = this.candidats.get(body.get("nomCandidat"));
            Bulletin bulletin = new Bulletin(candidat);
            Ballot ballot = new Ballot(bulletin);
            this.ballots.put(candidat.getNom(), ballot);
            req.setAttribute("ballots", this.ballots);
            resp.sendError(HttpServletResponse.SC_CREATED);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requéte non acceptables");
        }
    }

    /**
     * Convertir l'URI sous forme de tableau
     * @param request HttpServletRequest
     */
    private void splitPathUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        this.pathUri = uri.split("/");
        this.pathUri = ArrayUtils.removeElement(this.pathUri, "v3_war"); // delete /v3_war
        this.pathUri = ArrayUtils.removeElement(this.pathUri, "");
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