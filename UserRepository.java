package com.example.kaigo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kaigo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 同じ名前が既に登録されているか調べる命令
    boolean existsByName(String name);

    // 同じ名前から始まる人が何人いるか数える命令（A, B...をつけるため）
    long countByNameStartingWith(String name);
}