package com.github.melpis;

import java.text.SimpleDateFormat;
import java.util.*;


public class BoardLauncher {

    public void launcher() {
        // 사용예제
        System.out.println("사용예:");
        System.out.println("목록: LT|");
        System.out.println("상세 조회: VW|3");
        System.out.println("삭제: DT|3");
        System.out.println("수정 화면: EF|3");
        System.out.println("수정 처리: EP|3|제목|내용");
        System.out.println("등록 화면: RF|");
        System.out.println("등록 처리: RP|제목|내용");

        Map<String, List<Map<String, String>>> db = new HashMap<>();
        List<Map<String, String>> dbBoardList = new ArrayList<>();
        db.put("board", dbBoardList);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String calendar = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String userInput = scanner.nextLine();
                String[] aryUserInput = userInput.split("\\|");
                String request = aryUserInput[0];

                if (request.equals("LT")) { // 목록 조회 기능
                    List<Map<String, String>> boardList = db.get("board");
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
                    // 목록 조회 기능
                } else if (request.equals("RF")) { // 등록 화면 기능
                    System.out.println("등록 화면");
                    System.out.println("제목 : ");
                    System.out.println("내용 : ");
                } else if (request.equals("RP")) { // 등록 기능
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
                        newBoard.put("register_Date", calendar);
                        newBoard.put("read_count", "0");

                        boardList.add(newBoard);
                        System.out.println("등록");
                        System.out.println("번호: " + newBoard.get("seq"));
                        System.out.println("제목: " + newBoard.get("title"));
                        System.out.println("내용: " + newBoard.get("content"));
                        System.out.println("등록일: " + newBoard.get("register_Date"));
                        System.out.println("조회수: " + newBoard.get("read_count"));
                    }
                } else if (request.equals("VW")) { // 상세 조회 기능
                    String seq = aryUserInput[1];
                    if (seq != null && !"".equalsIgnoreCase(seq)) {
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

                        //조회수 증가
                        Integer readCount = Integer.parseInt(board.get("read_count")) + 1;
                        board.put("read_count", readCount.toString());

                        System.out.println("상세 조회");
                        System.out.println("번호: " + board.get("seq"));
                        System.out.println("제목: " + board.get("title"));
                        System.out.println("내용: " + board.get("content"));
                        System.out.println("등록일: " + board.get("register_Date"));
                        System.out.println("조회수: " + board.get("read_count"));

                    }
                } else if (request.equals("EF")) { // 수정 화면 기능
                    String seq = aryUserInput[1];
                    if (seq != null && !"".equalsIgnoreCase(seq)) {
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
                            //조회수 증가
                            Integer readCount = Integer.parseInt(board.get("read_count")) + 1;
                            board.put("read_count", readCount.toString());

                            System.out.println("수정 화면");
                            System.out.println("번호: " + board.get("seq"));
                            System.out.println("제목: " + board.get("title"));
                            System.out.println("내용: " + board.get("content"));
                            System.out.println("등록일: " + board.get("register_Date"));
                            System.out.println("조회수: " + board.get("read_count"));
                        }
                    }
                } else if (request.equals("EP")) { // 수정 기능
                    String seq = aryUserInput[1];
                    String title = aryUserInput[2];
                    String content = aryUserInput[3];
                    if (seq != null && title != null && content != null && !"".equalsIgnoreCase(seq) && !"".equalsIgnoreCase(title) && !"".equalsIgnoreCase(content)) {
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

                        board.put("seq", seq);
                        board.put("title", title);
                        board.put("content", content);
                        board.put("register_Date", calendar);

                        System.out.println("수정");
                        System.out.println("번호: " + board.get("seq"));
                        System.out.println("제목: " + board.get("title"));
                        System.out.println("내용: " + board.get("content"));
                        System.out.println("등록일: " + board.get("register_Date"));
                        System.out.println("조회수: " + board.get("read_count"));
                    }
                } else if (request.equals("DT")) { // 삭제 기능
                    String seq = aryUserInput[1];
                    if (seq != null && !"".equalsIgnoreCase(seq)) {
                        List<Map<String, String>> boardList = db.get("board");
                        Map<String, String> board = null;
                        if (boardList != null) {
                            for (int indexI = 0; indexI < boardList.size(); indexI++) {
                                Map<String, String> temp = boardList.get(indexI);
                                String boardSeq = temp.get("seq");
                                if (seq.equalsIgnoreCase(boardSeq)) {
                                    board = temp;
                                    boardList.remove(indexI);
                                    break;
                                }
                            }
                        }

                        System.out.println("삭제");
                        System.out.println("번호: " + board.get("seq"));
                        System.out.println("제목: " + board.get("title"));
                        System.out.println("내용: " + board.get("content"));
                        System.out.println("등록일: " + board.get("register_Date"));
                        System.out.println("조회수: " + board.get("read_count"));
                    }
                } else {
                    System.out.println("시스템 종료");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
