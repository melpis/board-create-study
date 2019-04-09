package com.github.board.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void saveShouldPersistData() {
        Board board = this.entityManager.persistFlushFind(new Board("subject", "content"));
        assertThat(board.getSubject()).isEqualTo("subject");
        assertThat(board.getContent()).isEqualTo("content");
    }

}