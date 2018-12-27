package com.github.melpis.server;

import java.io.*;

public class DataBase {

    private final String COLUMN_VALUE_SEPARATOR = "|";
    private final String DATA_SEPARATOR = ",";

    public void createTable(String tableName) {
        this.getFile(tableName);
        this.getFile(tableName + "Seq");
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

    public String save(String message) throws IOException {
        String systemMessage;
        String[] messageLines = message.split("\n");
        String[] columns = messageLines[0].split(",");
        String fromData = messageLines[1].substring(5);
        String[] valuesData = messageLines[2].substring(7).split(",");

        if (columns.length != valuesData.length) {
            systemMessage = "not match data";
            return systemMessage;
        }

        if ("".equalsIgnoreCase(fromData)) {
            systemMessage = "empty from";
            return systemMessage;
        }

        String systemSeq = String.valueOf(this.getSequence(fromData));
        File dataFile = new File(fromData);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile, true))) {
            bw.write("seq" + COLUMN_VALUE_SEPARATOR + systemSeq + DATA_SEPARATOR);
            for (int indexI = 0; indexI < valuesData.length; indexI++) {
                String str = columns[indexI].trim() + COLUMN_VALUE_SEPARATOR + valuesData[indexI].trim();
                if (indexI != valuesData.length - 1) {
                    str += DATA_SEPARATOR;
                }
                bw.write(str);
            }
            bw.newLine();
            bw.flush();
        }

        return systemSeq;
    }

    public String delete(String message) throws IOException {
        String systemMessage;
        String[] messageLines = message.split("\n");
        String fromData = messageLines[0].substring(5).trim();
        String whereData = messageLines[1].substring(6).replace("=", COLUMN_VALUE_SEPARATOR).trim();
        if ("".equalsIgnoreCase(fromData)) {
            systemMessage = "empty from";
            return systemMessage;
        }

        File dataFile = new File(fromData);
        File tempFile = File.createTempFile(fromData, ".tmp");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
             BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                boolean isMarkDeleteData = this.isMarkData(whereData, readLine);
                if (!isMarkDeleteData) {
                    bw.write(readLine);
                    bw.newLine();
                }
            }
            bw.flush();
            dataFile.delete();
            tempFile.renameTo(dataFile);
        }
        return "ok";
    }

    private boolean isMarkData(String whereData, String readLine) {
        String[] rows = readLine.split(DATA_SEPARATOR);
        boolean markDeleteData = false;
        for (String row : rows) {
            if (whereData.equalsIgnoreCase(row)) {
                markDeleteData = true;
                break;
            }
        }
        return markDeleteData;
    }

    private int getSequence(String tableName) throws IOException {
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

    public String select(String message) throws IOException {
        String[] messageLines = message.split("\n");
        String[] columns = messageLines[0].split(",");
        String fromData = messageLines[1].substring(5).trim();
        if (messageLines.length > 2) {
            String whereData = messageLines[2].substring(6).replace("=", COLUMN_VALUE_SEPARATOR).trim();
            return select(fromData, whereData);
        }


        StringBuilder stringBuilder = new StringBuilder();
        File dataFile = new File(fromData);
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                stringBuilder.append(readLine);
            }
        }
        return stringBuilder.toString();
    }

    private String select(String fromData, String whereData) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        File dataFile = new File(fromData);
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                boolean isMarkData = this.isMarkData(whereData, readLine);
                if (isMarkData) {
                    stringBuilder.append(readLine);
                    break;
                }

            }
        }
        return stringBuilder.toString();
    }


    public String update(String message) throws IOException {
        String systemMessage;
        String[] messageLines = message.split("\n");
        String[] columnsAndValues = messageLines[0].split(",");
        String fromData = messageLines[1].substring(5);
        String whereData = messageLines[2].substring(6).replace("=", COLUMN_VALUE_SEPARATOR).trim();

        File dataFile = new File(fromData);
        File tempFile = File.createTempFile(fromData, ".tmp");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
             BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                boolean isMarkData = this.isMarkData(whereData, readLine);
                String[] rows = readLine.split(DATA_SEPARATOR);
                if (isMarkData) {
                    for (int indexI = 0; indexI < rows.length; indexI++) {
                        String[] saveDataColumnAndValue = rows[indexI].split("\\"+ COLUMN_VALUE_SEPARATOR);
                        String value = saveDataColumnAndValue[1];
                        for (String columnAndValue : columnsAndValues) {
                            String[] split = columnAndValue.split("=");
                            if (saveDataColumnAndValue[0].equalsIgnoreCase(split[0])) {
                                value = split[1];
                                break;
                            }
                        }
                        String str = saveDataColumnAndValue[0].trim() + COLUMN_VALUE_SEPARATOR + value.trim();
                        if (indexI != rows.length - 1) {
                            str += DATA_SEPARATOR;
                        }
                        bw.write(str);
                    }
                } else {
                    bw.write(readLine);
                }
                bw.newLine();
                bw.flush();

            }
            dataFile.delete();
            tempFile.renameTo(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return "ok";
    }


}
