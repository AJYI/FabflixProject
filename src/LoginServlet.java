import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        Fetching the id and pass from the url
         */
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");


        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String email = request.getParameter("email");
        String password = request.getParameter("pass");

        // Will be used in either login success/fail
        JsonObject jsonObject = new JsonObject();

        // Login Success
        if(validateUser(email, password, request)){
            Cookie c = new Cookie("check", "blah");
            response.addCookie(c);

            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("message", "success");
        }
        else{
            //We had a login fail
            jsonObject.addProperty("status", "fail");
            // We don't want to notify the user if it's either id or password for security reasons
            jsonObject.addProperty("message", "Incorrect credentials!");

        }

        // set response status to 200 (OK)
        response.setStatus(200);
        response.getWriter().write(jsonObject.toString());
    }

    protected boolean validateUser(String email, String pass, HttpServletRequest request){
        boolean verified = false;
        try (Connection conn = dataSource.getConnection()){
            // Preparing the query
            String query = "select c.firstName, c.lastName, c.id, c.email, c.password from customers c \n" +
                    "where c.email = ?";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, email);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            boolean success = false;
            // Means a result has been given
            if(rs.next()) {
                String encryptedPassword = rs.getString("password");
                System.out.println(encryptedPassword);
                success = new StrongPasswordEncryptor().checkPassword(pass, encryptedPassword);
                System.out.println(success);

                if(success){
                    /*
                    ###########################
                    We set the user here
                    ###########################
                     */
                    String firstName = rs.getString("c.firstName");
                    String lastName = rs.getString("c.lastName");
                    String customerID = rs.getString("c.id");
                    request.getSession().setAttribute("user", new SessionUser(firstName, lastName, customerID));

                    /*
                    ###############################################
                    We create the shopping cart session object here
                    ################################################
                     */
                    SessionCart.initializeCart(request);

                    HttpSession session = request.getSession(true);
                    SessionUser check = (SessionUser) session.getAttribute("user");
                    check.print();

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
