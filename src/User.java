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
        cart = new ArrayList<ShoppingCartItem>;

        //cart = new ShoppingCart();
        //totalPrice = 0;
    }

    public void addItemToShoppingCart()



    // ShoppingCart showCart() {
    //     return cart;
    // }

//    int getMovieQuantity(String movie) {
//        return cart.getNumOfCopies(movie);
//    }
//
//    double getTotalMoviePrice(String movie) {
//        return cart.getMoviePriceSum(movie);
//    }
//
//    double getTotal() {
//        totalPrice = cart.getTotalCartPrice();
//        return totalPrice;
//    }


}