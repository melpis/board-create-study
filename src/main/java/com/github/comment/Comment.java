package com.github.comment;

import com.github.board.domain.Board;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter @Setter
public class Comment {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Board owner;

    private String content;
}
