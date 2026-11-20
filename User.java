package com.example.kaigo.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "users") // MySQLの予約語「user」を避けるためテーブル名はusersにします
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "care_level")
    private String careLevel;

    @Column(name = "next_update_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextUpdateDate;
    
 // 【追加】既往歴と禁忌・備考
    @Column(name = "medical_history", length = 1000)
    private String medicalHistory;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    
    // 【追加】1人の利用者に、複数の計画書が紐づく設定
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Plan> plans;
    
    // ゲッター・セッター
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCareLevel() { return careLevel; }
    public void setCareLevel(String careLevel) { this.careLevel = careLevel; }
    public LocalDate getNextUpdateDate() { return nextUpdateDate; }
    public void setNextUpdateDate(LocalDate nextUpdateDate) { this.nextUpdateDate = nextUpdateDate; }
    // 【追加】新しい項目のゲッター・セッター
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    // 【追加】plansのゲッター・セッター
    public List<Plan> getPlans() {
        return plans;
    }

    // 【追加】生年月日のゲッター・セッター
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }
}