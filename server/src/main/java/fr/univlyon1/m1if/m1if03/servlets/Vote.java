package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.Candidat;
import fr.univlyon1.m1if.m1if03.classes.User;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Vote", value = {"/votes/*"})
public class Vote extends HttpServlet {
    Map<String, Candidat> candidats = null;
    Map<String, Ballot> ballots;
    Map<Integer, String> votesIds;
    List<Bulletin> bulletins;
    String [] pathUri;


    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
        votesIds = (Map<Integer, String>) config.getServletContext().getAttribute("votesIds");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User userSession = (User) session.getAttribute("user");
        splitPathUri(request);

        if (userSession == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }
        if (this.pathUri.length == 3
                && this.pathUri[1].equals("votes")) { // /election/votes/{voteId}

            Integer idVote = Integer.parseInt(this.pathUri[2]);
            if (!this.votesIds.containsKey(idVote)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Vote non trouvé");
            }

            assert userSession != null;
            if (!userSession.isAdmin()
                    && !userSession.getLogin().equals(this.votesIds.get(idVote))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Utilisateur non administrateur ou non propriétaire du vote");
            }
            List<String> res = new ArrayList<>();
            sendDataAsJSON(response, res);
        } else
            if (this.pathUri.length == 4
                    && this.pathUri[1].equals("votes")
                    && this.pathUri[2].equals("byUser") ) { // /election/votes/byUser{userId}
                String loginUri = this.pathUri[3];
                if (this.ballots.get(loginUri) == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur ou vote non trouvé");
                }
                assert userSession != null;
                if ( !(userSession.isAdmin() ||
                        this.ballots.containsKey(userSession.getLogin())) ) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Utilisateur non administrateur ou non propriétaire du ballot");
                }

                for (Integer idVote : this.votesIds.keySet()) {
                    if (this.votesIds.get(idVote).equals(loginUri)) {
                        List<String> uriVotant = new ArrayList<>();
                        String url = request.getScheme() + "://" +
                                request.getServerName() + ":" +
                                request.getServerPort() + "/election/votes/" +
                                idVote;
                        uriVotant.add(url);
                        sendDataAsJSON(response, uriVotant);
                        break;
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requéte non acceptables");
            }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Rappel : la liste des candidats n'est initialisée qu'à la première connexion d'un utilisateur, dans la servlet Init.
        // Donc pas possible de mettre sa récupération dans la méthode init() de cette servlet, car elle peut être appelée avant.
        this.candidats = (Map<String, Candidat>) req.getServletContext().getAttribute("candidats");

        HttpSession session = req.getSession(false);
        User userSession = (User) session.getAttribute("user");
        splitPathUri(req);

        if (userSession == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }
        assert userSession != null;
        if (this.pathUri.length == 4
                && this.pathUri[1].equals("votes")
                && this.pathUri[2].equals("byUser")) {
           String login = this.pathUri[3];
           if ( !(userSession.isAdmin() || login.equals(userSession.getLogin()))) {
               resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur ou pas celui qui est logué");
           }
           if (this.ballots.get(login) == null){
               resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur ou vote non trouvé");
           } else {
               ObjectMapper mapper = new ObjectMapper();
               TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>(){};
               Map<String, String > body = mapper.readValue(req.getReader(), ref);
               String nomNewCandidat = body.get("nomCandidat");
               if (this.candidats.get(nomNewCandidat) == null) {
                   resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Candidat choisi non trouvé");
               }
               Candidat c = this.candidats.get(nomNewCandidat);
               Bulletin ancien = this.ballots.get(login).getBulletin();
               this.bulletins.remove(ancien);
               Bulletin b = new Bulletin(c);
               this.bulletins.add(b);
               req.getServletContext().setAttribute("bulletins", this.bulletins);
               this.ballots.get(login).setBulletin(b);
               req.getServletContext().setAttribute("ballots", this.ballots);
               //sendDataAsJSON(resp, this.ballots.get(login).getBulletin().getCandidat().getNom());
               resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Vote correctement modifié");
           }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametres de requête non acceptable");
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