package com.github.melpis.client;

import java.util.Scanner;


public class BoardLauncher {
    public void launcher() {
        // 사용예제
        printHelpMessage();
        BoardService boardService = new BoardService();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String userInput = scanner.nextLine();
                String[] aryUserInput = userInput.split("\\|");
                String request = aryUserInput[0];
                if ("LT".equalsIgnoreCase(request)) { // 목록 조회 기능
                    boardService.viewList();
                } else if ("RF".equalsIgnoreCase(request)) { // 등록 화면 기능
                    boardService.registerPrint();
                } else if ("RP".equalsIgnoreCase(request)) { // 등록 기능
                    boardService.register(aryUserInput);
                } else if ("VW".equalsIgnoreCase(request)) { // 상세 조회 기능
                    boardService.viewDetail(aryUserInput);
                } else if ("EF".equalsIgnoreCase(request)) { // 수정 화면 기능
                    boardService.viewEdit(aryUserInput);
                } else if ("EP".equalsIgnoreCase(request)) { // 수정 기능
                    boardService.edit(aryUserInput);
                } else if ("DT".equalsIgnoreCase(request)) { // 삭제 기능
                    boardService.delete(aryUserInput);
                } else {
                    System.out.println("시스템 종료");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printHelpMessage() {
        System.out.println("사용예:");
        System.out.println("목록: LT|");
        System.out.println("상세 조회: VW|3");
        System.out.println("삭제: DT|3");
        System.out.println("수정 화면: EF|3");
        System.out.println("수정 처리: EP|3|제목|내용");
        System.out.println("등록 화면: RF|");
        System.out.println("등록 처리: RP|제목|내용");
    }


}
