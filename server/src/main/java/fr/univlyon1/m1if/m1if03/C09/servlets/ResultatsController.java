package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.C09.classes.Candidat;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ResultatsController", value = "/election/resultats")
public class ResultatsController extends HttpServlet {
    Map<String, Candidat> candidats = new HashMap<>();
    List<Bulletin> bulletins = new ArrayList<>();
    Map<String, Integer> votes = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        try {
            candidats = (Map<String, Candidat>) request.getServletContext().getAttribute("candidats");
            for (String nomCandidat :  candidats.keySet()) {
                votes.put(nomCandidat, 0);
            }
            bulletins = (List<Bulletin>) request.getServletContext().getAttribute("bulletins");
            for (Bulletin bulletin : bulletins) {
                int score = votes.get(bulletin.getCandidat().getNom());
                votes.put(bulletin.getCandidat().getNom(), ++score);
            }
            request.getServletContext().setAttribute("votes", votes);
            request.getRequestDispatcher("/resultats.jsp").include(request, response);
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
