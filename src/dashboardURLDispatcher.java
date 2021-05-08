import javax.servlet.RequestDispatcher;
        import javax.servlet.ServletException;
        import javax.servlet.annotation.WebServlet;
        import javax.servlet.http.*;
        import java.io.IOException;

@WebServlet(name = "EmployeeLoginDispatcher", urlPatterns = "/_dashboard")
public class dashboardURLDispatcher extends HttpServlet {

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //response.sendRedirect("fabflix/employeeLogin.html");
        RequestDispatcher rd = request.getRequestDispatcher("fabflix/employeeLogin.html");
        rd.forward(request, response);
    }
}