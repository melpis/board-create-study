package com.github.melpis.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DataBaseServer {

    private final static String INSERT_COMMAND = "insert";
    private final static String UPDATE_COMMAND = "update";
    private final static String DELETE_COMMAND = "delete";
    private final static String SELECT_COMMAND = "select";
    private DataBase dataBase;

    public DataBaseServer(DataBase dataBase){
        this.dataBase = new DataBase();
        dataBase.createTable("board");
    }

    public void startServer(){
        Thread thread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(10001)) {
                while (true) {
                    try (Socket clientSocket = serverSocket.accept();
                         InputStream is = clientSocket.getInputStream();
                         OutputStream os = clientSocket.getOutputStream()) {
                        MessageRequest messageRequest = new MessageRequest(is);
                        ResultResponse resultResponse = new ResultResponse(os);
                        this.service(messageRequest,resultResponse);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void service(MessageRequest messageRequest,
                        ResultResponse resultResponse) throws IOException {
        String returnMessage;
        if (INSERT_COMMAND.equalsIgnoreCase(messageRequest.getCommand())) {
            returnMessage = this.dataBase.save(messageRequest.getMessage());
        } else if (UPDATE_COMMAND.equalsIgnoreCase(messageRequest.getCommand())) {
            returnMessage = this.dataBase.update(messageRequest.getMessage());
        } else if (DELETE_COMMAND.equalsIgnoreCase(messageRequest.getCommand())) {
            returnMessage = this.dataBase.delete(messageRequest.getMessage());
        } else if (SELECT_COMMAND.equalsIgnoreCase(messageRequest.getCommand())) {
            returnMessage = this.dataBase.select(messageRequest.getMessage());
        } else {
            returnMessage = "error";
        }
        resultResponse.write(returnMessage);
    }
}
