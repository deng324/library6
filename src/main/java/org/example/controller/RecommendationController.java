package org.example.controller;

import org.example.entity.Recommendation;
import org.example.entity.Sort;
import org.example.service.RecommendationService;
import org.example.service.SortService; // 新增导入
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired // 注入 SortService
    private SortService sortService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Recommendation> recommendationList = recommendationService.list();
        model.addAttribute("recommendationList", recommendationList);
        return "user/recommendation"; // 返回 recommendation.html 页面
    }

    @PostMapping("/add")
    public String add(Recommendation recommendation, Model model) {
        try {
            recommendationService.save(recommendation);
            return "redirect:/recommendation/list";
        } catch (Exception e) {
            model.addAttribute("error", "荐购提交失败：" + e.getMessage());
            List<Sort> sortList = sortService.list();
            model.addAttribute("list", sortList);
            return "user/addRecommendation"; // 返回提交页面并显示错误信息
        }
    }


    @GetMapping("/addRecommendation")
    public String addRecommendation(Model model) {
        // 获取所有类别数据
        List<Sort> sortList = sortService.list();
        model.addAttribute("list", sortList);
        return "user/addRecommendation";
    }
    @GetMapping("/sysadminList")
    public String sysadminList(Model model) {
        List<Recommendation> recommendationList = recommendationService.list();
        model.addAttribute("recommendationList", recommendationList);
        return "sysadmin/recommendation"; // 返回 sysadmin 下的模板
    }

}
