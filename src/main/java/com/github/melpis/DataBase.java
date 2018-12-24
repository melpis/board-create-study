package com.github.melpis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {

    private Map<String, List<Map<String, String>>> db;
    private Map<String, Map<String, Integer>> index = new HashMap<>();

    public DataBase() {
        db = new HashMap<>();
    }

    public void createTable(String tableName) {
        List<Map<String, String>> tableData = new ArrayList<>();
        db.put(tableName, tableData);
        Map<String, Integer> tableIndex = new HashMap<>();
        index.put(tableName, tableIndex);

    }

    public void add(String tableName, Map<String, String> data) {
        this.db.get(tableName).add(data);
        this.index.get(tableName).put(data.get("seq"), this.db.get(tableName).size() - 1);
    }

    public void remove(String tableName, String seq) {
        List<Map<String, String>> boardList = this.db.get(tableName);
        int indexNum = this.index.get(tableName).get(seq);
        boardList.remove(indexNum);
    }

    public int getSequence(String tableName) {
        int size = this.db.get(tableName).size();
        return size + 1;
    }


    public List<Map<String, String>> getList(String tableName) {
        return this.db.get(tableName);
    }

    public Map<String, String> get(String tableName, String seq) {
        return this.db.get(tableName).get(Integer.parseInt(seq) - 1);
    }

    public void set(String tableName, Map<String, String> data, String seq) {
        List<Map<String, String>> boardList = this.db.get(tableName);
        int indexNum = this.index.get(tableName).get(seq);
        boardList.set(indexNum, data);

    }


}
