package com.example.kaigo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kaigo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // これだけでデータの一覧取得や保存（CRUD）が自動的に使えます
}