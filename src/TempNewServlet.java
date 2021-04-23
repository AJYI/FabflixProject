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
import java.util.Arrays;
import java.util.List;

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */


@WebServlet(name = "TempMovieServlet", urlPatterns = "api/tempMovieList")
public class TempMovieServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Building page head with title
        out.println("<html><head><title>MovieDBExample: Found Records</title></head>");

        // Building page body
        out.println("<body><h1>MovieDBExample: Found Records</h1>");


        try {

            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();

            // Declare a new statement
            Statement statement = dbCon.createStatement();

            // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
            String movieTitle = request.getParameter("movieTitle");
            String movieYear = request.getParameter("movieYear");
            String movieDirector = request.getParameter("movieDirector");
            String movieStar = request.getParameter("movieStar");
            String movieGenre = request.getParameter("movieGenre");


            // Generate a SQL query
            String query = String.format("SELECT m.title as title,\n" +
                    "        m.id as movieId,\n" +
                    "        m.year as year,\n" +
                    "        m.director as director,\n" +
                    "        r.rating as rating,\n" +
                    "        substring_index(group_concat(distinct g.name SEPARATOR ', '), ', ', 3) genres,\n" +
                    "        substring_index(group_concat(s.name SEPARATOR ', '), ', ', 3) actors,\n" +
                    "        substring_index(group_concat(s.id SEPARATOR ', '), ', ', 3) starId\n" +
                    "        FROM (SELECT r.rating, r.movieId from ratings r order by r.rating desc limit 20) as r,\n" +
                    "        movies m,\n" +
                    "        genres_in_movies gim,\n" +
                    "        genres g,\n" +
                    "        stars_in_movies sim,\n" +
                    "        stars s\n" +
                    "        where r.movieId = m.id AND m.id = gim.movieId AND gim.genreId = g.id AND m.id = sim.movieId AND sim.starID = s.id\n" +
                    "        and m.title like '%%%s%%' and m.year like '%%%s%%' and m.director like '%%%s%%'\n" +
                    "        group by m.title\n" +
                    "        having actors like '%%%s%%' and genres like 's%%'\n" +
                    "        order by r.rating desc, m.title asc\n", movieTitle, movieYear, movieDirector, movieStar, movieGenre);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            // Create a html <table>
            out.println("<table border>");

            // Iterate through each row of rs and create a table row <tr>
            out.println("<tr><td>Movie Title</td><td>Movie Year</td><td>Movie Director</td><td>Movie Rating</td><td>Movie Star(s)</td><td>Genres</td></tr>");
            while (rs.next()) {
                String m_Title = rs.getString("title");
                String m_Year = rs.getString("year");
                String m_Director = rs.getString("director");
                String m_Rating = rs.getString("rating");
                String m_Stars = rs.getString("actors");
                //          List<String> starList = Arrays.asList(m_Stars.split(","));
                String m_Genres = rs.getString("genres");

                out.println(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", m_Title, m_Year, m_Director, m_Rating, m_Genres, m_Stars));
            }
            out.println("</table>");


            // Close all structures
            rs.close();
            statement.close();
            dbCon.close();

        } catch (Exception ex) {
            ex.printStackTrace();

            // Output Error Massage to html
            out.println(String.format("<html><head><title>MovieDBExample: Error</title></head>\n<body><p>SQL error in doGet: %s</p></body></html>", ex.getMessage()));
            return;
        }
        out.close();
    }
}