package commons;

import java.io.File;
import java.io.FileInputStream;

public class Example {
    public static void main(String[] args) {
      if (args.length == 1) {
            try {
                File file = new File(args[0]);
                byte[] bIn = readContentIntoByteArray(file);
                Object obj = Deserializer.deserialize(bIn);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Please provide a payload path");
        }
   }
 
    private static byte[] readContentIntoByteArray(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bFile = new byte[(int) file.length()];
    
        //convert file into array of bytes 
        fileInputStream.read(bFile);
        fileInputStream.close();
        return bFile;
    }
}