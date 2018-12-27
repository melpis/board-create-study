package com.github.melpis.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class BoardService {
    private DataBaseConnector dataBaseConnector;

    BoardService(DataBaseConnector dataBaseConnector) {
        this.dataBaseConnector = dataBaseConnector;
    }

    public void viewList() {
        try {
            this.dataBaseConnector.connection();
            String statement = "select seq, title, content, register_date, read_count\n" +
                    "from board";
            this.dataBaseConnector.executeQuery(statement);
            System.out.println("목록");
            System.out.println("번호_제목_내용_등록일_조회수");
            while (this.dataBaseConnector.hasNext()) {
                String seq = this.dataBaseConnector.getResult("seq");
                String title = this.dataBaseConnector.getResult("title");
                String content = this.dataBaseConnector.getResult("content");
                String registerDate = this.dataBaseConnector.getResult("register_date");
                String readCount = this.dataBaseConnector.getResult("read_count");
                System.out.println("" + seq + "_" + title + "_" + content + "_" + registerDate + "_" + readCount + "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.dataBaseConnector.close();
        }
    }

    public void registerPrint() {
        System.out.println("등록 화면");
        System.out.println("제목 : ");
        System.out.println("내용 : ");
    }


    public void register(String[] aryUserInput) {
        String title = aryUserInput[1];
        String content = aryUserInput[2];

        if (title != null && content != null && !"".equalsIgnoreCase(title) && !"".equalsIgnoreCase(content)) {
            try {
                this.dataBaseConnector.connection();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String statement = "insert title, content, register_date, read_count\n" +
                        "from board\n" +
                        "values " + title + "," + content + "," + date + "," + "0";
                this.dataBaseConnector.execute(statement);
                Map<String, String> newBoard = new HashMap<>();
                newBoard.put("seq", this.dataBaseConnector.getResult());
                newBoard.put("title", title);
                newBoard.put("content", content);
                newBoard.put("register_date", date);
                newBoard.put("read_count", "0");
                System.out.println("등록");
                this.printBoard(newBoard);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.dataBaseConnector.close();
            }

        }
    }

    private void printBoard(Map<String, String> board) {
        System.out.println("번호: " + board.get("seq"));
        System.out.println("제목: " + board.get("title"));
        System.out.println("내용: " + board.get("content"));
        System.out.println("등록일: " + board.get("register_date"));
        System.out.println("조회수: " + board.get("read_count"));
    }


    public void edit(String[] aryUserInput) {
        String seq = aryUserInput[1];
        String title = aryUserInput[2];
        String content = aryUserInput[3];
        if (seq != null && title != null && content != null && !"".equalsIgnoreCase(seq) && !"".equalsIgnoreCase(title) && !"".equalsIgnoreCase(content)) {
            Iterator<Map<String, String>> resultSet = this.getBoard(seq);
            String statement;
            if (resultSet.hasNext()) {
                Map<String, String> board = resultSet.next();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                try {
                    this.dataBaseConnector.connection();
                    statement = "update title=" + title + "," + "content="
                            + content + "," + "register_date=" + date + " \n" +
                            "from board\n" +
                            "where seq=" + seq;
                    this.dataBaseConnector.execute(statement);
                    board.put("seq", seq);
                    board.put("title", title);
                    board.put("content", content);
                    board.put("register_date", date);
                    System.out.println("수정");
                    this.printBoard(board);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    this.dataBaseConnector.close();
                }
            }
        }
    }

    private Iterator<Map<String, String>> getBoard(String seq) {
        Iterator<Map<String, String>> resultSet = null;
        try {
            this.dataBaseConnector.connection();
            String statement = "select seq, title, content, register_date, read_count\n" +
                    "from board\n" +
                    "where seq=" + seq;
            resultSet = this.dataBaseConnector.executeQuery(statement);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.dataBaseConnector.close();
        }
        return resultSet;
    }


    public void viewDetail(String[] aryUserInput) {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Iterator<Map<String, String>> resultSet = this.getBoard(seq);
            if (resultSet.hasNext()) {
                Map<String, String> board = resultSet.next();
                this.incrementReadCountUpdate(seq, board);
                System.out.println("상세 조회");
                this.printBoard(board);
            }
        }
    }

    private void incrementReadCountUpdate(String seq, Map<String, String> board) {
        try {
            this.incrementReadCount(board);
            this.dataBaseConnector.connection();
            String statement = "update read_count=" + board.get("read_count") + "\n" +
                    "from board\n" +
                    "where seq=" + seq;
            this.dataBaseConnector.execute(statement);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.dataBaseConnector.close();
        }
    }

    private void incrementReadCount(Map<String, String> board) {
        int readCount = Integer.parseInt(board.get("read_count")) + 1;
        board.put("read_count", String.valueOf(readCount));
    }

    public void viewEdit(String[] aryUserInput) {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Iterator<Map<String, String>> resultSet = this.getBoard(seq);
            if (resultSet.hasNext()) {
                Map<String, String> board = resultSet.next();
                this.incrementReadCountUpdate(seq, board);
                System.out.println("수정 화면");
                this.printBoard(board);
            }
        }
    }

    public void delete(String[] aryUserInput) {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = null;
            try {
                this.dataBaseConnector.connection();
                String statement = "select seq, title, content, register_date, read_count\n" +
                        "from board\n" +
                        "where seq=" + seq;
                Iterator<Map<String, String>> resultSet = this.dataBaseConnector.executeQuery(statement);
                if (resultSet.hasNext()) {
                    board = resultSet.next();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.dataBaseConnector.close();
            }

            if (board != null) {
                try {
                    this.dataBaseConnector.connection();
                    String statement = "delete \n" +
                            "from board \n" +
                            "where seq=" + seq;
                    this.dataBaseConnector.execute(statement);
                    System.out.println("삭제");
                    this.printBoard(board);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    this.dataBaseConnector.close();
                }
            }
        }
    }
}

