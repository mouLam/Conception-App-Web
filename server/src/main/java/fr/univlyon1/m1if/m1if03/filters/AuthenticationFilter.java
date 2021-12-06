package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = "/*")
public class AuthenticationFilter extends HttpFilter {

    private final String[] authorizedURIs = {"/users/login", "/index.html", "/static", "/election/resultats"}; // Manque "/", traité plus bas...

    Map<String, User> users;

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String currentUri = req.getRequestURI().replace(req.getContextPath(), "");

        // Traitement de l'URL racine
        if(currentUri.equals("/")) {
            res.sendRedirect("index.html");
            return;
        }

        // Traitement des autres URLs autorisées sans authentification
        for(String authorizedUri: authorizedURIs) {
            if(currentUri.startsWith(authorizedUri)) {
                super.doFilter(req, res, chain);
                return;
            }
        }

        //HttpSession session = req.getSession(false); // On récupère la session sans la créer
        User session = (User) req.getServletContext().getAttribute("user");
        if(session != null) {
            super.doFilter(req, res, chain);
        } else {
            String login = req.getParameter("login");
            if(req.getMethod().equals("POST") && login != null && !login.equals("")) {
                //session = req.getSession(true);
                User user = new User(login,
                        req.getParameter("nom") != null ? req.getParameter("nom") : "",
                        req.getParameter("admin") != null && req.getParameter("admin").equals("on"));
                //session.setAttribute("user", user);
                req.getServletContext().setAttribute("user", user);
                this.users.put(user.getLogin(), user);
                super.doFilter(req, res, chain);
            } else
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être connecté pour accéder à cette page.");
        }
    }
}