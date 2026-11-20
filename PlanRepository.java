package com.example.kaigo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kaigo.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    // 特定の利用者に紐づく計画書の一覧を、作成日の新しい順で取得する命令
    List<Plan> findByUserIdOrderByCreateDateDesc(Long userId);
}