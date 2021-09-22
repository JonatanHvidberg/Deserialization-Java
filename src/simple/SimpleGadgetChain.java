import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SimpleGadgetChain {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream oOut = new ObjectOutputStream(bOut);

        CacheManager gadgetChain = new CacheManager(new CommandTask("notify-send Hi!"));
        oOut.writeObject(gadgetChain);
        oOut.flush();
        byte[] bytes = bOut.toByteArray();

        oOut.close();
        bOut.close();

        ByteArrayInputStream bIn = new ByteArrayInputStream(bytes);
        ObjectInputStream oIn = new ObjectInputStream(bIn);
        User user = (User) oIn.readObject();
   } 
}

class CacheManager implements Serializable {
    private final Runnable initHook;
    public CacheManager(Runnable initHook) {
        this.initHook = initHook;
    }
    public void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        System.out.println("Test!");
        ois.defaultReadObject();
        initHook.run();
    }
}

class CommandTask implements Runnable, Serializable {
    private final String command;
    public CommandTask(String command) {
        this.command = command;
    }
    public void run() {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}