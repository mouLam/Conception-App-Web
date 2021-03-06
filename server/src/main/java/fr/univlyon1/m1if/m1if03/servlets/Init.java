package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.classes.Candidat;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.utils.CandidatListGenerator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(loadOnStartup = 1, value = {})
public class Init extends HttpServlet {
    Map<String, Candidat> candidats = null;
    final Map<String, Ballot> ballots = new HashMap<>();
    final List<Bulletin> bulletins = new ArrayList<>();
    final Map<String, User> users = new HashMap<>();

    //Identifier les candidats par des Identifiants
    Map<Integer, Candidat> candidatsIds = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Cette instruction doit toujours être au début de la méthode init() pour pouvoir accéder à l'objet config.
        super.init(config);

        System.out.println("Initialisation des ressources du contexte applicatif.");

        ServletContext context = config.getServletContext();
        context.setAttribute("ballots", ballots);
        context.setAttribute("bulletins", bulletins);
        context.setAttribute("users", users);


        // Fait dans un bloc try/catch pour le cas où la liste des candidats ne s'est pas construite correctement.
        try {
            if (candidats == null || candidatsIds == null) {
                candidats = CandidatListGenerator.getCandidatList();
                candidatsIds = CandidatListGenerator.getCandidatIDList();
                context.setAttribute("candidats", candidats);
                System.out.println(candidats.size() + " candidats.");
                context.setAttribute("candidatsIds", candidatsIds);
                System.out.println(candidatsIds.size() + " candidatsIds.");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}