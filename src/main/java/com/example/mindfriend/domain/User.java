package com.example.mindfriend.domain;

import com.example.mindfriend.common.entity.BaseEntity;
import com.example.mindfriend.domain.enums.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User extends BaseEntity implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한 정보를 반환해야 합니다.
        // 권한 정보를 저장하는 필드가 없다면, 별도의 권한 정보를 정의하고 사용자가 어떤 권한을 가지는지 반환해야 합니다.
        // 예를 들어, ROLE_USER, ROLE_ADMIN 등의 권한을 정의하고, 사용자가 가지는 권한에 따라 해당 Role 객체를 반환하면 됩니다.
        // 별도의 권한 정보가 없다면, 간단하게 빈 리스트를 반환할 수도 있습니다.
        return new ArrayList<>(); // 또는 null 또는 사용자가 가지는 권한 목록을 반환
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userId;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true; // 사용자 계정의 유효 기간이 만료되지 않았는지를 반환
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 사용자 계정이 잠겨 있는지를 반환
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 사용자의 비밀번호가 만료되지 않았는지를 반환
    }

    @Override
    public boolean isEnabled() {
        return true; // 사용자 계정이 활성화되어 있는지를 반환
    }


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
