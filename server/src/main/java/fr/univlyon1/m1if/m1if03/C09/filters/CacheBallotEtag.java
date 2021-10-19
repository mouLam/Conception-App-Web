package fr.univlyon1.m1if.m1if03.C09.filters;

import fr.univlyon1.m1if.m1if03.C09.classes.Ballot;
import fr.univlyon1.m1if.m1if03.C09.classes.Bulletin;
import fr.univlyon1.m1if.m1if03.C09.classes.Candidat;
import fr.univlyon1.m1if.m1if03.C09.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebFilter(filterName = "CacheBallotEtag")
public class CacheBallotEtag extends HttpFilter implements Filter {

    Map<String, Ballot> ballots = new HashMap<>();
    List<Bulletin> bulletins = new ArrayList<>();

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        System.out.println("--- Initialisation du filtre CacheBallotEtag");
    }

    public void destroy() {
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        ServletContext context = req.getServletContext();
        User user = (User) session.getAttribute("user");
        bulletins = (List<Bulletin>) context.getAttribute("bulletins");
        ballots = (Map<String, Ballot>) context.getAttribute("ballots");

        if (req.getMethod().equals("GET")) {
            String eTag = req.getHeader("If-None-Match");
            String monTag = this.getTag(user);
            if (monTag.equals(eTag)) {
                res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            } else {
                res.addHeader("ETag", monTag);
            }
        }
        chain.doFilter(req, res);
    }

    private String getTag(User user) {
        String votes =  Integer.toString(this.bulletins.size());
        Ballot b = this.ballots.get(user.getLogin());
        String nomCandidat = "";
        if (b != null) {
            nomCandidat = b.getBulletin().getCandidat().getNom();
        }
        return user.getNom()+" : "+nomCandidat+" nbVote: "+votes;
    }
}
