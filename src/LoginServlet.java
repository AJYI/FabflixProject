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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


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
        String email = request.getParameter("email");
        String password = request.getParameter("pass");

        // Will be used in either login success/fail
        JsonObject jsonObject = new JsonObject();

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

        // set response status to 200 (OK)
        response.setStatus(200);
        response.getWriter().write(jsonObject.toString());
    }

    protected boolean validateUser(String email, String pass, HttpServletRequest request){
        boolean verified = false;
        try (Connection conn = dataSource.getConnection()){
            // Preparing the query
            String query = "select c.firstName, c.lastName, c.id, c.email, c.password from customers c \n" +
                    "where c.email = ? and c.password = ?";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, email);
            statement.setString(2, pass);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            // Means found
            if(rs.next()) {
                String firstName = rs.getString("c.firstName");
                String lastName = rs.getString("c.lastName");
                String customerID = rs.getString("c.id");
                request.getSession().setAttribute("user", new User(firstName, lastName, customerID));

                HttpSession session = request.getSession(true);
                User check = (User) session.getAttribute("user");
                check.print();

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
