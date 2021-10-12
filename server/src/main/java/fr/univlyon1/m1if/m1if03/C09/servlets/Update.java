package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Update", value = "/update")
@SuppressWarnings("unchecked")
public class Update extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Interdire l'accés quand on est pas connecté
            HttpSession session = req.getSession();
            if (session.getAttribute("user") == null) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN); //403
            }
            //Récupération du nouveau nom et MàJ
            String nouveaunom = req.getParameter("newname");
            User user = (User) session.getAttribute("user");
            if (nouveaunom != null && !nouveaunom.equals("")) {
                user.setNom(nouveaunom);
                session.setAttribute("user", user);
                req.getRequestDispatcher("update.jsp").forward(req, resp);
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN); //403
        }
    }
}
