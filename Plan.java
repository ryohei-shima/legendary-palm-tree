package com.example.kaigo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 厚労省様式に準拠した項目
    @Column(name = "create_date")
    private LocalDate createDate; // 計画作成日

    @Column(name = "long_term_goal", length = 1000)
    private String longTermGoal; // 長期目標（心身機能・活動・参加）

    @Column(name = "short_term_goal", length = 1000)
    private String shortTermGoal; // 短期目標（細分化した行為）

    @Column(name = "training_program", length = 1000)
    private String trainingProgram; // 具体的な訓練項目（プログラム・頻度・時間など）

    @Column(name = "home_program", length = 1000)
    private String homeProgram; // 自宅や日々の生活での自主訓練項目

    @Column(name = "evaluation", length = 1000)
    private String evaluation; // 3ヶ月後の評価・進捗状況

    // 利用者（User）との紐づけ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // ゲッター・セッター
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getCreateDate() { return createDate; }
    public void setCreateDate(LocalDate createDate) { this.createDate = createDate; }
    public String getLongTermGoal() { return longTermGoal; }
    public void setLongTermGoal(String longTermGoal) { this.longTermGoal = longTermGoal; }
    public String getShortTermGoal() { return shortTermGoal; }
    public void setShortTermGoal(String shortTermGoal) { this.shortTermGoal = shortTermGoal; }
    public String getTrainingProgram() { return trainingProgram; }
    public void setTrainingProgram(String trainingProgram) { this.trainingProgram = trainingProgram; }
    public String getHomeProgram() { return homeProgram; }
    public void setHomeProgram(String homeProgram) { this.homeProgram = homeProgram; }
    public String getEvaluation() { return evaluation; }
    public void setEvaluation(String evaluation) { this.evaluation = evaluation; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}