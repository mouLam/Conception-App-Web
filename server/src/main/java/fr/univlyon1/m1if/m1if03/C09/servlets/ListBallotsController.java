package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.Ballot;
import fr.univlyon1.m1if.m1if03.C09.classes.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@WebServlet(name = "ListBallotsController", urlPatterns = "/election/listBallots")
public class ListBallotsController extends HttpServlet {
    Map<String, Ballot> ballots = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("user");
        if(user.isAdmin()){
            req.getRequestDispatcher("/listBallots.jsp").include(req,resp);
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED); //403
        }


    }

    /** Ajouter la méthode doPost pour la gestion du bouton supprimer (plus tard) */
}
