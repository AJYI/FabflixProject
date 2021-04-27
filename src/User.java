/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String customerID;
    //private double totalPrice;
    private ArrayList<ShoppingCartItem> cart;
    //private ShoppingCart cart;

    public User(String firstName, String lastName, String customerID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerID = customerID;
        cart = new ArrayList<ShoppingCartItem>();
    }

    // For debugging purposes
    public void print(){
        System.out.println("firstName: " + firstName + "LastName: " + lastName + "customerID: " + customerID);
    }



//    public void addItemToShoppingCart()



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