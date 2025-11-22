package Xthon.gAIde.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_login_id", nullable = false, unique = true, length = 255)
    private String loginId;

    @Column(name = "member_password", nullable = false, length = 255)
    private String password;

    @Column(name = "member_name", nullable = false, length = 100)
    private String name;

    @Builder
    public MemberEntity(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }
}
