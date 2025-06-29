package org.example.controller;


import org.example.entity.Back;
import org.example.entity.Book;
import org.example.entity.Borrow;
import org.example.entity.User;
import org.example.service.BackService;
import org.example.service.BookService;
import org.example.service.BorrowService;
import org.example.vo.BackVO;
import org.example.vo.BorrowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-05-18
 */
@Controller
@RequestMapping("/back")
public class BackController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private BackService backService;

    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public String list(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<BorrowVO> backList = this.borrowService.backList(user.getId());
        model.addAttribute("list", backList);
        return "/user/back";
    }

    @GetMapping("/add")
    public String add(Integer id){
        Back back = new Back();
        back.setBrid(id);
        this.backService.save(back);
        Borrow borrow = this.borrowService.getById(id);
        borrow.setStatus(3); //将状态设置成3
        this.borrowService.updateById(borrow);
        return "redirect:/back/list";
    }

    @GetMapping("/adminList")
    public String adminList(Model model){
        List<BackVO> backVOList = this.backService.backList();
        model.addAttribute("list", backVOList);
        return "/admin/back";
    }

    @GetMapping("/allow")
    public String allow(Integer id){
        Back back = this.backService.getById(id);
        back.setStatus(1);
        this.backService.updateById(back);
        Borrow borrow = this.borrowService.getById(back.getBrid());
        Book book = this.bookService.getById(borrow.getBid());
        book.setNumber(book.getNumber()+1);
        this.bookService.updateById(book);
        return "redirect:/back/adminList";
    }

}

