package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.Candidat;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.utils.ElectionM1if03JwtHelper;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name="Ballots", urlPatterns = {"/ballots", "/ballots/*"})
public class Ballots extends HttpServlet {
    Map<String, Ballot> ballots;
    Map<String, Candidat> candidats;
    List<Bulletin> bulletins;
    String [] pathUri;

    //Identifier les ballots par des Identifiants. Id - nomDuVotant
    Map<Integer, String> votesIds = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.getRequestDispatcher("/WEB-INF/components/listBallots.jsp").forward(req, resp);

        this.ballots = (Map<String, Ballot>) req.getServletContext().getAttribute("ballots");
        this.votesIds = (Map<Integer, String>) req.getServletContext().getAttribute("votesIds");
        splitPathUri(req);
        User user = (User) req.getServletContext().getAttribute("user");
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }

        String url = req.getScheme() + "://" +
                req.getServerName() + ":" + req.getServerPort()
                + req.getContextPath() + "/election/ballots/";

        // /election/ballots
        if (this.pathUri.length == 2 && this.pathUri[1].equals("ballots")) {
            assert user != null;
            String token = (String) req.getAttribute("token");
            if (!user.isAdmin()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur");
            } else {
                List<String> urisVote = new ArrayList<>();
                if (this.ballots.isEmpty()) {
                    String ballot = "Il n'a pas encore de vote";
                    sendDataAsJSON(resp, ballot);
                } else {
                    for (Integer id : this.votesIds.keySet()) {
                        urisVote.add(url + id);
                    }
                    resp.setHeader("Authorization", (String) req.getAttribute("token"));
                    sendDataAsJSON(resp, urisVote);
                }
            }
        }

        // /election/ballots/{ballotId}
        if (this.pathUri.length == 3 && this.pathUri[1].equals("ballots")) {
            Integer ballotId = Integer.parseInt(this.pathUri[2]);
            if (!this.votesIds.containsKey(ballotId) ) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ballot non trouvé");
            }
            assert user != null;
            if (!user.isAdmin()
                    && !user.getLogin().equals(this.votesIds.get(ballotId))) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN
                        , "Utilisateur non administrateur ou non propriétaire du ballot");
            } else {
                String uriVote = url + ballotId;
                resp.setHeader("Authorization", (String) req.getAttribute("token"));
                sendDataAsJSON(resp, uriVote);
            }

        }

        // /election/ballots/byUser/{userId}
        if (this.pathUri.length == 4
                && this.pathUri[1].equals("ballots")
                && this.pathUri[2].equals("byUser")) {
            String userId = this.pathUri[3];
            if (!this.ballots.containsKey(userId)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateteur ou ballot non trouvé");
            }
            assert user != null;
            if ( !(user.isAdmin() || this.ballots.containsKey(user.getLogin())) ) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN
                        , "Utilisateur non administrateur ou non propriétaire du ballot");
            }
            for (String u : this.ballots.keySet()) {
                for (Integer i : this.votesIds.keySet()) {
                    if (this.votesIds.get(i).equals(u)) {
                        resp.setHeader("Authorization", (String) req.getAttribute("token"));
                        sendDataAsJSON(resp, url + i);
                        break;
                    }
                }
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Rappel : la liste des candidats n'est initialisée qu'à la première connexion d'un utilisateur, dans la servlet Init.
        // Donc pas possible de mettre sa récupération dans la méthode init() de cette servlet, car elle peut être appelée avant.
        this.candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");
        this.ballots = (Map<String, Ballot>) req.getServletContext().getAttribute("ballots");
        this.bulletins = (List<Bulletin>) req.getServletContext().getAttribute("bulletins");
        splitPathUri(req);
        User userSession = (User) req.getServletContext().getAttribute("user");
        if (userSession == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }

        // /election/ballots
        assert userSession != null;
        if(this.pathUri.length == 2  && this.pathUri[1].equals("ballots")) {

            // vérifier s'il a déjà voté
            if (this.ballots.containsKey(userSession.getLogin())) {
                resp.sendError(HttpServletResponse.SC_NOT_MODIFIED, "Vous avez déjà voté");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>(){};
                Map<String, String > body = mapper.readValue(req.getReader(), ref);
                Candidat candidat = this.candidats.get(body.get("nomCandidat"));
                if (candidat == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Candidat sélectionné non trouvé");
                }
                Bulletin bulletin = new Bulletin(candidat);
                this.bulletins.add(bulletin);
                req.getServletContext().setAttribute("bulletins", this.bulletins);
                Ballot ballot = new Ballot(bulletin);
                assert candidat != null;
                this.ballots.put(userSession.getLogin(), ballot);
                req.getServletContext().setAttribute("ballots", this.ballots);

                int length = this.ballots.size();
                this.votesIds.put(length - 1, userSession.getLogin());
                req.getServletContext().setAttribute("votesIds", this.votesIds);
                resp.setHeader("Authorization", (String) req.getAttribute("token"));
                resp.sendError(HttpServletResponse.SC_CREATED, "Ballot créé");
            }

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requéte non acceptables");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.votesIds = (Map<Integer, String>) req.getServletContext().getAttribute("votesIds");
        this.ballots = (Map<String, Ballot>) req.getServletContext().getAttribute("ballots");
        this.bulletins = (List<Bulletin>) req.getServletContext().getAttribute("bulletins");

        splitPathUri(req);

        User user = (User) req.getServletContext().getAttribute("user");
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }

        if (this.pathUri.length == 3 && this.pathUri[1].equals("ballots")) {
            Integer ballotId = Integer.parseInt(this.pathUri[2]);
            if (!this.votesIds.containsKey(ballotId)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ballot non trouvé");
            } else {
                assert user != null;
                if (!user.isAdmin()
                        && !user.getLogin().equals(this.votesIds.get(ballotId))) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN
                            , "Utilisateur non administrateur ou non propriétaire du ballot");
                } else {
                    Ballot ballot = this.ballots.get(this.votesIds.get(ballotId));
                    Bulletin bulletin = ballot.getBulletin();
                    this.bulletins.remove(bulletin);
                    ballot.setBulletin(null);
                    this.votesIds.remove(ballotId);
                    this.ballots.remove(user.getLogin());
                    req.getServletContext().setAttribute("bulletins", this.bulletins);
                    req.getServletContext().setAttribute("ballots", this.ballots);
                    req.getServletContext().setAttribute("voteIds", this.votesIds);
                    resp.setHeader("Authorization", (String) req.getAttribute("token"));
                    resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Ballot supprimé");
                }
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptable");
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