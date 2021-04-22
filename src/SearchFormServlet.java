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

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "FormServlet", urlPatterns = "/form")
public class SearchFormServlet extends HttpServlet {

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
            String movieTitle = request.getParameter("movie_title");
            String movieYear = request.getParameter("movie_year");
            String movieDirector = request.getParameter("movie_director");
            String movieStar = request.getParameter("movie_star");

            // Generate a SQL query
            String query = String.format("select m.title as 'Title', m.year as 'Year', m.director as 'Director',\n" +
                    "\tgroup_concat(s.name SEPARATOR ', ') Stars\n" +
                    "from movies m, stars s, stars_in_movies sim\n" +
                    "where m.id = sim.movieId and sim.starId = s.id\n" +
                    "and m.title like '%%%s%%' and m.year like '%%%s%%' and m.director like '%%%s%%'" +
                    "group by m.id", movieTitle, movieYear, movieDirector, movieStar);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            // Create a html <table>
            out.println("<table border>");

            // Iterate through each row of rs and create a table row <tr>
            out.println("<tr><td>Movie Title</td><td>Movie Year</td><td>Movie Director</td><td>Movie Star(s)</td></tr>");
            while (rs.next()) {
                String m_Title = rs.getString("Title");
                String m_Year = rs.getString("Year");
                String m_Director = rs.getString("Director");
//                String m_Stars = rs.getString("Stars");
//                List<String> starList = Arrays.asList(m_Stars.split(","));

                out.println(String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", m_Title, m_Year, m_Director));
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