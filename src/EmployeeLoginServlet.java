import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/EmployeeLoginValidator")
public class EmployeeLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("Went in here");
        /*
        Fetching the id and pass from the url
         */
        JsonObject jsonObject = new JsonObject();
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse, 1);
            String email = request.getParameter("email");
            String password = request.getParameter("pass");

            // Will be used in either login success/fail


            System.out.println("Up to here");

            // Login Success
            if(validateUser(email, password, request)){
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "success");
            }
            else{
                //We had a login fail
                jsonObject.addProperty("status", "fail");
                // We don't want to notify the user if it's either id or password for security reasons
                jsonObject.addProperty("message", "Incorrect credentials!");

            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("status", "ReCaptchaFail");
            // We don't want to notify the user if it's either id or password for security reasons
            jsonObject.addProperty("message", "Verify ReCaptcha");
        }

        // set response status to 200 (OK)
        response.setStatus(200);
        response.getWriter().write(jsonObject.toString());
    }

    protected boolean validateUser(String email, String pass, HttpServletRequest request){
        boolean verified = false;
        try (Connection conn = dataSource.getConnection()){
            // Preparing the query
            String query = "select e.email, e.password \n" +
                    "from employees e\n" +
                    "where e.email = ?";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, email);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            System.out.println("check");
            System.out.println(email + " " + pass);

            boolean success = false;
            // Means a result has been given
            if(rs.next()) {
                String encryptedPassword = rs.getString("password");
                System.out.println(encryptedPassword);
                success = new StrongPasswordEncryptor().checkPassword(pass, encryptedPassword);
                System.out.println(success);

                if(success){
                    verified = true;
                }
                else{
                    verified = false;
                }
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
