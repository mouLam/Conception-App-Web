package fr.univlyon1.m1if.m1if03.C09.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Controller", urlPatterns = "/election/*")

public class Controller extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI();
        if(action.endsWith("vote")){
            System.out.println("page vote has been triggered");
        }else if(action.endsWith("user")){
            System.out.println("user (update) page ??");
        }else if(action.endsWith("resultats")){

            System.out.println("result page ??");
        }

    }
}
