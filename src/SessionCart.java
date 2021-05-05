import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;


// This class will handle the sessions url during the searches
public class SessionCart {

    // We initialize the cart
    public static void initializeCart(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        ArrayList<ItemObject> items = new ArrayList<ItemObject>();
        session.setAttribute("cart", items);
    }

    // We have to add the item to the cart by getting the session
    public static void addToCart(HttpServletRequest request, ItemObject item){
        HttpSession session = request.getSession(true);
        ArrayList<ItemObject> items = (ArrayList<ItemObject>) session.getAttribute("cart");
        String itemName = item.getMovieTitle();

        // Gotta check whether the item is already in the ArrayList
        // If it does exist, we have to increment it
        if(!checkCart(items, itemName)){
            incrementCart(items, itemName);
            session.setAttribute("cart", items);
        }

        // Adds to the arrayList
        items.add(item);
        session.setAttribute("cart", items);
    }

    //increment item
    public static void incrementCart(ArrayList<ItemObject> items, String itemName){
        int index = findIndex(items, itemName);
        items.get(index).updateQuantity(items.get(index).getQuantity() + 1);
    }

    public static void updateCart(HttpServletRequest request, String itemName, int quantity){
        HttpSession session = request.getSession(true);
        ArrayList<ItemObject> items = (ArrayList<ItemObject>) session.getAttribute("cart");
        int index = findIndex(items, itemName);
        items.get(index).updateQuantity(quantity);
    }


    //Delete item
    public static void deleteItem(HttpServletRequest request, String itemName){
        HttpSession session = request.getSession(true);
        ArrayList<ItemObject> items = (ArrayList<ItemObject>) session.getAttribute("cart");
        int index = findIndex(items, itemName);
        items.remove(index);
    }

    private static int findIndex(ArrayList<ItemObject> items, String itemName) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (itemName.equals(items.get(i).getMovieTitle())) {
                index = i;
                break;
            }
        }
        return index;
    }

    // Verify function to check whether the string is already in the arrayList
    private static boolean checkCart(ArrayList<ItemObject> items, String itemName){
        for(int i = 0; i < items.size(); i++){
            if(itemName.equals(items.get(i).getMovieTitle())){
                return false;
            }
        }
        return true;
    }

    public void printCart(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        ArrayList<ItemObject> items = (ArrayList<ItemObject>) session.getAttribute("cart");
        for(int i = 0; i < items.size(); i++){
            System.out.println(items.get(i).getMovieTitle());
        }
    }
}
