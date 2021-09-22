package rmi.server;

import rmi.common.LocalTime;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class TimeServer implements LocalTime {

    public TimeServer() {}

    @Override
    public LocalDateTime getTime() throws RemoteException {
        return LocalDateTime.now();
    }

    public static void main(String[] args) {
        try {
            TimeServer s = new TimeServer();
            LocalTime stub = (LocalTime) UnicastRemoteObject.exportObject(s, 1);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(LocalTime.registryName, stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}