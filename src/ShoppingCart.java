import java.util.*;
import java.text.*;
import java.lang.*;

public class ShoppingCart {
    private int totalItems;
    private double totalPrice;
    ArrayList<ShoppingCartItem> shoppingCart;


    ShoppingCart() {
        shoppingCart = new ArrayList<ShoppingCartItem>();
    }

    void addACopy(ShoppingCartItem movie) {
        movie.incrementQuantity();
    }

    void removeACopy(ShoppingCartItem movie) {
        movie.decrementQuantity();
    }

    void changeNumOfCopies(ShoppingCartItem movie, int copies) {
        movie.updateQuantity(copies);
    }

    void removeMovieFromCart(ShoppingCartItem movie) {
        shoppingCart.remove(movie);
    }

    int returnTotalNumOfItems() {
        for (ShoppingCartItem movie : shoppingCart) {
            totalItems += movie.getQuantity();
        }
        return totalItems;
    }


    double getTotalCartPrice() {
        totalPrice = shoppingCart.size();
        return totalPrice;
    }


}


