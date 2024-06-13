package com.example.pet.board.domain;

import com.example.pet.comment.domain.Comment;
import com.example.pet.common.domain.BaseTimeEntity;
import com.example.pet.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.UUID;

@Table(name="board")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "board")
    private List<Image> images;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private Boolean isPetHelp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<LikedBoard> likedBoards;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Board(String title, String content, Boolean isPetHelp, Member member, List<Image> images) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.images = images;
        this.likeCount = 0;
        this.isPetHelp = isPetHelp;
    }

    public void update(String title, String content, Boolean isPetHelp, List<Image> images) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.isPetHelp = isPetHelp;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
