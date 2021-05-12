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

@WebServlet(name = "MovieServlet", urlPatterns = "/MovieInformation/movie")
public class MovieServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

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

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        System.out.println("Here");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource
            
            // We append the id with the query for easier searching.
            String query = "select m.title as 'title', m.year as 'year', m.director as 'director', group_concat(distinct g.name) as genres ,tester.*, r.rating as 'rating'\n" +
                    "from stars_in_movies sim, stars s, \n" +
                    "(select group_concat(test.name) as \"actors\", group_concat(test.starzId) as \"starId\" from\n" +
                    "(select s.id as 'starzId', s.name as 'name', count(s.name) as moviesStarredIn\n" +
                    "from stars_in_movies sim, stars s, movies m,\n" +
                    "(select sim.starId from stars_in_movies sim, stars s where sim.starId = s.id and sim.movieId = ?) as starIdentifier\n" +
                    "where s.id = starIdentifier.starId and sim.starId = s.id and m.id = sim.movieId\n" +
                    "group by s.name\n" +
                    "order by moviesStarredIn desc) as test) as tester,\n" +
                    "genres_in_movies gim, genres g , movies m\n" +
                    "left join ratings r on r.movieId = m.id\n" +
                    "where m.id = ? and m.id = sim.movieId and sim.starId = s.id and m.id = gim.movieId and g.id = gim.genreId\n" +
                    "group by title";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,  id);
            statement.setString(2,  id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            rs.next();

            String movieTitle = rs.getString("title");
            String movieYear = rs.getString("year");
            String movieDirector = rs.getString("director");
            String movieGenres = rs.getString("genres");
            String movieActors = rs.getString("actors");
            String movieRatings = rs.getString("rating");
            String movieStarIds = rs.getString("starId");

            // Create a JsonObject based on the data we retrieve from rs
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("movie_title", movieTitle);
            jsonObject.addProperty("movie_year", movieYear);
            jsonObject.addProperty("movie_director", movieDirector);
            jsonObject.addProperty("movie_genres", movieGenres);
            jsonObject.addProperty("movie_actors", movieActors);
            jsonObject.addProperty("movie_rating", movieRatings);
            jsonObject.addProperty("movie_star_ids", movieStarIds);

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
