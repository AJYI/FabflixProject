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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentCCServlet extends HttpServlet {

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    // Auto login to database
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String creditCard = request.getParameter("creditCard");
        String expDate = request.getParameter("expDate");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        // Will be used in either login success/fail
        JsonObject jsonObject = new JsonObject();

        // Login Success
        if(validateUser(firstName, lastName, creditCard, expDate)){

            // Only needed if we want to save user info for session
            // request.getSession().setAttribute("user", new User(email));
            System.out.println("Hello Sucess");

            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("message", "success");
        }
        else{
            System.out.println("Hello Fail");
            //We had a login fail
            jsonObject.addProperty("status", "fail");
            // We don't want to notify the user if it's either id or password for security reasons
            jsonObject.addProperty("message", "Incorrect information; please enter again");
        }

        // set response status to 200 (OK)
        response.setStatus(200);
        response.getWriter().write(jsonObject.toString());
    }

    protected boolean validateUser(String fName, String lName, String userCC, String cardDate){
        boolean verified = false;
        try (Connection conn = dataSource.getConnection()){

            // Preparing the query
            String query = "SELECT EXISTS (SELECT cc.firstName, cc.lastName, cc.id, cc.expiration\n" +
                    "\tFROM creditcards cc\n" +
                    "WHERE cc.firstName = ? AND cc.lastName = ? AND cc.id = ? AND cc.expiration = ?) as foundCC";


            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setString(3, userCC);
            statement.setString(4, cardDate);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            // To get the 0/1
            rs.next();

            String foundCC = rs.getString("foundCC");

            // Means found
            if(Integer.parseInt(foundCC) == 1) {
                verified = true;
            }
            // Not found
            else{
                verified = false;
            }

            // Closing after opening
            rs.close();
            statement.close();
        }
        catch(Exception e){
            // Need to work on finding out a better way to write to the log
            System.out.println(e.getMessage());
        }
        return verified;
    }

}
