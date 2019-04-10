package com.github.board.domain;

import com.github.comment.Comment;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor

public class Board {

    @Id @GeneratedValue
    private Long id;

    private String subject;

    private String content;

    private int readCount;

    public Board(String subject, String content){
        Assert.hasLength(subject, "Subject must not be empty");
        this.subject = subject;
        this.content = content;
        this.readCount = 0;
    }

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void increaseReadCount(){
        this.readCount += 1;
    }

    public void setComments(List<Comment> comments){
        this.comments = comments;
        this.comments.forEach(comment -> comment.setOwner(this));
    }


    public void addComments(Comment comment){
        this.getComments().add(comment);
        comment.setOwner(this);
    }

}
