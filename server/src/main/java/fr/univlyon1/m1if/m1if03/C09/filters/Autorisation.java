package fr.univlyon1.m1if.m1if03.C09.filters;

import fr.univlyon1.m1if.m1if03.C09.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Autorisation")
public class Autorisation extends HttpFilter implements Filter {

    private ServletContext context;

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
        System.out.println("--- Initialisation du filtre Autorisation");
    }

    public void destroy() {
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user.isAdmin()) {
            chain.doFilter(req, res);
            return;
        } else {
            req.getRequestDispatcher("/election/ballot").forward(req, res);
        }
    }
}
