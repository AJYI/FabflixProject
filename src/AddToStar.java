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


/*
This class inserted to the star class through the employee login
 */


@WebServlet(name = "AddToStar", urlPatterns = "/fabflix/EmployeeStarInsert/addStar")
public class AddToStar extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/master");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("HI");

        response.setContentType("application/json"); // Response mime type

        // Retrieving the parameter of ActorName
        String ActorName = request.getParameter("ActorName");
        String BirthYear = request.getParameter("BirthYear");

        // Output stream to STDOUT


        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            String query = "select s.id as 'maxID' from stars s order by s.id desc limit 1;";

            // Perform the query
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String highestStarID = rs.getString("maxID");
            String IDChar = highestStarID.substring(0, 2);
            String NumChar = highestStarID.substring(2);
            int numConvert = Integer.parseInt(NumChar);
            Random rand = new Random();
            numConvert += rand.nextInt(50) + 1;
            NumChar = String.valueOf(numConvert);

            String newID = IDChar + NumChar;
            rs.close();

            if(BirthYear.equals(new String())) {
                BirthYear = null;
            }

            // Initializing a new query and inserting into the database
            query = "INSERT INTO stars VALUES(?, ?, ?)";
            statement = conn.prepareStatement(query);


            statement.setString(1,  newID);
            statement.setString(2,  ActorName);

            if(BirthYear == null) {
                System.out.println("in here");
                statement.setNull(3, Types.INTEGER);
            }
            else{
                System.out.println("in here2");
                System.out.println(BirthYear);
                int i = Integer.parseInt(BirthYear);
                statement.setInt(3, i);
            }


            System.out.println(statement);
            System.out.println("checker");
            int num = statement.executeUpdate();

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("message", "success");
            jsonObject.addProperty("id", newID);

            // set response status to 200 (OK)
            response.setStatus(200);
            response.getWriter().write(jsonObject.toString());
            System.out.println(jsonObject.toString());
            rs.close();
            statement.close();


        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            //We had a login fail
            jsonObject.addProperty("status", "fail");
            // We don't want to notify the user if it's either id or password for security reasons
            jsonObject.addProperty("message", "Incorrect credentials!");
            response.setStatus(200);
            response.getWriter().write(jsonObject.toString());
        }

        // always remember to close db connection after usage. Here it's done by try-with-resources
    }

}
