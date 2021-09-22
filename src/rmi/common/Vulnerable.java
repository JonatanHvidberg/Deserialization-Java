package rmi.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Vulnerable is a demo class to demonstrate that RMI communication is
 * vulnerable to deserialization attacks.
 */
public class Vulnerable implements Serializable {

    private String message;

    public Vulnerable(String message) {
        this.message = message;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        System.out.println(this.message);
    }
}
