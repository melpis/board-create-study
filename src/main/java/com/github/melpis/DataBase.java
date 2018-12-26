package com.github.melpis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    public void createTable(String tableName) {
        this.getFile(tableName);
        this.getFile(tableName+"Seq");

    }

    private void getFile(String tableName) {
        File dataFile = new File(tableName);
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> save(String tableName, Map<String, String> data) throws IOException {
        String systemSeq = String.valueOf(this.getSequence(tableName));
        data.put("seq", systemSeq);
        File dataFile = new File(tableName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile, true))) {
            this.writeRowData(bw, systemSeq, data);
        }

        return data;
    }

    public void remove(String tableName, String seq) throws IOException {
        File dataFile = new File(tableName);
        File tempFile = File.createTempFile(tableName,".tmp");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
             BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                String[] rows = readLine.split(",");
                String[] columns = rows[0].split("\\|");
                if (!columns[1].equalsIgnoreCase(seq)) {
                    bw.write(readLine);
                    bw.newLine();
                    bw.flush();
                }
            }
            dataFile.delete();
            tempFile.renameTo(dataFile);
        }
    }

    public int getSequence(String tableName) throws IOException {
        int returnSeq = 0;
        File seqDataFile = new File(tableName + "Seq");
        try (FileInputStream fis = new FileInputStream(seqDataFile);
             FileOutputStream fos = new FileOutputStream(seqDataFile)) {
            returnSeq = fis.read();
            if (returnSeq < 0) {
                returnSeq = 0;
            }
            returnSeq = returnSeq + 1;
            fos.write(returnSeq);
            fos.flush();
        }
        return returnSeq;
    }

    public List<Map<String, String>> getList(String tableName) throws IOException {
        List<Map<String, String>> returnResult = new ArrayList<>();
        File dataFile = new File(tableName);
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String readLine;
            Map<String, String> result;

            while ((readLine = br.readLine()) != null) {
                String[] rows = readLine.split(",");
                result = new HashMap<>();
                this.setReturnData(result, rows);
                returnResult.add(result);
            }

        }
        return returnResult;
    }

    private void setReturnData(Map<String, String> result, String[] rows) {
        for (String resultColumns : rows) {
            String[] columns = resultColumns.split("\\|");
            result.put(columns[0], columns[1]);
        }
    }

    public Map<String, String> get(String tableName, String seq) throws IOException {
        Map<String, String> returnResult = null;
        File dataFile = new File(tableName);
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                String[] rows = readLine.split(",");
                String[] columns = rows[0].split("\\|");
                if ("seq".equalsIgnoreCase(columns[0]) && columns[1].equals(seq)) {
                    returnResult = new HashMap<>();
                    setReturnData(returnResult, rows);
                    break;
                }
            }

        }
        return returnResult;
    }

    public void update(String tableName, Map<String, String> data, String seq) throws IOException {
        File dataFile = new File(tableName);
        File tempFile = File.createTempFile(tableName,".tmp");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
             BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                String[] rows = readLine.split(",");
                String[] columns = rows[0].split("\\|");
                if (columns[1].equals(seq)) {
                    this.writeRowData(bw, seq, data);
                } else {
                    bw.write(readLine);
                    bw.newLine();
                    bw.flush();
                }

            }
            dataFile.delete();
            tempFile.renameTo(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void writeRowData(BufferedWriter bw, String seq, Map<String, String> data) throws IOException {
        bw.write("seq|" + seq + ",");
        bw.write("title|" + data.get("title") + ",");
        bw.write("content|" + data.get("content") + ",");
        bw.write("register_date|" + data.get("register_date") + ",");
        bw.write("read_count|" + data.get("read_count"));
        bw.newLine();
        bw.flush();
    }

}
