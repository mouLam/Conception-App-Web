package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.utils.ElectionM1if03JwtHelper;

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
        String login = req.getParameter("login");
        if(session != null) {
            super.doFilter(req, res, chain);
        } else if (res.getHeader("Authorization") != null && login != null && !login.equals("")){
            String token = res.getHeader("Authorization");
            String verifyLogin = ElectionM1if03JwtHelper.verifyToken(token, req);

            if(verifyLogin.endsWith(session.getLogin())) {
                req.setAttribute("token", token);
                super.doFilter(req, res, chain);
            } else {
                res.sendRedirect("index.html");
                //res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez être connecté pour accéder à cette page.");
            }
        }
    }
}