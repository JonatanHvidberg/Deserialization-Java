package rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface LocalTime extends Remote {
    String registryName = "time";

    LocalDateTime getTime() throws RemoteException;
}