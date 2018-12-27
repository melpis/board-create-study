package com.github.melpis.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;


public class DataBaseConnector {

    private Socket client = null;

    private InputStream serverDataRead = null;
    private OutputStream serverDataWrite = null;
    private String result = null;
    private Iterator<Map<String, String>> results = null;
    private Map<String, String> resultData = null;

    public void connection() throws IOException {
        this.client = new Socket("127.0.0.1", 10001);
        this.serverDataRead = this.client.getInputStream();
        this.serverDataWrite = this.client.getOutputStream();
    }

    public Iterator<Map<String, String>> executeQuery(String statement) throws IOException{
        this.serverDataWrite.write(statement.getBytes());
        this.serverDataWrite.flush();
        resultParse();
        return this.results;
    }

    private void resultParse() throws IOException{
        byte[] buffer = new byte[4096];
        int readCount;
        StringBuilder stringBuilder = new StringBuilder();
        while ((readCount = this.serverDataRead.read(buffer)) > 0) {
            stringBuilder.append(new String(buffer, 0, readCount));
        }

        String responseString = stringBuilder.toString();
        String[] resultRows = responseString.split("\n");
        List<Map<String, String>> results = new ArrayList<>();
        for (String resultRow : resultRows) {
            String[] resultColumns = resultRow.split(",");
            if (resultColumns.length <= 1) {
                this.result = responseString;
            } else {
                Map<String, String> resultData = new HashMap<>();
                for (String resultColumn : resultColumns) {
                    String[] resultColumnAndValue = resultColumn.split("\\|");
                    resultData.put(resultColumnAndValue[0], resultColumnAndValue[1]);
                }
                results.add(resultData);
            }
        }

        this.results = results.iterator();
    }

    public void execute(String statement) throws IOException {
        this.serverDataWrite.write(statement.getBytes());
        this.serverDataWrite.flush();
        resultParse();
    }

    public String getResult() {
        return this.result;
    }

    public boolean hasNext() {
        if (this.results.hasNext()) {
            this.resultData = this.results.next();
            return true;
        }
        return false;
    }

    public String getResult(String column) {
        return this.resultData.get(column);
    }


    public void close() {
        try {
            this.serverDataRead.close();
            this.serverDataWrite.close();
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
