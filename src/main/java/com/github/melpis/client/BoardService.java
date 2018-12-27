package com.github.melpis.client;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class BoardService {
    private static final String DRIVER_NAME = "org.h2.Driver";
    private static final String DB_URI = "jdbc:h2:mem:myDb";


    public void viewList() {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("목록");
        System.out.println("번호_제목_내용_등록일_조회수");

        String sql = "select seq, title, content, register_date, read_count ";
        sql += "from board ";
        try (Connection connection = DriverManager.getConnection(DB_URI);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String seq = String.valueOf(resultSet.getInt("seq"));
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String registerDate = resultSet.getString("register_date");
                String readCount = String.valueOf(resultSet.getInt("read_count"));
                System.out.println("" + seq + "_" + title + "_" + content + "_" + registerDate + "_" + readCount + "");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
                Class.forName(DRIVER_NAME);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String sql = "insert into board(title, content, register_date, read_count) ";
            sql += "values(?,?,?,?)";
            try (Connection connection = DriverManager.getConnection(DB_URI);
                 PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                int count = 0;
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, content);
                preparedStatement.setString(3, date);
                preparedStatement.setInt(4, count);
                preparedStatement.executeUpdate();
                int seq = 0;
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if(generatedKeys.next()) {
                    seq = generatedKeys.getInt("seq");
                }
                Map<String, String> newBoard = new HashMap<>();
                newBoard.put("seq", String.valueOf(seq));
                newBoard.put("title", title);
                newBoard.put("content", content);
                newBoard.put("register_date", date);
                newBoard.put("read_count", String.valueOf(count));
                System.out.println("등록");
                this.printBoard(newBoard);

            } catch (SQLException e) {
                e.printStackTrace();
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
            Map<String, String> board = this.getBoard(seq);

            try {
                Class.forName(DRIVER_NAME);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String sql = "update board set title=?, content=?, register_date=? ";
            sql += "where seq=? ";
            try (Connection connection = DriverManager.getConnection(DB_URI);
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, content);
                preparedStatement.setString(3, date);
                preparedStatement.setInt(4, Integer.parseInt(seq));
                preparedStatement.executeUpdate();
                board.put("seq", seq);
                board.put("title", title);
                board.put("content", content);
                board.put("register_date", date);
                System.out.println("수정");
                this.printBoard(board);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private Map<String, String> getBoard(String seq) {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> board = new HashMap<>();
        String sql = "select seq, title, content, register_date, read_count ";
        sql += "from board ";
        sql += "where seq=? ";
        try (Connection connection = DriverManager.getConnection(DB_URI);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(seq));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                board.put("seq", String.valueOf(resultSet.getInt("seq")));
                board.put("title", resultSet.getString("title"));
                board.put("content", resultSet.getString("content"));
                board.put("register_date", resultSet.getString("register_date"));
                board.put("read_count", String.valueOf(resultSet.getInt("read_count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return board;
    }


    public void viewDetail(String[] aryUserInput) {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = this.getBoard(seq);
            this.incrementReadCountUpdate(seq, board);
            System.out.println("상세 조회");
            this.printBoard(board);
        }
    }

    private void incrementReadCountUpdate(String seq, Map<String, String> board) {
        this.incrementReadCount(board);
        String sql = "update board set read_count=? ";
        sql += "where seq=? ";
        try (Connection connection = DriverManager.getConnection(DB_URI);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(board.get("read_count")));
            preparedStatement.setInt(2, Integer.parseInt(seq));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void incrementReadCount(Map<String, String> board) {
        int readCount = Integer.parseInt(board.get("read_count")) + 1;
        board.put("read_count", String.valueOf(readCount));
    }

    public void viewEdit(String[] aryUserInput) {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = this.getBoard(seq);
            this.incrementReadCountUpdate(seq, board);
            System.out.println("수정 화면");
            this.printBoard(board);
        }
    }

    public void delete(String[] aryUserInput) {
        String seq = aryUserInput[1];
        if (seq != null && !"".equalsIgnoreCase(seq)) {
            Map<String, String> board = this.getBoard(seq);
            if (board != null) {
                String sql = "delete from board ";
                sql += "where seq=? ";
                try (Connection connection = DriverManager.getConnection(DB_URI);
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, Integer.parseInt(seq));
                    preparedStatement.executeUpdate();
                    System.out.println("삭제");
                    this.printBoard(board);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

