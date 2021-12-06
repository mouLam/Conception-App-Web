package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateAccount", value = {})
public class UpdateAccount extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/components/user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("nom") != null && !(req.getParameter("nom").equals(""))) {
            User user = (User) req.getSession().getAttribute("user");
            user.setNom(req.getParameter("nom"));
            req.getRequestDispatcher("/WEB-INF/components/electionHome.jsp").forward(req, resp);
        } else
            resp.sendRedirect("index.html");
    }
}