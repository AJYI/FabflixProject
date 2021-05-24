import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// server endpoint URL
@WebServlet("/autocomplete")
public class AutoCompleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /*
     * populate the Super hero hash map.
     * Key is hero ID. Value is hero name.
     */
    public static HashMap<String, String> actorMap;

    void getDb(HttpServletRequest request, JsonArray jsonArray) {
        String url = "jdbc:mysql://localhost:3306/moviedb?rewriteBatchedStatements=true";
        String user = "mytestuser";
        String password = "My6$Password";

        actorMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
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
            System.out.println("something");


            System.out.println("Entering into the hashMap");
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
//        superHeroMap.put(1, "Blade");
//        superHeroMap.put(2, "Ghost Rider");
//        superHeroMap.put(3, "Luke Cage");
//        superHeroMap.put(4, "Silver Surfer");
//        superHeroMap.put(5, "Beast");
//        superHeroMap.put(6, "Thing");
//        superHeroMap.put(7, "Black Panther");
//        superHeroMap.put(8, "Invisible Woman");
//        superHeroMap.put(9, "Nick Fury");
//        superHeroMap.put(10, "Storm");
//        superHeroMap.put(11, "Iron Man");
//        superHeroMap.put(12, "Professor X");
//        superHeroMap.put(13, "Hulk");
//        superHeroMap.put(14, "Cyclops");
//        superHeroMap.put(15, "Thor");
//        superHeroMap.put(16, "Jean Grey");
//        superHeroMap.put(17, "Wolverine");
//        superHeroMap.put(18, "Daredevil");
//        superHeroMap.put(19, "Captain America");
//        superHeroMap.put(20, "Spider-Man");
//
//        superHeroMap.put(101, "Superman");
//        superHeroMap.put(102, "Batman");
//        superHeroMap.put(103, "Wonder Woman");
//        superHeroMap.put(104, "Flash");
//        superHeroMap.put(105, "Green Lantern");
//        superHeroMap.put(106, "Catwoman");
//        superHeroMap.put(107, "Nightwing");
//        superHeroMap.put(108, "Captain Marvel");
//        superHeroMap.put(109, "Aquaman");
//        superHeroMap.put(110, "Green Arrow");
//        superHeroMap.put(111, "Martian Manhunter");
//        superHeroMap.put(112, "Batgirl");
//        superHeroMap.put(113, "Supergirl");
//        superHeroMap.put(114, "Black Canary");
//        superHeroMap.put(115, "Hawkgirl");
//        superHeroMap.put(116, "Cyborg");
//        superHeroMap.put(117, "Robin");
    }

    public AutoCompleteServlet() {
        super();
    }

    /*
     *
     * Match the query against superheroes and return a JSON response.
     *
     * For example, if the query is "super":
     * The JSON response look like this:
     * [
     * 	{ "value": "Superman", "data": { "heroID": 101 } },
     * 	{ "value": "Supergirl", "data": { "heroID": 113 } }
     * ]
     *
     * The format is like this because it can be directly used by the
     *   JSON auto complete library this example is using. So that you don't have to convert the format.
     *
     * The response contains a list of suggestions.
     * In each suggestion object, the "value" is the item string shown in the dropdown list,
     *   the "data" object can contain any additional information.
     *
     *
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Hi");
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

            // search on superheroes and add the results to JSON Array
            // this example only does a substring match
            // TODO: in project 4, you should do full text search with MySQL to find the matches on movies and stars

//            for (String id : actorMap.keySet()) {
//                if(jsonArray.size() >= 10){
//                    break;
//                }
//                String title = actorMap.get(id);
//                jsonArray.add(generateJsonObject(id, title));
//            }
            System.out.println(jsonArray.toString());
            response.getWriter().write(jsonArray.toString());
            return;
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(500, e.getMessage());
        }
    }

    /*
     * Generate the JSON Object from hero to be like this format:
     * {
     *   "value": "Iron Man",
     *   "data": { "heroID": 11 }
     * }
     *
     */
//    private static JsonObject generateJsonObject(String heroID, String heroName) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("value", heroName);
//
//        JsonObject additionalDataJsonObject = new JsonObject();
//        additionalDataJsonObject.addProperty("heroID", heroID);
//
//        jsonObject.add("data", additionalDataJsonObject);
//        return jsonObject;
//    }

    private static JsonObject generateJsonObject(String movieID, String movieTitle) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", movieTitle);

        JsonObject additionalDataJsonObject = new JsonObject();
        additionalDataJsonObject.addProperty("movieID", movieID);

        jsonObject.add("data", additionalDataJsonObject);
        return jsonObject;
    }

}