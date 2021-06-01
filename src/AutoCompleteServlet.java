import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// server endpoint URL
@WebServlet("/autocomplete")
public class AutoCompleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static HashMap<String, String> actorMap;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    void getDb(HttpServletRequest request, JsonArray jsonArray) {
        actorMap = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Getting Results from the database");
            String q = request.getParameter("query");
            System.out.println(q);
            String[] movieTitleArr = q.split("[-+*/= ]");
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
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                if(jsonArray.size() >= 10) {
                    break;
                }
                String ID = rs.getString("movieID");
                String movieTitle = rs.getString("movieTitle");
                jsonArray.add(generateJsonObject(ID, movieTitle));
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AutoCompleteServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // setup the response json arrray
            JsonArray jsonArray = new JsonArray();
            getDb(request, jsonArray);

            // get the query string from parameter
            String query = request.getParameter("query");

            // return the empty json array if query is null or empty
            if (query == null || query.trim().isEmpty() || query.length() < 3) {
                response.getWriter().write(jsonArray.toString());
                return;
            }

            System.out.println("The JSON ARRAY FOR AUTOCOMPLETE SERVER: " + jsonArray.toString());
            response.getWriter().write(jsonArray.toString());
            return;
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(500, e.getMessage());
        }
    }


    private static JsonObject generateJsonObject(String movieID, String movieTitle) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", movieTitle);

        JsonObject additionalDataJsonObject = new JsonObject();
        additionalDataJsonObject.addProperty("movieID", movieID);

        jsonObject.add("data", additionalDataJsonObject);
        return jsonObject;
    }
}