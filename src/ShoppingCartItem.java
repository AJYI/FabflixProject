/*
This class represents the objects that's within the arrayList object of shopping cart
 */

public class ShoppingCartItem {
    private String title;
    private int quantity;
    private double price;

    ShoppingCartItem(String title, int quantity){
        this.title = title;
        this.quantity = quantity;
        price = 1.0;
    }

    void incrementQuantity(){
        quantity ++;
    }

    void decrementQuantity() { quantity -- ;}

    void updateQuantity(int quantity){
        this.quantity = quantity;
    }

    int getQuantity() { return quantity; }

    String getTitle() { return title; }

}
