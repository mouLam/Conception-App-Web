package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "UserController", value = "/election/user")
public class UserController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        req.getRequestDispatcher("/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        try {
            HttpSession session = req.getSession();
            //Récupération du nouveau nom et MàJ
            req.setCharacterEncoding("UTF-8");
            String nouveaunom = req.getParameter("newname");
            User user = (User) session.getAttribute("user");
            if (nouveaunom != null && !nouveaunom.equals("")) {
                user.setNom(nouveaunom);
                session.setAttribute("user", user);
                req.getRequestDispatcher("/update.jsp").forward(req, resp);
            } else {
                resp.sendError(
                        HttpServletResponse.SC_NOT_MODIFIED,
                        "Erreur sur la modification");
            }
        } catch (IOException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
