package com.liveklass.testa.domain.classmate.domain;

import com.liveklass.testa.domain.auth.domain.Account;
import com.liveklass.testa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classmates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Classmate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classmate_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(nullable = false)
    private String name;

    public static Classmate create(Account account, String name) {
        Classmate classmate = new Classmate();
        classmate.account = account;
        classmate.name = name;
        return classmate;
    }
}
