public class ItemObject {
    private String movieTitle;
    private int movieId;
    private int quantity;
    private int price;

    public ItemObject(String movieTitle){
        this.movieTitle = movieTitle;
        this.quantity = 1;
        this.price = 10;
    }

    public String getMovieTitle(){
        return movieTitle;
    }

    public void updateQuantity(int value){
        quantity = value;
    }

    public int getQuantity(){
        return quantity;
    }
}