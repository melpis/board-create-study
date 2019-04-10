package com.github.board.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void createWhenSubjectIsNullShouldThrowException() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Board(null, "content"))
                .withMessage("Subject must not be empty");
    }

    @Test
    public void createWhenSubjectIsEmptyShouldThrowException() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Board("", "content"))
                .withMessage("Subject must not be empty");
    }

    @Test
    public void saveShouldPersistData() {
        Board board = this.entityManager.persistFlushFind(new Board("subject", "content"));
        assertThat(board.getSubject()).isEqualTo("subject");
        assertThat(board.getContent()).isEqualTo("content");
    }

}