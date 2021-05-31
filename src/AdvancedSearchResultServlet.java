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

/*
For the advance search part of our html
 */

@WebServlet(name = "advancedSearchResultServlet", urlPatterns = "/AdvancedSearchPages/advancedSearchResult")
public class AdvancedSearchResultServlet extends HttpServlet {
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

        // Get a instance of current session on the request
        SessionURL.rememberSession(request);
        SessionURL.printCurrentSession(request);

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {

            // Getting the parameters
            String movieTitle = request.getParameter("movieTitle");
            String movieYear = request.getParameter("movieYear");
            String movieDirector = request.getParameter("movieDirector");
            String movieStar = request.getParameter("movieStar");
            String sort1 = request.getParameter("sort1");
            String sort2 = request.getParameter("sort2");

            if(movieTitle.equals("null")) {
                movieTitle = "";
            }
            if(movieYear.equals("null")){
                movieYear = "";
            }
            if(movieDirector.equals("null")){
                movieDirector = "";
            }
            if(movieStar.equals("null")){
                movieStar = "";
            }

            System.out.println("Movie Title: " + movieTitle + " Movie Director: " + movieDirector + " Movie Star: " + movieStar + " Movie Year: " + movieYear + " sort1:" + sort1 + " sort2:" + sort2);

            PreparedStatement statement;
            char c1 = ' ';
            if(!sort1.equals(new String())){
                c1 = sort1.charAt(0);
            }
            //System.out.println("c1");

            if (c1 == 'T'){
                //System.out.println("in TTT");
                // When a year is not inputted
                if(movieYear.equals(new String())){
                    String query = "select m.title as 'title', m.id as 'movieID', m.year as 'year', m.director as 'director', r.rating as 'rating',\n" +
                            "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                            "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                            "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                            "from genres g, genres_in_movies gim, (select distinct sim.movieId from stars s, stars_in_movies sim where s.id = sim.starId and s.name like ?) as sID, \n" +
                            "stars_in_movies sim, stars s, movies m\n" +
                            "left join ratings r on r.movieId = m.id\n" +
                            "where sID.movieId = sim.movieId and m.id = sID.movieId and sim.starId = s.id and m.id = gim.movieId and gim.genreId = g.id and m.title like ? and m.director like ?\n" +
                            "group by title\n";

                    if(sort1.equals("TA")){
                        if(sort2.equals("RA")){
                            query += "order by title ASC, rating DESC\n";
                        }
                        else if (sort2.equals("RD")){
                            query += "order by title ASC, rating ASC\n";
                        }
                        else{
                            query += "order by title ASC\n";
                        }
                    }
                    else if (sort1.equals("TD")){
                        //System.out.println("Entered here" + sort1 + " " + sort2) ;
                        if(sort2.equals("RA")){
                            query += "order by title DESC, rating DESC\n";
                        }
                        else if(sort2.equals("RD")){
                            query += "order by title DESC, rating ASC\n";
                        }
                        else{
                            query += "order by title DESC\n";
                        }
                    }

                    statement = conn.prepareStatement(query);
                    statement.setString(1, "%" + movieStar + "%");
                    statement.setString(2,  movieTitle + "%");
                    statement.setString(3,  "%" + movieDirector + "%");

                }
                else{
                    String query = "select m.title as 'title', m.id as 'movieID', m.year as 'year', m.director as 'director', r.rating as 'rating',\n" +
                            "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                            "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                            "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                            "from genres g, genres_in_movies gim, (select distinct sim.movieId from stars s, stars_in_movies sim where s.id = sim.starId and s.name like ?) as sID, \n" +
                            "stars_in_movies sim, stars s, movies m\n" +
                            "left join ratings r on r.movieId = m.id\n" +
                            "where sID.movieId = sim.movieId and m.id = sID.movieId and sim.starId = s.id and m.id = gim.movieId and gim.genreId = g.id and m.title like ? and m.director like ? and m.year = ?\n" +
                            "group by title\n";

                    if(sort1.equals("TA")){
                        if(sort2.equals("RA")){
                            query += "order by title ASC, rating DESC\n";
                        }
                        else if (sort2.equals("RD")){
                            query += "order by title ASC, rating ASC\n";
                        }
                        else{
                            query += "order by title ASC\n";
                        }
                    }
                    else if (sort1.equals("TD")){
                        //System.out.println("Entered here" + sort1 + " " + sort2) ;
                        if(sort2.equals("RA")){
                            query += "order by title DESC, rating DESC\n";
                        }
                        else if(sort2.equals("RD")){
                            query += "order by title DESC, rating ASC\n";
                        }
                        else{
                            query += "order by title DESC\n";
                        }
                    }

                    statement = conn.prepareStatement(query);

                    statement.setString(1, "%" + movieStar + "%");
                    statement.setString(2,  movieTitle + "%");
                    statement.setString(3,  "%" + movieDirector + "%");
                    statement.setString(4,  movieYear);
                }
            }

            else if (c1 == 'R'){
                //System.out.println("in RRR");
                // When a year is not inputted
                if(movieYear.equals(new String())){
                    String query = "select m.title as 'title', m.id as 'movieID', m.year as 'year', m.director as 'director', r.rating as 'rating',\n" +
                            "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                            "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                            "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                            "from genres g, genres_in_movies gim, (select distinct sim.movieId from stars s, stars_in_movies sim where s.id = sim.starId and s.name like ?) as sID, \n" +
                            "stars_in_movies sim, stars s, movies m\n" +
                            "left join ratings r on r.movieId = m.id\n" +
                            "where sID.movieId = sim.movieId and m.id = sID.movieId and sim.starId = s.id and m.id = gim.movieId and gim.genreId = g.id and m.title like ? and m.director like ?\n" +
                            "group by title\n";

                    if(sort1.equals("RA")){
                        if(sort2.equals("TA")){
                            query += "order by rating DESC, title ASC ";
                        }
                        else if (sort2.equals("TD")){
                            query += "order by rating DESC, title DESC";
                        }
                        else{
                            query += "order by rating DESC";
                        }
                    }
                    else{
                        if(sort2.equals("TA")){
                            query += "order by rating ASC, title ASC";
                        }
                        else if(sort2.equals("TD")){
                            query += "order by rating ASC, title DESC";
                        }
                        else{
                            query += "order by rating ASC";
                        }
                    }

                    statement = conn.prepareStatement(query);
                    statement.setString(1, "%" + movieStar + "%");
                    statement.setString(2,  movieTitle + "%");
                    statement.setString(3,  "%" + movieDirector + "%");

                }
                else{
                    String query = "select m.title as 'title', m.id as 'movieID', m.year as 'year', m.director as 'director', r.rating as 'rating',\n" +
                            "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                            "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                            "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                            "from genres g, genres_in_movies gim, (select distinct sim.movieId from stars s, stars_in_movies sim where s.id = sim.starId and s.name like ?) as sID, \n" +
                            "stars_in_movies sim, stars s, movies m\n" +
                            "left join ratings r on r.movieId = m.id\n" +
                            "where sID.movieId = sim.movieId and m.id = sID.movieId and sim.starId = s.id and m.id = gim.movieId and gim.genreId = g.id and m.title like ? and m.director like ? and m.year = ?\n" +
                            "group by title\n";

                    if(sort1.equals("RA")){
                        if(sort2.equals("TA")){
                            query += "order by rating DESC, title ASC ";
                        }
                        else if (sort2.equals("TD")){
                            query += "order by rating DESC, title DESC";
                        }
                        else{
                            query += "order by rating DESC";
                        }
                    }
                    else{
                        if(sort2.equals("TA")){
                            query += "order by rating ASC, title ASC";
                        }
                        else if(sort2.equals("TD")){
                            query += "order by rating ASC, title DESC";
                        }
                        else{
                            query += "order by rating ASC";
                        }
                    }

                    statement = conn.prepareStatement(query);

                    statement.setString(1, "%" + movieStar + "%");
                    statement.setString(2,  movieTitle + "%");
                    statement.setString(3,  "%" + movieDirector + "%");
                    statement.setString(4,  movieYear);
                }
            }
            else {
                //System.out.println("In here!!!!!!!!");
                // When a year is not inputted
                if(movieYear.equals(new String())){
                    String query = "select m.title as 'title', m.id as 'movieID', m.year as 'year', m.director as 'director', r.rating as 'rating',\n" +
                            "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                            "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                            "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                            "from genres g, genres_in_movies gim, (select distinct sim.movieId from stars s, stars_in_movies sim where s.id = sim.starId and s.name like ?) as sID, \n" +
                            "stars_in_movies sim, stars s, movies m\n" +
                            "left join ratings r on r.movieId = m.id\n" +
                            "where sID.movieId = sim.movieId and m.id = sID.movieId and sim.starId = s.id and m.id = gim.movieId and gim.genreId = g.id and m.title like ? and m.director like ?\n" +
                            "group by title\n";
                    statement = conn.prepareStatement(query);
                    statement.setString(1, "%" + movieStar + "%");
                    statement.setString(2,  movieTitle + "%");
                    statement.setString(3,  "%" + movieDirector + "%");

                }
                else{
                    String query = "select m.title as 'title', m.id as 'movieID', m.year as 'year', m.director as 'director', r.rating as 'rating',\n" +
                            "substring_index(group_concat(distinct g.name), ',', 3) 'genres',\n" +
                            "substring_index(group_concat(distinct s.name), ',', 3) 'actors',\n" +
                            "substring_index(group_concat(distinct s.id order by s.name), ',', 3) 'starId'\n" +
                            "from genres g, genres_in_movies gim, (select distinct sim.movieId from stars s, stars_in_movies sim where s.id = sim.starId and s.name like ?) as sID, \n" +
                            "stars_in_movies sim, stars s, movies m\n" +
                            "left join ratings r on r.movieId = m.id\n" +
                            "where sID.movieId = sim.movieId and m.id = sID.movieId and sim.starId = s.id and m.id = gim.movieId and gim.genreId = g.id and m.title like ? and m.director like ? and m.year = ?\n" +
                            "group by title\n";
                    statement = conn.prepareStatement(query);

                    statement.setString(1, "%" + movieStar + "%");
                    statement.setString(2,  movieTitle + "%");
                    statement.setString(3,  "%" + movieDirector + "%");
                    statement.setString(4,  movieYear);
                }
            }

            //System.out.println("reached here");

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
            //System.out.println(jsonArray.toString());

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
    }
}
