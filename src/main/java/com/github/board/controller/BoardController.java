package com.github.board.controller;

import com.github.board.domain.Board;
import com.github.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board/{id}")
    public Board getBoard(@PathVariable long id){
        return boardService.get(id);
    }

    @PostMapping("/board")
    public ResponseEntity saveBoard(@RequestBody Board board){
        this.boardService.save(board);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable long id){
        this.boardService.remove(id);
    }
}
