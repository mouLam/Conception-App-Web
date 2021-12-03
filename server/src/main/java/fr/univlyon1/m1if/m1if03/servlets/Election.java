package fr.univlyon1.m1if.m1if03.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/election/*")
public class Election extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI().replace(req.getContextPath() + "/election/", "");
        req.setAttribute("action", action); // Utilisé dans electionHome.jsp

        if (action.contains("candidats")) {
            this.getServletContext().getNamedDispatcher("Candidats").forward(req, resp);
        }else if(action.contains("ballots")) {
            this.getServletContext().getNamedDispatcher("Ballots").forward(req, resp);
        }

        switch(action) {
            case "resultats":
                this.getServletContext().getNamedDispatcher("Resultats").forward(req, resp);
                break;
            case "vote":
                this.getServletContext().getNamedDispatcher("Vote").forward(req, resp);
                break;
            case "deleteVote":
                this.getServletContext().getNamedDispatcher("DeleteVote").forward(req, resp);
                break;
            case "listBallots":
                this.getServletContext().getNamedDispatcher("Ballots").forward(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
