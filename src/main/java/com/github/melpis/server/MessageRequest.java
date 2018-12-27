package com.github.melpis.server;

import java.io.IOException;
import java.io.InputStream;

public class MessageRequest {
    private InputStream is;
    private String command = null;
    private String message = null;

    public MessageRequest(InputStream is) throws IOException {
        this.is = is;
        this.parseData();
    }

    private void parseData() throws IOException {
        byte[] buffer = new byte[4096];
        is.read(buffer);
        String message = new String(buffer);
        this.command = message.substring(0, 7).trim().toLowerCase();
        this.message = message.substring(7).trim();

    }

    public String getCommand() {
        return this.command;
    }

    public String getMessage() {
        return this.message;
    }
}
