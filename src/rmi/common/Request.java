package rmi.common;

import java.io.Serializable;

public class Request implements Serializable {
    public String message;

    public Request(String message) {
        this.message = message;
    }
}
