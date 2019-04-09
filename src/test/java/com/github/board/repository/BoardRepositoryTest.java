package com.github.board.repository;

import com.github.board.domain.Board;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void findBoard(){
        this.entityManager.persist(new Board("subject", "content"));
        Board board = this.boardRepository.findBySubject("subject");
        assertThat(board.getSubject()).isEqualTo("subject");
        assertThat(board.getContent()).isEqualTo("content");
    }
}