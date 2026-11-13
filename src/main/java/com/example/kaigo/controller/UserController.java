package com.example.kaigo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.kaigo.entity.Plan;
import com.example.kaigo.entity.User;
import com.example.kaigo.repository.PlanRepository; // 👈 追加
import com.example.kaigo.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository; // 👈 追加

    // 1. 利用者一覧画面の表示
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user_list";
    }

    // 2. 利用者登録画面の表示
    @GetMapping("/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user_form";
    }

    // 3. 利用者登録の実行処理
    @PostMapping
    public String saveUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    // 4. 利用者の詳細画面を表示（★計画書の一覧も自動的にここに含まれます）
    @GetMapping("/{id}")
    public String showUserDetail(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get());
            return "user_detail";
        } else {
            return "redirect:/users";
        }
    }

    // 5. 情報の追記・修正画面を表示
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get());
            return "user_edit";
        } else {
            return "redirect:/users";
        }
    }

    // 6. 情報の追記・修正を実行
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        user.setId(id);
        userRepository.save(user); 
        return "redirect:/users/" + id;
    }

    // 7. 【新規追加】個別機能訓練計画書の作成画面を表示
    @GetMapping("/{userId}/plans/new")
    public String showPlanForm(@PathVariable("userId") Long userId, Model model) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        
        Plan plan = new Plan();
        plan.setUser(user); // どの利用者の計画書かを紐づける
        
        model.addAttribute("user", user);
        model.addAttribute("plan", plan);
        return "plan_form"; // plan_form.html を呼び出す
    }

    // 8. 【新規追加】個別機能訓練計画書の登録を実行
    @PostMapping("/{userId}/plans")
    public String savePlan(@PathVariable("userId") Long userId, @ModelAttribute("plan") Plan plan) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        
        plan.setUser(user); // 確実に利用者を紐づける
        planRepository.save(plan); // 計画書を保存
        
        return "redirect:/users/" + userId; // 登録後は利用者の詳細画面に戻る
    }
}