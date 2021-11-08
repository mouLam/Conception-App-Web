package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.Ballot;
import fr.univlyon1.m1if.m1if03.classes.Bulletin;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@WebFilter(urlPatterns = {"/election/listBallots", "/election/vote", "/election/deleteVote"})
public class BallotListCacheFilter extends HttpFilter {
    Map<String, Ballot> ballots;
    Long lastModified = (long) -1;

    @Override
    @SuppressWarnings("unchecked")
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        ballots = (Map<String, Ballot>) config.getServletContext().getAttribute("ballots");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        switch(req.getRequestURI().split("/")[req.getRequestURI().split("/").length - 1]) {
            case "listBallots":
                if(req.getMethod().equals("GET")) {
                    if(req.getHeader("If-Modified-Since") != null && req.getDateHeader("If-Modified-Since") > lastModified) {
                        res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    } else {
                        res.setDateHeader("Last-Modified", new Date().getTime());
                        super.doFilter(req, res, chain);
                    }
                }
                break;
            case "vote":
            case "deleteVote":
                if(req.getMethod().equals("POST")) {
                    super.doFilter(req, res, chain);
                    lastModified = new Date().getTime();
                    break;
                }
            default:
                super.doFilter(req, res, chain);
        }
    }
}
