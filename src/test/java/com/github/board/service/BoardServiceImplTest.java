package com.github.board.service;

import com.github.board.domain.Board;
import com.github.board.repository.BoardRepository;
import com.github.board.service.BoardService;
import com.github.board.service.BoardServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class BoardServiceImplTest {

    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.boardService = new BoardServiceImpl(this.boardRepository);
    }
    // mock test
    @Test
    public void save() {
        Board returnBoard = new Board("test", "content");
        given(this.boardRepository.save(any())).willReturn(returnBoard);

        Board board =  new Board("test", "content");
        this.boardService.save(board);

        assertThat(board.getSubject()).isEqualTo(returnBoard.getSubject());
    }

    @Test
    public void remove() {
        this.boardService.remove(1L);
        verify(this.boardRepository,times(1)).deleteById(1L);
    }

    @Test
    public void list() {
        Board returnBoard = new Board("test", "content");
        given(this.boardRepository.findAll()).willReturn(List.of(returnBoard));
        List<Board> list = this.boardService.list();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void get() {
        Board returnBoard = new Board("test", "content");
        given(this.boardRepository.findById(any())).willReturn(Optional.of(returnBoard));
        Board board = this.boardService.get(1L);
        assertThat(board.getReadCount()).isEqualTo(1);
    }
}