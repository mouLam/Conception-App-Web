package fr.univlyon1.m1if.m1if03.C09.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Deco", value = "/deco")
public class Deco extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();

        HttpSession session=request.getSession();
        session.invalidate();

        if(session == null || !request.isRequestedSessionIdValid() ){
            request.getRequestDispatcher("index.jsp").include(request, response);
            out.print("Vous êtes déconnecté.");
        }

        out.close();
    }
}
