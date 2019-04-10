package com.github.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.board.domain.Board;
import com.github.board.service.BoardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(BoardController.class)
public class BoardControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BoardService boardService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    public void getBoard() throws Exception {
        given(this.boardService.get(1L))
                .willReturn(new Board("subject", "content"));
        this.mvc.perform(get("/board/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'subject':'subject','content':'content'}"))
                .andDo(print());
    }

    @Test
    public void saveBoard() throws Exception {
        Board value = new Board("subject", "content");

        this.mvc.perform(post("/board/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(value)))
                .andDo(print()).andExpect(status().isOk());

        verify(this.boardService).save(value);
    }

    @Test
    public void deleteBoard() throws Exception {
        this.mvc.perform(delete("/board/1"))
                .andDo(print()).andExpect(status().isOk());
        verify(this.boardService).remove(1L);

    }


}