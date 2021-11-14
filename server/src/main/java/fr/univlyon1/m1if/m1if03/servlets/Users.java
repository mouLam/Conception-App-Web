package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;
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

@WebServlet(name = "Users", urlPatterns = {"/user", "/users/*"})
public class Users extends HttpServlet {
    Map<String, User> users;
    Map<String, Ballot> ballots;
    List<Bulletin> bulletins;
    String [] pathUri;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
        this.bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        this.ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        splitPathUri(req);
        HttpSession session = req.getSession(false);
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }
        assert userSession != null;
        if (this.pathUri.length == 1) {
            // retrieve list user and send it as JSON
            if (!userSession.isAdmin()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur");
            }
            List<User> userList = new ArrayList<User>(this.users.values());
            sendDataAsJSON(resp, userList);
        } else if (this.pathUri.length == 2) {
            // retrieve a user from his login
            String loginInURI = this.pathUri[1];
            if (!userSession.isAdmin() && !userSession.getLogin().equals(loginInURI)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Utilisateur non administrateur ou pas celui qui est logué");
            }
            User userToRetrieve = this.users.get(loginInURI);
            if (userToRetrieve == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur non trouvé");
            }
            req.setAttribute("user", userToRetrieve);
            sendDataAsJSON(resp, userToRetrieve);

        } else if (this.pathUri.length == 3) {
            String loginInURI = this.pathUri[1];
            String lastValueURI = this.pathUri[2];
            if (lastValueURI.equals("ballot")) {
                if (!userSession.isAdmin()
                        && !this.ballots.containsKey(userSession.getLogin())) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Utilisateur non administrateur ou non propriétaire du ballot");
                }
                resp.sendRedirect("election/ballots/byUser/"+loginInURI);
            } else if (lastValueURI.equals("vote")) {
                Bulletin sonBulletin = this.ballots.get(userSession.getLogin()).getBulletin();
                boolean voteOwner = (sonBulletin != null && this.bulletins.contains(sonBulletin));
                //TODO : vérifier si l'égalite peut marcher ainsi

                if (!userSession.isAdmin() && !voteOwner) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Utilisateur non administrateur ou non propriétaire du vote");
                }
                resp.sendRedirect("election/votes/byUser/"+loginInURI);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "GET - More than 3");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        splitPathUri(req);
        HttpSession session = req.getSession();
        if (this.pathUri.length == 2) {
            if (this.pathUri[1].equals("login")) {
                // Jackson ObjectMapper class and how to serialize Java objects into JSON
                // and deserialize JSON string into Java objects.
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>(){};
                Map<String, String > body = mapper.readValue(req.getReader(), ref);
                User newUser = new User(body.get("login"), body.get("nom"), body.get("admin").equals("true") );
                // add in session
                session = req.getSession(true);
                session.setAttribute("user", newUser);
                // add in users map
                this.users.put(newUser.getLogin(), newUser);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else if (this.pathUri[1].equals("logout")) {
                User userSession = (User) session.getAttribute("user");
                if (userSession == null) {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
                }
                session.invalidate();
                resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Successful operation");
                this.getServletContext().getNamedDispatcher("Home").forward(req, resp);

            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requéte non acceptables");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "POST - More than 2");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        splitPathUri(req);
        HttpSession session = req.getSession(false);
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }
        assert userSession != null;
        if (this.pathUri.length == 3 && this.pathUri[2].equals("nom")) {
            User user = this.users.get(this.pathUri[1]);
            if (user == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur non trouvé");
            } else {
                if (!userSession.isAdmin() && userSession != user) {
                    //TODO : vérifier si l'égalite peut marcher ainsi. sinon faire égalité profonde
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Utilisateur non administrateur ou pas celui qui est logué");
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String > body = mapper.readValue(req.getReader(), new TypeReference<Map<String,String>>(){});
            assert user != null;
            user.setNom(body.get("nom"));
            sendDataAsJSON(resp, user);
            resp.sendError(HttpServletResponse.SC_NO_CONTENT, "User correctement modifié");
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requéte non acceptables");
        }
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI().replace(req.getContextPath() + "/users/", "");
        req.setAttribute("action", action); // Utilisé dans electionHome.jsp
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
