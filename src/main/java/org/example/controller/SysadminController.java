package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.example.entity.Book;
import org.example.entity.Sort;
import org.example.entity.User;
import org.example.service.BookService;
import org.example.service.SortService;
import org.example.service.UserService;
import org.example.vo.BookVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
@RequestMapping("/sysadmin")
public class SysadminController {

    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private SortService sortService;

    @GetMapping("/{url}")
    public String redirect(@PathVariable("url") String url){
        return "/sysadmin/"+url;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }

    //查询所有用户
    @GetMapping("/userList")
    public String userList(Model model){
        List<User> list = this.userService.list();
        model.addAttribute("list", list);
        return "/sysadmin/user";
    }

    //通过名字查询用户(模糊查询)
    @PostMapping("/findByName")
    public String findByName(String username,Model model){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //设置查询条件
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        List<User> list = this.userService.list(queryWrapper);
        model.addAttribute("list", list);
        return "/sysadmin/user";
    }

    @GetMapping("/bookList")
    public String bookList(Model model){
        List<Book> list = this.bookService.list();
        List<BookVO> bookVOList = new ArrayList<>();
        for (Book book : list) {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(book, bookVO);
            Sort sort = this.sortService.getById(book.getSid());
            bookVO.setSname(sort.getName());
            bookVOList.add(bookVO);
        }
        model.addAttribute("list", bookVOList);
        return "/sysadmin/book";
    }

    @GetMapping("/sortList")
    public String sortList(Model model){
        List<Sort> list = this.sortService.list();
        model.addAttribute("list", list);
        return "/sysadmin/sort";
    }

}

