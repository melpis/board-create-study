package com.github.board;

import com.github.board.domain.Board;
import com.github.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;


    @Override
    public void save(Board board) {
        this.boardRepository.save(board);
    }

    @Override
    public void update(Board board) {
        board.increaseReadCount();
        this.boardRepository.save(board);
    }

    @Override
    public void remove(Long id) {
        this.boardRepository.deleteById(id);
    }

    @Override
    public List<Board> list() {
        return this.boardRepository.findAll();
    }

    @Override
    public Board get(Long id) {
        Board board = this.boardRepository.findById(id).get();
        board.increaseReadCount();
        return board;
    }
}
