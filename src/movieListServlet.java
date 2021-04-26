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
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;


@WebServlet(name = "movieListServlet", urlPatterns = "/api/movieList")
public class movieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Inside first");

            // Getting the parameters
            String movieTitle = request.getParameter("movieTitle");
            String movieYear = request.getParameter("movieYear");
            String movieDirector = request.getParameter("movieDirector");
            String movieStar = request.getParameter("movieStar");
            String movieGen = request.getParameter("movieGenre");

            System.out.println("Movie Title: " + movieTitle + " Movie Director: " + movieDirector + " Movie Star: " + movieStar + " Movie Year: " + movieYear + "Movie Genre: " + movieGen);

            PreparedStatement statement;

            // For the condition when the user browses a movie with *, means things that are not A-Z and 0-9
            if(movieTitle.equals("*")){
                String query = "select mr.title as 'title', mr.id as 'movieID', mr.year as 'year', mr.director as 'director', mr.rating as 'rating', g.name as 'singleGenre',\n" +
                        "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                        "s.name as \"singleActor\",\n" +
                        "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                        "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                        "from genres g, stars s,\n" +
                        "(select * from movies m left join ratings r on r.movieID = m.id) as mr, \n" +
                        "(select * from movies m inner join genres_in_movies gim on m.id = gim.movieId) as mgim,\n" +
                        "(select * from movies m inner join stars_in_movies sim on sim.movieId = m.id) as msim\n" +
                        "where g.id = mgim.genreId and mr.id = mgim.movieId and s.id = msim.starId and msim.id = mgim.movieId and mgim.title regexp '^[^a-zA-Z0-9]'\n" +
                        "group by title, year, director, rating\n";
                statement = conn.prepareStatement(query);
            }
            // Case when the movie year is inputted
            else if(!movieYear.equals(new String())){
                System.out.println("inside 2nd");
                // prepare query
                String query = "select mr.title as 'title', mr.id as 'movieID', mr.year as 'year', mr.director as 'director', mr.rating as 'rating', g.name as 'singleGenre',\n" +
                        "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                        "s.name as \"singleActor\",\n" +
                        "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                        "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                        "from genres g, stars s,\n" +
                        "(select * from movies m left join ratings r on r.movieID = m.id) as mr, \n" +
                        "(select * from movies m inner join genres_in_movies gim on m.id = gim.movieId) as mgim,\n" +
                        "(select * from movies m inner join stars_in_movies sim on sim.movieId = m.id) as msim\n" +
                        "where g.id = mgim.genreId and mr.id = mgim.movieId and s.id = msim.starId and msim.id = mgim.movieId\n" +
                        "AND mr.title like ? AND mr.year = ? and mr.director like ?\n" +
                        "group by title, year, director, rating\n" +
                        "having s.name like ?";

                // Declare our statement
                statement = conn.prepareStatement(query);

                statement.setString(1,  movieTitle + "%");
                statement.setString(2, movieYear);
                statement.setString(3,  "%"+ movieDirector + "%");
                statement.setString(4, "%"+ movieStar + "%");
            }

            /*
            Else general case where there are no constraints so far
            Only String (VARCHAR) fields are required to support substring matching. Thus, year should not support it.
             */
            else {
                System.out.println("inside third");
                // prepare query
                String query = "select mr.title as 'title', mr.id as 'movieID', mr.year as 'year', mr.director as 'director', mr.rating as 'rating', g.name as 'singleGenre',\n" +
                        "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                        "s.name as \"singleActor\",\n" +
                        "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                        "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                        "from genres g, stars s,\n" +
                        "(select * from movies m left join ratings r on r.movieID = m.id) as mr, \n" +
                        "(select * from movies m inner join genres_in_movies gim on m.id = gim.movieId) as mgim,\n" +
                        "(select * from movies m inner join stars_in_movies sim on sim.movieId = m.id) as msim\n" +
                        "where g.id = mgim.genreId and mr.id = mgim.movieId and s.id = msim.starId and msim.id = mgim.movieId\n" +
                        "AND mr.title like ? AND mr.year like ? and mr.director like ?\n" +
                        "group by title, year, director, rating\n" +
                        "having singleActor like ? and singleGenre like ?";

                // Declare our statement
                statement = conn.prepareStatement(query);
                statement.setString(1,  movieTitle + "%");
                statement.setString(2, movieYear + "%");
                statement.setString(3,  "%"+ movieDirector + "%");
                statement.setString(4, "%"+ movieStar + "%");
                statement.setString(5, "%" + movieGen + "%");
            }

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
                String movieID = rs.getString("movieId");
                String movieTit = rs.getString("title");
                String movieYr = rs.getString("year");
                String movieDirect = rs.getString("director");
                String movieGenre = rs.getString("genres");
                String movieActors = rs.getString("actors");
                String movieRating = rs.getString("rating");
                String starID = rs.getString("starId");

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movieID);
                jsonObject.addProperty("movie_title", movieTit);
                jsonObject.addProperty("movie_year", movieYr);
                jsonObject.addProperty("movie_director", movieDirect);
                jsonObject.addProperty("movie_genre", movieGenre);
                jsonObject.addProperty("movie_actors", movieActors);
                jsonObject.addProperty("movie_rating", movieRating);
                jsonObject.addProperty("star_id", starID);

                jsonArray.add(jsonObject);
            }

            rs.close();
            statement.close();

            // For debugging purposes
            System.out.println(jsonArray.toString());

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
