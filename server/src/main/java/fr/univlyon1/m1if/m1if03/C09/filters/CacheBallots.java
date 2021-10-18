package fr.univlyon1.m1if.m1if03.C09.filters;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "CacheBallots", urlPatterns = {"/election/vote", "/election/listBallots"})
public class CacheBallots extends HttpFilter implements Filter {
    private long lastModifiedSince;
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        if (req.getMethod().equals("POST") && req.getRequestURI().endsWith("/election/vote")) {
           this.lastModifiedSince = new Date().getTime();
           chain.doFilter(req, res);
        }
        if (req.getMethod().equals("GET") && req.getRequestURI().endsWith("/election/listBallots")) {
            System.out.println("get : "+req.getRequestURI());
            res.setDateHeader("Last-Modified", new Date().getTime() );
            if (req.getHeader("If-Modified-Since") != null) {
                long lastModifBrowser = req.getDateHeader("If-Modified-Since");
                if ( (this.lastModifiedSince <= lastModifBrowser) && lastModifBrowser != 1) {
                    res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
            chain.doFilter(req, res);
        }

        if (req.getMethod().equals("GET") && req.getRequestURI().endsWith("/election/vote")) {
            chain.doFilter(req, res);
        }

    }
}
