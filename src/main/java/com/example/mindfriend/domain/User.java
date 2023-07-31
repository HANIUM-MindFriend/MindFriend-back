package com.example.mindfriend.domain;

import com.example.mindfriend.common.entity.BaseEntity;
import com.example.mindfriend.domain.enums.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userIdx")
    private Long userIdx;

    @Column(name = "userId")
    private String userId;

    @Column(name = "userPassword")
    private String userPassword;

    @Column(name = "userNickname")
    private String userNickname;

    @Column(name = "userBirth")
    private String userBirth;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "userProfileImg")
    private String userProfileImg;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Diary> DiaryList = new ArrayList<>();

    // 생성 메서드
    @Builder
    public User(String userId, String userPassword, String userNickname, String userBirth, String userEmail,
                String userProfileImg) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userBirth = userBirth;
        this.userEmail = userEmail;
        this.userProfileImg = userProfileImg;
        this.authority = Authority.USER;
    }
}
