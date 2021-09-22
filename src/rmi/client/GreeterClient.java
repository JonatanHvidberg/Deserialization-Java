package rmi.client;

import rmi.common.Greeter;
import rmi.common.Request;
import rmi.common.Response;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GreeterClient {

    private GreeterClient() {}

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Greeter server = (Greeter) registry.lookup(Greeter.registryName);

            Response resp = server.greet(new Request("world"));
            System.out.println("response: " + resp.message);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
