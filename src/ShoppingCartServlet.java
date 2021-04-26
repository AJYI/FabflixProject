import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Date;

/**
 * This IndexServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "ShoppingCartServlet", urlPatterns = "/api/shoppingCart")
public class ShoppingCartServlet extends HttpServlet {

    /**
     * handles GET requests to store session information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        long lastAccessTime = session.getLastAccessedTime();

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("sessionID", sessionId);
        responseJsonObject.addProperty("lastAccessTime", new Date(lastAccessTime).toString());

        /**
         * What we will do is create a map that will store the movie name and the number of copies the
         * user ordered
         */

        HashMap<String, Integer> previousItems = (HashMap<String, Integer>) session.getAttribute("previousItems");
        if (previousItems.isEmpty()) {
            previousItems = new HashMap();
        }

        // Now here, we convert the map into a JsonObject
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String strResponseJsonObject = gson.toJson(previousItems, gsonType);

        response.getWriter().write(strResponseJsonObject);
    }

    /**
     * handles POST requests to add and show the item list information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String item = request.getParameter("item");
        System.out.println(item);
        HttpSession session = request.getSession();

        // get the previous items in a HashMap
        HashMap<String, Integer> previousItems = (HashMap<String, Integer>) session.getAttribute("previousItems");
        if (previousItems.isEmpty()) {
            previousItems = new HashMap();
            previousItems.put(item, 1);
            session.setAttribute("previousItems", previousItems);
        } else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (previousItems) {
                if(previousItems.containsKey(item)) {
                    previousItems.put(item, previousItems.get(item) + 1);
                } else {
                    previousItems.put(item, 1);
                }
            }
        }

        // Now here, we convert the map into a JsonObject
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String strResponseJsonObject = gson.toJson(previousItems, gsonType);

        response.getWriter().write(strResponseJsonObject);

    }
}