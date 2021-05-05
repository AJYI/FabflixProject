/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
import java.util.ArrayList;

public class SessionUser {
    private String firstName;
    private String lastName;
    private String customerID;

    public SessionUser(String firstName, String lastName, String customerID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerID = customerID;
    }

    // For debugging purposes
    public void print(){
        System.out.println("firstName: " + firstName + " LastName: " + lastName + " customerID: " + customerID);
    }

}