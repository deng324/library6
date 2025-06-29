package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.example.entity.Back;
import org.example.entity.Book;
import org.example.entity.Borrow;
import org.example.entity.Sort;
import org.example.service.BackService;
import org.example.service.BookService;
import org.example.service.BorrowService;
import org.example.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/sort")
public class SortController {

    @Autowired
    private SortService sortService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private BackService backService;
    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("list", this.sortService.list());
        return "sysadmin/addBook";
    }


    @PostMapping("/search")
    public String search(String name,Model model){
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        List<Sort> list = this.sortService.list(queryWrapper);
        model.addAttribute("list", list);
        return "/sysadmin/sort";
    }

    @PostMapping("/add")
    public String add(Sort sort){
        this.sortService.save(sort);
        return "redirect:/sysadmin/sortList";
    }

    @GetMapping("/findById/{id}")
    public String findById(@PathVariable("id") Integer id, Model model){
        //先查
        Sort sort = this.sortService.getById(id);
        model.addAttribute("sort", sort);
        //然后数据回显
        return "/sysadmin/updateSort";
    }

    @PostMapping("/update")
    public String update(Sort sort){
        this.sortService.updateById(sort);
        return "redirect:/sysadmin/sortList";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        //删除类别，同时要删除该类别的所有书籍
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.eq("sid", id);
        //通过类别ID，查询到类别的所有书籍
        List<Book> bookList = this.bookService.list(bookQueryWrapper);
        for (Book book : bookList) {
            //要删除书籍，要把借书表里面该书籍数据删除
            QueryWrapper<Borrow> borrowQueryWrapper = new QueryWrapper<>();
            borrowQueryWrapper.eq("bid", book.getId());
            //通过书籍ID查到所有的借书记录
            List<Borrow> borrowList = this.borrowService.list(borrowQueryWrapper);
            for (Borrow borrow : borrowList) {
                //要删除借书记录，同时要删除还书表中的记录
                QueryWrapper<Back> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("brid", borrow.getId());
                //通过还书ID直接删除
                this.backService.remove(queryWrapper);
                this.borrowService.removeById(borrow.getId());
            }
            this.bookService.removeById(book.getId());
        }
        this.sortService.removeById(id);
        return "redirect:/sysadmin/sortList";
    }

}

