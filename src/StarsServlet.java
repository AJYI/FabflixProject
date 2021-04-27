import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "StarsServlet", urlPatterns = "/api/star")
public class StarsServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xmls
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

        // Retrieve parameter id from url request.
        String starId = request.getParameter("id");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            String query = "select s.name as 'starName', s.birthYear as 'birthYear',\n" +
                    "group_concat(m.title order by m.year DESC separator '|') as 'movies',\n" +
                    "group_concat(m.id order by m.year DESC separator '|') as 'movieID'\n" +
                    "from movies m, stars s, stars_in_movies sim where sim.starId = s.id and s.id = ? and m.id = sim.movieId\n" +
                    "order by m.year desc";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,  starId);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            rs.next();

            String starName = rs.getString("starName");
            String birthYear = rs.getString("birthYear");
            String movieID = rs.getString("movieID");
            String movies = rs.getString("movies");

            // Create a JsonObject based on the data we retrieve from rs
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("star_name", starName);
            jsonObject.addProperty("star_birth_year", birthYear);
            jsonObject.addProperty("movie_id", movieID);
            jsonObject.addProperty("movies", movies);

            System.out.println(jsonArray.toString());
            jsonArray.add(jsonObject);

            rs.close();
            statement.close();

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // always remember to close db connection after usage. Here it's done by try-with-resources
    }

}