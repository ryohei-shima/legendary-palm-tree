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

    // 3. 利用者登録の実行処理（同姓同名チェック機能付き）
    @PostMapping
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        // 既に同じ名前が登録されているかチェック
        if (userRepository.existsByName(user.getName())) {
            long count = userRepository.countByNameStartingWith(user.getName());
            char suffix = (char) ('A' + count - 1); // 2人目ならA、3人目ならBを作る
            String suggestedName = user.getName() + suffix;

            model.addAttribute("warningMessage", 
                "⚠️ 同じ名前が登録されています。区別するため「" + suggestedName + "」に変更しました。問題なければもう一度「登録する」を押してください。");
            user.setName(suggestedName); // 入力欄の名前を自動で「〇〇A」に書き換える
            return "user_form"; // 登録せずに登録画面へ戻して注意を表示する
        }

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
    
    // 9. 【新規追加】個別機能訓練計画書の詳細画面を表示
    @GetMapping("/{userId}/plans/{planId}")
    public String showPlanDetail(@PathVariable("userId") Long userId, 
                                 @PathVariable("planId") Long planId, 
                                 Model model) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan Id:" + planId));
        
        model.addAttribute("user", user);
        model.addAttribute("plan", plan);
        return "user_plan_detail"; // user_plan_detail.html を呼び出す
    }
    
    // 10. 【新規追加】個別機能訓練計画書の編集画面を表示
    @GetMapping("/{userId}/plans/{planId}/edit")
    public String showEditPlanForm(@PathVariable("userId") Long userId,
                                   @PathVariable("planId") Long planId,
                                   Model model) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan Id:" + planId));

        model.addAttribute("user", user);
        model.addAttribute("plan", plan);
        return "plan_edit";
    }

    // 11. 【新規追加】個別機能訓練計画書の更新を実行（上書き保存）
    @PostMapping("/{userId}/plans/{planId}/update")
    public String updatePlan(@PathVariable("userId") Long userId,
                             @PathVariable("planId") Long planId,
                             @ModelAttribute("plan") Plan plan) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

        plan.setId(planId);     // 既存の計画書IDを指定して上書きにする
        plan.setUser(user);     // 利用者との紐づけを維持する
        planRepository.save(plan);

        return "redirect:/users/" + userId + "/plans/" + planId;
    }
}