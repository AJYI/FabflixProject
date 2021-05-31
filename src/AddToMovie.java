import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Random;

@WebServlet(name = "AddToMovie", urlPatterns = "/fabflix/NewMovie/addMovie")
public class AddToMovie extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {



        response.setContentType("application/json"); // Response mime type

        // Retrieving the parameter of ActorName
        String movieTitle = request.getParameter("movieTitle");
        String directorName = request.getParameter("directorName");
        String movieYear = request.getParameter("movieYear");
        String genre = request.getParameter("genre");
        String star = request.getParameter("star");

        // Output stream to STDOUT

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            String query = "call add_movie(?, ?, ?, ?, ?);";

            // Perform the query
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, genre);
            statement.setString(2, star);
            statement.setString(3, movieTitle);
            statement.setInt(4, Integer.parseInt(movieYear));
            statement.setString(5, directorName);
            //System.out.println("in here2");
            ResultSet rs = statement.executeQuery();

            //System.out.println("in here");
            // Getting ResultSet
            rs.next();
            JsonObject jsonObject = new JsonObject();

            String result = rs.getString("result");
            if (result.equals("1")){
                String m_id = rs.getString("m_id");
                String g_id = rs.getString("g_id");
                String s_id = rs.getString("s_id");

                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "success");
                jsonObject.addProperty("m_id", m_id);
                jsonObject.addProperty("g_id", g_id);
                jsonObject.addProperty("s_id", s_id);
            }
            // This was an error
            else{
                jsonObject.addProperty("status", "fail");
                jsonObject.addProperty("message", "Error: Duplicate Movies!");
            }

            rs.close();

            // set response status to 200 (OK)
            response.setStatus(200);
            response.getWriter().write(jsonObject.toString());
            System.out.println(jsonObject.toString());
            statement.close();


        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            //We had a login fail
            jsonObject.addProperty("status", "fail");
            // We don't want to notify the user if it's either id or password for security reasons
            jsonObject.addProperty("message", "Error: Incorrect Type(s)");
            response.setStatus(200);
            response.getWriter().write(jsonObject.toString());
        }
        // always remember to close db connection after usage. Here it's done by try-with-resources
    }
}
