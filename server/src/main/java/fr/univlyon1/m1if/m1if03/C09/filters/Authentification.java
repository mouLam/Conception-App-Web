package fr.univlyon1.m1if.m1if03.C09.filters;

import fr.univlyon1.m1if.m1if03.C09.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Authentification")
public class Authentification extends HttpFilter implements Filter {

    private ServletContext context;

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
        System.out.println("--- Initialisation du filtre Authentification");
    }

    public void destroy() {
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpSession session = req.getSession(true);
        String login = req.getParameter("login");
        res.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
        if (req.getRequestURI().endsWith("index.jsp") || req.getRequestURI().endsWith("/")) {
            chain.doFilter(req, res);
            return;
        } else {
            if (session.getAttribute("user") != null ) {
                chain.doFilter(req, res);
                return;
            } else if (login != null && !login.equals("")) {
                User user = new User(login,
                        req.getParameter("nom") != null ? req.getParameter("nom") : "",
                        req.getParameter("admin") != null && req.getParameter("admin").equals("on"));
                session.setAttribute("user", user);
                req.getRequestDispatcher("/election/vote").forward(req, res);
            } else {
                res.sendRedirect("./index.jsp");
            }
        }
    }


}
