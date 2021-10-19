package fr.univlyon1.m1if.m1if03.C09.filters;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@WebFilter(filterName = "CacheBallots")
public class CacheBallots extends HttpFilter implements Filter {

    private ServletContext context;
    private Date date;

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
        System.out.println("--- Initialisation du filtre CacheBallots");
    }

    public void destroy() {
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String uri = req.getRequestURI();
        System.out.println("uri : "+uri);
        if (uri.endsWith("/vote") || uri.endsWith("/listBallots")) {
            if (req.getMethod().equals("POST")) {
                this.date = new Date();
            } else if (req.getMethod().equals("GET")) {
                if (req.getHeader("If-Modified-Since") != null && this.date != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "EEE, dd MMM yyy HH:mm:ss z",
                            new Locale("us"));
                    try {
                        Date ifModifiedSince = dateFormat.parse(req.getHeader("If-Modified-Since"));
                        if (ifModifiedSince.after(this.date)) {
                            res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                res.setDateHeader("Last-Modified", new Date().getTime());
            }
            chain.doFilter(req, res);
        }


        /*if (req.getMethod().equals("POST") && req.getRequestURI().endsWith("/election/vote")) {
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
        }*/

    }
}
