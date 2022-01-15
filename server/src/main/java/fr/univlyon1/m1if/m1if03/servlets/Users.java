package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.utils.ElectionM1if03JwtHelper;
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
    User session;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
        this.bulletins = (List<Bulletin>) config.getServletContext().getAttribute("bulletins");
        this.ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
        this.session = (User) config.getServletContext().getAttribute("user");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getScheme() + "://" +
                req.getServerName() + ":" +
                req.getServerPort() + req.getContextPath() + "/users/";

        splitPathUri(req);
        //HttpSession session = req.getSession(false);
        //User userSession = (User) session.getAttribute("user");
        User userSession = (User) req.getServletContext().getAttribute("user");
        if (userSession == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
        }
        assert userSession != null;
        if (this.pathUri.length == 1 && this.pathUri[0].equals("users")) { // /users
            // retrieve list user and send it as JSON
            if (!userSession.isAdmin()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur");
            }
            List<User> userList = new ArrayList<User>(this.users.values());
            sendDataAsJSON(resp, userList);
        } else if (this.pathUri.length == 2 && this.pathUri[0].equals("users")) { // /users/{userId}
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
            resp.setHeader("Authorization", (String) req.getAttribute("token"));
            sendDataAsJSON(resp, userToRetrieve);

        } else if (this.pathUri.length == 3 && this.pathUri[0].equals("users")) {
            String loginInURI = this.pathUri[1];
            String lastValueURI = this.pathUri[2];
            if (lastValueURI.equals("ballot")) {  // /users/{userId}/ballot
                /*if (req.getHeader("Authorization") == null || !req.getHeader("Authorization").contains("Bearer ")) {
                    resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                            "Authorization header must be correct or not null");
                } else
                */
                if ( ! (userSession.isAdmin() ||
                        this.ballots.containsKey(userSession.getLogin())) ) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Utilisateur non administrateur ou non propriétaire du ballot");
                } else {
                    resp.setHeader("Authorization", (String) req.getAttribute("token"));
                    resp.sendError(HttpServletResponse.SC_SEE_OTHER, "Redirection vers l'URL du ballot de cet utilisateur");
                    resp.setHeader("Location", "election/ballots/byUser/" + loginInURI);
                    sendDataAsJSON(resp, url + loginInURI + "/ballot");

                }
            } else if (lastValueURI.equals("vote")) {  // /users/{userId}/vote
                if ( ! (userSession.isAdmin() ||
                        this.ballots.containsKey(userSession.getLogin())) ) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Utilisateur non administrateur ou non propriétaire du vote");
                } else {
                    resp.setHeader("Authorization", (String) req.getAttribute("token"));
                    resp.sendError(HttpServletResponse.SC_SEE_OTHER
                            , "Redirection vers l'URL du vote de cet utilisateur");
                    resp.setHeader("Location", "election/votes/byUser/" + loginInURI);
                    sendDataAsJSON(resp, url + loginInURI + "/vote");
                }

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        splitPathUri(req);
        //HttpSession session = req.getSession();
        //User session = (User) req.getServletContext().getAttribute("user");
        if (this.pathUri.length == 2) {
            if (this.pathUri[1].equals("login")) {
                // Jackson ObjectMapper class and how to serialize Java objects into JSON
                // and deserialize JSON string into Java objects.
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>(){};
                Map<String, String > body = mapper.readValue(req.getReader(), ref);
                User newUser = new User(body.get("login"), body.get("nom"), body.get("admin").equals("true") );

                String uriUser = req.getRequestURI() + "/" + newUser.getLogin();

                String token = ElectionM1if03JwtHelper.generateToken(uriUser, newUser.isAdmin(), req);
                resp.setHeader("Authorization", "Bearer "+token);
                req.getServletContext().setAttribute("user", newUser);
                this.users.put(newUser.getLogin(), newUser);
                req.getServletContext().setAttribute("users", this.users);
                //sendDataAsJSON(resp, token);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else if (this.pathUri[1].equals("logout")) {
                User userSession = (User) req.getServletContext().getAttribute("user");
                if (userSession == null) {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
                } else {
                    req.getSession().invalidate();
                    req.getServletContext().setAttribute("user", null);
                    //this.users.remove(userSession.getLogin());
                    resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Successful operation");
                }

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
        //HttpSession session = req.getSession(false);
        User userSession = (User) req.getServletContext().getAttribute("user");
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
            resp.setHeader("Authorization", (String) req.getAttribute("token"));
            sendDataAsJSON(resp, user);
            resp.sendError(HttpServletResponse.SC_NO_CONTENT, "User correctement modifié");
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
