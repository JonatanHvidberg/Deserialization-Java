package rmi.common;

import java.io.Serializable;

public class Response implements Serializable {
    public String message;

    public Response(String message) {
        this.message = message;
    }
}
