package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Ballot;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/users/*")
public class Users extends HttpServlet {
    Map<String, Ballot> ballots;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        //resp.getWriter().write();
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI().replace(req.getContextPath() + "/users/", "");
        req.setAttribute("action", action); // Utilisé dans electionHome.jsp

        switch(action) {
            case "":
                if (req.getMethod().equals("GET")) {

                }
                //this.getServletContext().getAttribute("")
                break;
            case "user":
                this.getServletContext().getNamedDispatcher("UpdateAccount").forward(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
