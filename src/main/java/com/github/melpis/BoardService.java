package com.github.melpis;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BoardService {
    private DataBase dataBase;

    BoardService(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public void viewList() throws IOException {
        List<Map<String, String>> boardList = this.dataBase.getList("board");
        // 목록 조회 기능
        if (boardList != null) {
            System.out.println("목록");
            System.out.println("번호_제목_내용_등록일_조회수");
            for (Map<String, String> board : boardList) {
                String seq = board.get("seq");
                String title = board.get("title");
                String content = board.get("content");
                String registerDate = board.get("register_date");
                String readCount = board.get("read_count");
                System.out.println("" + seq + "_" + title + "_" + content + "_" + registerDate + "_" + readCount + "");
            }
        }
    }

    public void registerPrint() {
        System.out.println("등록 화면");
        System.out.println("제목 : ");
        System.out.println("내용 : ");
    }

    public void register(String[] aryUserInput) throws IOException {
        String title = aryUserInput[1];
        String content = aryUserInput[2];
        if (title != null && content != null && !"".equalsIgnoreCase(title) && !"".equalsIgnoreCase(content)) {
            Map<String, String> newBoard = new HashMap<>();
            newBoard.put("title", title);
            newBoard.put("content", content);
            newBoard.put("register_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            newBoard.put("read_count", "0");

            this.dataBase.save("board", newBoard);
            System.out.println("등록");
            printBoard(newBoard);
        }
    }


    private void printBoard(Map<String, String> board) {
        System.out.println("번호: " + board.get("seq"));
        System.out.println("제목: " + board.get("title"));
        System.out.println("내용: " + board.get("content"));
        System.out.println("등록일: " + board.get("register_date"));
        System.out.println("조회수: " + board.get("read_count"));
    }

    public void edit(String[] aryUserInput) throws IOException {
        String seq = aryUserInput[1];
        String title = aryUserInput[2];
        String content = aryUserInput[3];
        if (seq != null && title != null && content != null && !"".equalsIgnoreCase(seq) && !"".equalsIgnoreCase(title) && !"".equalsIgnoreCase(content)) {
            Map<String, String> board = this.dataBase.get("board", seq);
            board.put("seq", seq);
            board.put("title", title);
            board.put("content", content);
            board.put("register_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            this.dataBase.update("board", board, seq);
            System.out.println("수정");
            printBoard(board);
        }
    }

    public void viewDetail(String[] aryUserInput) throws IOException {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = this.dataBase.get("board", seq);
            incrementReadCount(board);
            System.out.println("상세 조회");
            this.dataBase.update("board", board, seq);
            printBoard(board);

        }
    }

    private void incrementReadCount(Map<String, String> board) {
        int readCount = Integer.parseInt(board.get("read_count")) + 1;
        board.put("read_count", String.valueOf(readCount));
    }

    public void viewEdit(String[] aryUserInput) throws IOException {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = this.dataBase.get("board", seq);
            incrementReadCount(board);
            System.out.println("수정 화면");
            this.dataBase.update("board", board, seq);
            printBoard(board);
        }
    }

    public void delete(String[] aryUserInput) throws IOException {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = this.dataBase.get("board", seq);
            this.dataBase.remove("board", board.get("seq"));
            System.out.println("삭제");
            printBoard(board);
        }
    }
}
