/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
import java.util.ArrayList;

public class User {

    private String username;
    //private double totalPrice;
    private ArrayList<ShoppingCartItem> cart;
    //private ShoppingCart cart;

    public User(String username) {
        this.username = username;
        cart = new ArrayList<ShoppingCartItem>();
    }

    public void addItemToShoppingCart(ShoppingCartItem movie) {
        cart.add(movie);
    }

    ArrayList<ShoppingCartItem> accessCart() { return cart; }

}