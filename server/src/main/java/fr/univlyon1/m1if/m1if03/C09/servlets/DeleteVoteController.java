package fr.univlyon1.m1if.m1if03.C09.servlets;

import fr.univlyon1.m1if.m1if03.C09.classes.Ballot;
import fr.univlyon1.m1if.m1if03.C09.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.C09.classes.User;

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

@WebServlet(name = "DeleteVoteController", value = "/election/deleteVote")
@SuppressWarnings("unchecked")
public class DeleteVoteController extends HttpServlet {

    Map<String, Ballot> ballots = new HashMap<>();
    List<Bulletin> bulletins = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        try {
            HttpSession session = req.getSession();

            if (req.getParameter("actiondelete") != null) {
                System.out.println("delete button has been pressed");
                // Récupérer le ballot de vote correspondant à l'utilisateur
                ballots = (Map<String, Ballot>) req.getServletContext().getAttribute("ballots");
                User user = (User) session.getAttribute("user");
                Ballot ballot = ballots.get(user.getLogin());
                Bulletin bulletin = ballot.getBulletin();
                bulletins = (List<Bulletin>) req.getServletContext().getAttribute("bulletins");
                for (Bulletin b: bulletins) {
                    if(b.getCandidat().getNom().equals(bulletin.getCandidat().getNom())
                            && b.getCandidat().getPrenom().equals(bulletin.getCandidat().getPrenom())){
                        bulletins.remove(b);
                        break;
                    }
                }

                ballot.setBulletin(null);
                ballots.remove(user.getLogin());
                req.getServletContext().setAttribute("bulletins", bulletins);
                req.getServletContext().setAttribute("ballots", ballots);
                req.getServletContext().removeAttribute("selectCandidat");
                req.getRequestDispatcher("/WEB-INF/components/vote.jsp").include(req, resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
