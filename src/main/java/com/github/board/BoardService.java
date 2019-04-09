package com.github.board;

import com.github.board.domain.Board;

import java.util.List;

public interface BoardService {
    void save(Board board);

    void update(Board board);

    void remove(Long id);

    List<Board> list();

    Board get(Long id);
}
