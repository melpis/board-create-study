package com.github.melpis;

import com.github.melpis.client.BoardLauncher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BoardLauncherTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Before
    public void setUpOutput() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1";

        try (Connection con = DriverManager.getConnection(url);
             Statement prepareStatement = con.createStatement()
        ) {
            String sql = "CREATE TABLE BOARD (" +
                    "  seq integer AUTO_INCREMENT PRIMARY KEY," +
                    "  title varchar(255), " +
                    "  content varchar(255), " +
                    "  register_date varchar(255), " +
                    "  read_count  bigint"+
                    ");";

            prepareStatement.execute(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
    @Test
    public void main() {

        //Given
        String data = "RF|\nRP|test|test\nLT| \nVW|1\nEF|1\nEP|1|test1|test1\nDT|1\nEND";
        provideInput(data);

        //when
        BoardLauncher boardLauncher = new BoardLauncher();
        boardLauncher.launcher();

        //then
        //시간을 동적으로 생성하기 때문에 분이 테스트 케이스에 실패 할수도 ..
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String expected = "사용예:\n" +
                "목록: LT|\n" +
                "상세 조회: VW|3\n" +
                "삭제: DT|3\n" +
                "수정 화면: EF|3\n" +
                "수정 처리: EP|3|제목|내용\n" +
                "등록 화면: RF|\n" +
                "등록 처리: RP|제목|내용\n" +
                "등록 화면\n" +
                "제목 : \n" +
                "내용 : \n" +
                "등록\n" +
                "번호: 1\n" +
                "제목: test\n" +
                "내용: test\n" +
                "등록일: " + date + "\n" +
                "조회수: 0\n" +
                "목록\n" +
                "번호_제목_내용_등록일_조회수\n" +
                "1_test_test_" + date + "_0\n" +
                "상세 조회\n" +
                "번호: 1\n" +
                "제목: test\n" +
                "내용: test\n" +
                "등록일: " + date + "\n" +
                "조회수: 1\n" +
                "수정 화면\n" +
                "번호: 1\n" +
                "제목: test\n" +
                "내용: test\n" +
                "등록일: " + date + "\n" +
                "조회수: 2\n" +
                "수정\n" +
                "번호: 1\n" +
                "제목: test1\n" +
                "내용: test1\n" +
                "등록일: " + date + "\n" +
                "조회수: 2\n" +
                "삭제\n" +
                "번호: 1\n" +
                "제목: test1\n" +
                "내용: test1\n" +
                "등록일: " + date + "\n" +
                "조회수: 2\n" +
                "시스템 종료\n";

        assertThat(getOutput(), is(expected));

    }
}