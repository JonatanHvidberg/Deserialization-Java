package rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Greeter extends Remote {
    String registryName = "demo";

    Response greet(Request msg) throws RemoteException;
}