//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import java.util.ArrayList;
//
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@WebServlet(name = "ShoppingCartServlet", urlPatterns = "/api/shopping")
//public class ShoppingCartServlet extends HttpServlet {
//
//    /**
//     * handles GET requests to store session information
//     */
//
//    // Essentially takes all of the information and puts it into a JSON array
//    // and sends it back to the client
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // Checks to see if a session has been set up --> this is done by the login filter
//        HttpSession session = request.getSession();
//        SessionUser customer = (SessionUser) session.getAttribute("user");
//
//        ArrayList<ShoppingCartItem> customerCart = new ArrayList<ShoppingCartItem>();
//        customerCart = customer.accessCart(); // check to make sure this is valid;
//
//        JsonArray previousItemsJsonArray = new JsonArray();
//        JsonObject responseJsonObject = new JsonObject();
//        if (customer != null) {
//            // Start adding items into JSON Object
////            for (String movieTitle : customer.showCart().shoppingCart.keySet()) {
////                responseJsonObject.addProperty("movieTitle", movieTitle);
////                responseJsonObject.addProperty("movieQuantity", customer.showCart().getNumOfCopies(movieTitle));
////                responseJsonObject.addProperty("moviePrice", customer.showCart().moviePrices.get(movieTitle));
////                previousItemsJsonArray.add(responseJsonObject);
//            }
//        //}
////        else {
////            responseJsonObject.add("Movies", previousItemsJsonArray);
////        }
////
////
////        // write all the data into the jsonObject
////        response.getWriter().write(responseJsonObject.toString());
//    }
//
//    /**
//     * handles POST requests to add and show the item list information
//     */
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        doGet(request, response);
//    }
//}