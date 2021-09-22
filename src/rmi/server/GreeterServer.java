package rmi.server;

import rmi.common.Greeter;
import rmi.common.Request;
import rmi.common.Response;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class GreeterServer implements Greeter {

    public GreeterServer() {}

    @Override
    public Response greet(Request req) throws RemoteException {
        return new Response("Hello " + req.message + "!");
    }

    public static void main(String[] args) {
        try {
            GreeterServer s = new GreeterServer();
            Greeter stub = (Greeter) UnicastRemoteObject.exportObject(s, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(Greeter.registryName, stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}