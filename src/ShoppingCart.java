import java.util.*;
import java.text.*;
import java.lang.*;

public class ShoppingCart {
    private int totalItems;
    private double totalPrice;
    public Map<String, Integer> shoppingCart;
    public Map<String, Double> moviePrices;

//
//    ShoppingCart() {
//        shoppingCart = new HashMap<String, Integer>();
//        moviePrices = new HashMap<String, Integer>();
//        totalItems = 0;
//        totalPrice = 0;
//    }

    void addToCart(String movie) {
        // Check to see if item exists within the map
        if(shoppingCart.containsKey(movie)) {
            shoppingCart.put(movie, shoppingCart.get(movie) + 1);
            totalItems++;
        }
        else {
            shoppingCart.put(movie, 1);
            totalItems++;
            setMoviePrice(movie);
        }
    }

    void removeMovieFromCart(String movie) {
        if(shoppingCart.containsKey(movie)) {
            totalItems -= shoppingCart.get(movie);
            shoppingCart.remove(movie);
            moviePrices.remove(movie);
        }
    }


    void removeOneCopy(String movie) {
        if(shoppingCart.containsKey(movie)) {
            if(shoppingCart.get(movie) == 1) {
                removeMovieFromCart(movie);
                totalItems--;
            }
            else {
                shoppingCart.put(movie, shoppingCart.get(movie) - 1);
                totalItems--;
            }
        }
    }

    void addOneCopy(String movie) {
            shoppingCart.put(movie, shoppingCart.get(movie) + 1);
            totalItems++;
    }

    int returnTotalNumOfItems() {
        return totalItems;
    }

    int getNumOfCopies(String movie) {
        return shoppingCart.get(movie);
    }

    void setMoviePrice(String movie) {
        Random rand = new Random();
        double price = Math.round(rand.nextDouble() * 5) + 5;
        moviePrices.put(movie, price);
    }

    double getTotalPrice() {
        for (String movieTitle : shoppingCart.keySet()) {
            totalPrice += shoppingCart.get(movieTitle) * moviePrices.get(movieTitle);
        }

        return totalPrice;
    }


}


