import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

// This class will handle the sessions url during the searches
public class SessionURL {
    public static void rememberSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        String sessionURL = (String) session.getAttribute("sessionURL");
        session.setAttribute("sessionURL", request.getRequestURL().toString() + ".html?" + request.getQueryString().toString());
    }

    public static void printPreviousSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        String sessionURL = (String) session.getAttribute("sessionURL");
        System.out.println("checking: sessionURL="+ sessionURL);
    }

    public static String returnSavedSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        String sessionURL = (String) session.getAttribute("sessionURL");
        if (sessionURL == null){
            sessionURL = "index.html";
        }
        return sessionURL;
    }
}
