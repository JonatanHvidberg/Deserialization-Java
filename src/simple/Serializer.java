package simple;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class Serializer {
    public static String serialize(Object obj) {
        byte [] userBytes = {};
        try {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ObjectOutput oOut = new ObjectOutputStream(bOut);
            oOut.writeObject(obj);
            oOut.flush();
            userBytes = bOut.toByteArray();
            oOut.close();
            bOut.close();
        } catch(Exception e) {
            System.out.println(e);
        }
        return new String(Base64.getEncoder().encode(userBytes));
    }
}
