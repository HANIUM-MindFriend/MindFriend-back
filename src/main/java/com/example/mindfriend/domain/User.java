package com.example.mindfriend.domain;

import com.example.mindfriend.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
