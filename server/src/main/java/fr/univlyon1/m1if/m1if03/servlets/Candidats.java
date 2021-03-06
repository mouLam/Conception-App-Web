package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Candidat;
import fr.univlyon1.m1if.m1if03.classes.User;
import org.apache.commons.lang3.ArrayUtils;

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

@WebServlet(name = "Candidats", urlPatterns = {"/candidats", "/candidats/*"})
public class Candidats extends HttpServlet {

    Map<String, Candidat> candidats;
    String [] pathUri;
    Map<Integer, Candidat> candidatsIds;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Rappel : la liste des candidats n'est initialisée qu'à la première connexion d'un utilisateur, dans la servlet Init.
        // Donc pas possible de mettre sa récupération dans la méthode init() de cette servlet, car elle peut être appelée avant.
        this.candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");
        User userSession = (User) req.getServletContext().getAttribute("user");

        this.candidatsIds = new HashMap<>();
        int i = 0;
        // identifie les candidats grâce à un entier.
        for (String name: this.candidats.keySet()) {
            this.candidatsIds.put(i, this.candidats.get(name));
            i++;
        }

        splitPathUri(req);
        if (this.pathUri.length == 2) { // /election/candidats
            List<String> uriCandidat = new ArrayList<>();
            for ( Integer id : this.candidatsIds.keySet()) {
                uriCandidat.add(req.getRequestURL() + "/" + id);
            }
            sendDataAsJSON(resp, uriCandidat);
        } else if (this.pathUri.length == 3) {
            if (this.pathUri[2].equals("noms")) { // /election/candidats/noms
                List<String> listNameCandidat = new ArrayList<>(this.candidats.keySet());
                sendDataAsJSON(resp, listNameCandidat);
            } else {  // /election/candidats/{candidatId}
                Integer idUrl = Integer.parseInt(this.pathUri[2]);
                if (userSession == null) {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
                }
                Candidat candidattrouve = this.candidatsIds.get(idUrl);
                if (candidattrouve == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Candidat non trouvé");
                }
                assert candidattrouve != null;
                List<Candidat> candidats = new ArrayList<>();
                candidats.add(candidattrouve);
                sendDataAsJSON(resp, candidats);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User userSession = (User) req.getServletContext().getAttribute("user");
        splitPathUri(req);

        if (userSession == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        } else if (!userSession.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur");
        }

        if (this.pathUri.length == 3 && this.pathUri[2].equals("update")) { // /election/candidats/update
            this.candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");
            resp.setHeader("Authorization", (String) req.getAttribute("token"));
            if (this.candidats.size() == 0) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Erreur lors du chargement de la liste");
            } else {
                this.candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");
                resp.setHeader("Authorization", (String) req.getAttribute("token"));
                if (this.candidats.size() > 0) {
                    resp.sendError(HttpServletResponse.SC_NO_CONTENT,
                            "liste mise à jour");
                }
            }
        }
    }

    /**
     * Convertir l'URI sous forme de tableau
     * @param request HttpServletRequest
     */
    private void splitPathUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        this.pathUri = uri.split("/");
        this.pathUri = ArrayUtils.removeElement(this.pathUri, "api"); // delete /v3_war
        this.pathUri = ArrayUtils.removeElement(this.pathUri, "v3_war"); // delete /v3_war
        this.pathUri = ArrayUtils.removeElement(this.pathUri, "v3"); // delete /v3_war
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
