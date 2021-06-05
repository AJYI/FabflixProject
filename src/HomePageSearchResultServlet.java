import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.regex.Pattern;


@WebServlet(name = "HomePageSearchResultServlet", urlPatterns = "/HomePageSearch/homePageSearchResult")
public class HomePageSearchResultServlet extends HttpServlet {
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
        long JDBCStart, JDBCEnd, TSStart, TSEnd;
        TSStart = System.nanoTime();
        //System.out.println("HI");
        //System.out.println("HI");


        //System.out.println("Entered");
        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a instance of current session on the request
        SessionURL.rememberSession(request);
        SessionURL.printCurrentSession(request);

        // Get a connection from dataSource and let resource manager close the connection after usage.

        // We start the JDBC Part
        JDBCStart = System.nanoTime();

        try (Connection conn = dataSource.getConnection()) {
            //System.out.println("wrong here");
            // Getting the parameters
            String movieTitle = request.getParameter("movieTitle");
            System.out.println(movieTitle);
            String[] movieTitleArr = movieTitle.split("[-+*/= ]");
            String query = "SELECT * FROM ft WHERE MATCH(movieTitle) AGAINST (? IN BOOLEAN MODE);";

            String actualQuery = new String();
            for(int i = 0; i < movieTitleArr.length; i++){
                actualQuery += movieTitleArr[i];
                if(i != movieTitleArr.length-1){
                    actualQuery += "* ";
                }
                else{
                    actualQuery += "*";
                }
            }
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, actualQuery);
            System.out.println(statement);
            ResultSet rs1 = statement.executeQuery();

            // We end the JDBC Part
            JDBCEnd = System.nanoTime();

            //System.out.println("something");

            JsonArray jsonArray = new JsonArray();
            while(rs1.next()){
                String movieID = rs1.getString("movieID");
                String movieTit = rs1.getString("movieTitle");
                String movieYr = rs1.getString("movieYear");
                String movieDirect = rs1.getString("movieDirector");
                String movieGenre = rs1.getString("genres");
                String movieActors = rs1.getString("actors");
                String movieRating = rs1.getString("ratings");
                String starID = rs1.getString("starId");

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

            // The end of query things
            TSEnd = System.nanoTime();

            // Obviously, TS > TJ
            long TSvalue, TJvalue;
            TSvalue = TSEnd - TSStart;
            TJvalue = JDBCEnd - JDBCStart;


            System.out.println(TSvalue + " " + TJvalue);

            rs1.close();
            statement.close();

            // For debugging purposes
            //System.out.println(jsonArray.toString());

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            // SOURCE
            // https://www.tutorialspoint.com/Java-Program-to-Append-Text-to-an-Existing-File
            // https://beginnersbook.com/2014/01/how-to-write-to-a-file-in-java-using-fileoutputstream/

            File fileObj = new File("/home/ubuntu/projects/logFile.txt");

            if(fileObj.exists()){
                fileObj.createNewFile();
            }
//

            FileOutputStream fileWriter = new FileOutputStream(fileObj, true);
            String value = TSvalue + " " + TJvalue + "\n";
            byte[] bytesArray = value.getBytes();
            //System.out.println("hi");
            fileWriter.write(bytesArray);
            fileWriter.flush();
            fileWriter.close();
            //System.out.println("hi");

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