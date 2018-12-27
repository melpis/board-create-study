package com.github.melpis.server;

import java.io.IOException;
import java.io.OutputStream;

public class ResultResponse {

    private OutputStream os;

    public ResultResponse(OutputStream os) {
        this.os = os;
    }

    public void write(String data) throws IOException {
        this.write(data.getBytes());
    }

    public void write(byte[] data) throws IOException {
        this.os.write(data);
        this.os.flush();
    }

}
