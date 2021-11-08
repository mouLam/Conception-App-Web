package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = "/*")
public class AuthenticationFilter extends HttpFilter {
    private final String[] authorizedURIs = {"/index.html", "/static", "/election/resultats"}; // Manque "/", traité plus bas...
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

        HttpSession session = req.getSession(false); // On récupère la session sans la créer
        if(session != null && session.getAttribute("user") != null) {
            super.doFilter(req, res, chain);
        } else {
            String login = req.getParameter("login");
            if(req.getMethod().equals("POST") && login != null && !login.equals("")) {
                session = req.getSession(true);
                session.setAttribute("user", new User(login,
                        req.getParameter("nom") != null ? req.getParameter("nom") : "",
                        req.getParameter("admin") != null && req.getParameter("admin").equals("on")));
                super.doFilter(req, res, chain);
            } else
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être connecté pour accéder à cette page.");
        }
    }
}