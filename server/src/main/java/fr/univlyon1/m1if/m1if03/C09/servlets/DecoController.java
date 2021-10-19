package fr.univlyon1.m1if.m1if03.C09.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DecoController", value = "/DecoController")
public class DecoController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out=response.getWriter();

        HttpSession session=request.getSession();
        session.invalidate();

        if(session == null || !request.isRequestedSessionIdValid() ){
            request.getRequestDispatcher("./index.jsp").include(request, response);
            request.getSession().invalidate();
            out.print("Vous êtes déconnecté.");
        }
        out.close();
    }

}
