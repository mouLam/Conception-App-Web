package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.*;
import fr.univlyon1.m1if.m1if03.C09.utils.CandidatListGenerator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Init", value = "/init")
public class Init extends HttpServlet {
    Map<String, Candidat> candidats = null;
    final Map<String, Ballot> ballots = new HashMap<>();
    final List<Bulletin> bulletins = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Cette instruction doit toujours être au début de la méthode init() pour pouvoir accéder à l'objet config.
        super.init(config);
        ServletContext context = config.getServletContext();
        context.setAttribute("ballots", ballots);
        context.setAttribute("bulletins", bulletins);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // On intercepte le premier appel à Init pour mettre en place la liste des candidats,
        // car en cas d'erreur de chargement, il faut pouvoir renvoyer une erreur HTTP.
        // Fait dans un bloc try/catch pour le cas où la liste des candidats ne s'est pas construite correctement.
        try {
            if (candidats == null) {
                candidats = CandidatListGenerator.getCandidatList();
                request.getServletContext().setAttribute("candidats", candidats);
            }

            // Gestion de la session utilisateur
            String login = request.getParameter("login");
            if (login != null && !login.equals("")) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", new User(login, request.getParameter("nom") != null ? request.getParameter("nom") : ""));
                request.getRequestDispatcher("vote.jsp").forward(request, response);
            } else {
                response.sendRedirect("index.html");
            }
        } catch (IOException e) {
            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur dans la récupération de la liste des candidats.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("index.html");
    }
}
