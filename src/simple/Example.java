package simple;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Scanner;

public class Example {
    public static User deserialize(String cookie) throws Exception {
        byte[] userBytes = Base64.getDecoder().decode(cookie);
        ByteArrayInputStream bIn = new ByteArrayInputStream(userBytes);
        ObjectInputStream oIn = new ObjectInputStream(bIn);
        User user = (User) oIn.readObject();

        return user;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println("Provided cookie: " + args[0]);
            try {
                User user = deserialize(args[0]);
                System.out.println("Welcome back " + user.getUsername());
                if (user.getRole().equals("admin")) {
                    System.out.println("You are an admin, behave accordingly!");
                }
            } catch (Exception e) {
                System.out.println("Invalid session cookie.");
            }
        } else {
            System.out.println("Hello, please sign up.");
            Scanner scanner = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Enter username");

            String userName = scanner.nextLine();  // Read user input
            scanner.close();

            User user = new User(userName);
            String cookie = Serializer.serialize(user);
            
            System.out.println("Hello, " + userName);
            System.out.println("Your session cookie is: " + cookie);
        }
    }
}
