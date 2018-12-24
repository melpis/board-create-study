package com.github.melpis;

import java.text.SimpleDateFormat;
import java.util.*;


public class BoardLauncher {

    public void launcher() {
        // 사용예제
        printExample();
        Map<String, List<Map<String, String>>> db = prepareData();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String userInput = scanner.nextLine();
                String[] aryUserInput = userInput.split("\\|");
                String request = aryUserInput[0];
                if (request.equals("LT")) { // 목록 조회 기능
                    viewList(db);
                } else if (request.equals("RF")) { // 등록 화면 기능
                    registerPrint();
                } else if (request.equals("RP")) { // 등록 기능
                    register(db, aryUserInput);
                } else if (request.equals("VW")) { // 상세 조회 기능
                    viewDetail(db, aryUserInput[1]);
                } else if (request.equals("EF")) { // 수정 화면 기능
                    viewEdit(db, aryUserInput[1]);
                } else if (request.equals("EP")) { // 수정 기능
                    edit(db,  aryUserInput);
                } else if (request.equals("DT")) { // 삭제 기능
                    delete(db, aryUserInput[1]);
                } else {
                    System.out.println("시스템 종료");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<Map<String, String>>> prepareData() {
        Map<String, List<Map<String, String>>> db = new HashMap<>();
        List<Map<String, String>> dbBoardList = new ArrayList<>();
        db.put("board", dbBoardList);
        return db;
    }

    private void printExample() {
        System.out.println("사용예:");
        System.out.println("목록: LT|");
        System.out.println("상세 조회: VW|3");
        System.out.println("삭제: DT|3");
        System.out.println("수정 화면: EF|3");
        System.out.println("수정 처리: EP|3|제목|내용");
        System.out.println("등록 화면: RF|");
        System.out.println("등록 처리: RP|제목|내용");
    }

    public void viewList(Map<String, List<Map<String, String>>> db) {
        List<Map<String, String>> boardList = db.get("board");
        // 목록 조회 기능
        if (boardList != null) {
            System.out.println("목록");
            System.out.println("번호_제목_내용_등록일_조회수");
            for (Map<String, String> board : boardList) {
                String seq = board.get("seq");
                String title = board.get("title");
                String content = board.get("content");
                String registerDate = board.get("register_Date");
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

    public void register(Map<String, List<Map<String, String>>> db, String[] aryUserInput) {
        String title = aryUserInput[1];
        String content = aryUserInput[2];
        if (title != null && content != null && !"".equalsIgnoreCase(title) && !"".equalsIgnoreCase(content)) {
            // sequence 증가 로직
            List<Map<String, String>> boardList = db.get("board");
            int max = 0;
            if (boardList != null) {
                for (Map<String, String> board : boardList) {
                    max = Integer.parseInt(board.get("seq")) < max ? max : Integer.parseInt(board.get("seq"));
                }
                max += 1;
            }

            Map<String, String> newBoard = new HashMap<>();
            newBoard.put("seq", String.valueOf(max));
            newBoard.put("title", title);
            newBoard.put("content", content);
            newBoard.put("register_Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            newBoard.put("read_count", "0");

            boardList.add(newBoard);
            System.out.println("등록");
            printBoard(newBoard);
        }
    }



    private void printBoard(Map<String, String> board) {
        System.out.println("번호: " + board.get("seq"));
        System.out.println("제목: " + board.get("title"));
        System.out.println("내용: " + board.get("content"));
        System.out.println("등록일: " + board.get("register_Date"));
        System.out.println("조회수: " + board.get("read_count"));
    }

    public void edit(Map<String, List<Map<String, String>>> db, String[] aryUserInput) {
        String seq = aryUserInput[1];
        String title = aryUserInput[2];
        String content = aryUserInput[3];
        if (seq != null && title != null && content != null && !"".equalsIgnoreCase(seq) && !"".equalsIgnoreCase(title) && !"".equalsIgnoreCase(content)) {
            Map<String, String> board = getBoard(db, seq);
            board.put("seq", seq);
            board.put("title", title);
            board.put("content", content);
            board.put("register_Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            System.out.println("수정");
            printBoard(board);
        }
    }

    private Map<String, String> getBoard(Map<String, List<Map<String, String>>> db, String seq) {
        List<Map<String, String>> boardList = db.get("board");
        Map<String, String> board = null;
        if (boardList != null) {
            for (Map<String, String> temp : boardList) {
                String boardSeq = temp.get("seq");
                if (seq.equalsIgnoreCase(boardSeq)) {
                    board = temp;
                    break;
                }
            }
        }
        return board;
    }

    public void viewDetail(Map<String, List<Map<String, String>>> db, String seq) {
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = getBoard(db, seq);
            incrementReadCount(board);
            System.out.println("상세 조회");
            printBoard(board);

        }
    }

    private void incrementReadCount(Map<String, String> board) {
        int readCount = Integer.parseInt(board.get("read_count")) + 1;
        board.put("read_count", String.valueOf(readCount));
    }

    public void viewEdit(Map<String, List<Map<String, String>>> db, String seq) {
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = getBoard(db, seq);
            incrementReadCount(board);
            System.out.println("수정 화면");
            printBoard(board);
        }
    }

    public void delete(Map<String, List<Map<String, String>>> db, String seq1) {
        String seq = seq1;
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            List<Map<String, String>> boardList = db.get("board");
            Map<String, String> board = null;
            if (boardList != null) {
                board = getBoard(db, seq);
                boardList.remove(board);
            }

            System.out.println("삭제");
            printBoard(board);
        }
    }






}
