package com.github.board;

import com.github.board.domain.Board;
import com.github.board.repository.BoardRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


public class BoardServiceImplTest {
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.boardService = new BoardServiceImpl(this.boardRepository);
    }

    @Test
    public void save() {
        Board returnBoard = new Board();
        returnBoard.setSubject("test");
        given(this.boardRepository.save(any())).willReturn(returnBoard);

        Board board = new Board();
        board.setSubject("test");
        this.boardService.save(board);

        assertThat(board.getSubject()).isEqualTo(returnBoard.getSubject());
    }

    @Test
    public void update() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void list() {
    }

    @Test
    public void get() {
    }
}