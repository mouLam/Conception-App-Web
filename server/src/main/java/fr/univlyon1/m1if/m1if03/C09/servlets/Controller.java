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
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String action = req.getRequestURI();
        if(action.endsWith("vote")){
            req.getRequestDispatcher("/election/vote").forward(req,resp);
        }else if(action.endsWith("user")){
            req.getRequestDispatcher("/election/user").forward(req,resp);
        }else if(action.endsWith("deleteVote")){
            req.getRequestDispatcher("/election/deleteVote").forward(req,resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String action = req.getRequestURI();
        if(action.endsWith("listBallots")){
            req.getRequestDispatcher("/election/listBallots").forward(req,resp);
        }else if(action.endsWith("vote")){
            req.getRequestDispatcher("/election/vote").forward(req,resp);
        }else if(action.endsWith("user")){
            req.getRequestDispatcher("/election/user").forward(req,resp);
        }else if(action.endsWith("ballot")){
            req.getRequestDispatcher("/election/ballot").forward(req, resp);
        }else if(action.endsWith("resultats")){
            req.getRequestDispatcher("/election/resultats").forward(req, resp);
        }else if(action.endsWith("DecoController")) {
            req.getRequestDispatcher("/DecoController").forward(req, resp);
        }
    }
}
