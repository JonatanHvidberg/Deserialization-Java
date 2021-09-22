package rmi.client;

import rmi.common.LocalTime;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeClient {

    private TimeClient() {}

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE dd MMM, HH:mm:ss");

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            LocalTime server = (LocalTime) registry.lookup(LocalTime.registryName);

            LocalDateTime time = server.getTime();
            System.out.println("The local server time is " + time.format(format));
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
