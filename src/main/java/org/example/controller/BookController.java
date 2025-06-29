package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.Book;
import org.example.entity.Borrow;
import org.example.entity.Recommendation;
import org.example.entity.Sort;
import org.example.mapper.BorrowMapper;
import org.example.service.BookService;
import org.example.service.BorrowService;
import org.example.service.RecommendationService;
import org.example.service.SortService;
import org.example.vo.BookVO;
import org.example.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private SortService sortService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/list/{current}")
    public String list(@PathVariable("current") Integer current, Model model) {
        PageVO pageVO = this.bookService.pageList(current);
        model.addAttribute("page", pageVO);
        model.addAttribute("sortList", this.sortService.list()); //查询所有类别信息，并把数据填充到前端页面表单
        return "/user/list";
    }

    @PostMapping("/search")
    public String search(String keyWord,Integer current,Integer sid,Model model){
        PageVO pageVO = null;
        //类别检索
        if(!sid.equals(0)){
            pageVO = this.bookService.searchBySort(sid, current);
        } else {
            //关键字检索带分页
            pageVO = this.bookService.searchByKeyWord(keyWord, current);
        }
        model.addAttribute("page", pageVO);
        model.addAttribute("sortList", this.sortService.list());
        return "/user/list";
    }

    @PostMapping("/findByKey")
    public String findByKey(String key,Model model){
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(key), "name", key)
                .or()
                .like(StringUtils.isNotBlank(key), "author", key)
                .or()
                .like(StringUtils.isNotBlank(key), "publish", key);
        List<Book> list = this.bookService.list(queryWrapper);
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

    @PostMapping("/add")
    public String add(Book book){
        this.bookService.save(book);
        return "redirect:/sysadmin/bookList";
    }


    @GetMapping("/findById/{id}")
    public String findById(@PathVariable("id") Integer id,Model model){
        //先通过书籍ID获取该书籍
        Book book = this.bookService.getById(id);
        model.addAttribute("book", book);
        model.addAttribute("list", this.sortService.list());
        //将该书籍信息回显到页面
        return "/sysadmin/updateBook";
    }

    @PostMapping("/update")
    public String update(Book book){
        //更新书籍信息
        this.bookService.updateById(book);
        return "redirect:/sysadmin/bookList";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bid", id);
        this.borrowService.remove(queryWrapper);
        this.bookService.removeById(id);
        return "redirect:/sysadmin/bookList";
    }


    @PostMapping("/book/addFromRecommend")
    public String addFromRecommend(Recommendation recommendation) {
        Book book = new Book();
        book.setName(recommendation.getName());
        book.setSid(recommendation.getSid());
        book.setAuthor(recommendation.getAuthor());
        book.setPublish(recommendation.getPublish());
        book.setEdition(recommendation.getEdition());
        book.setNumber(recommendation.getNumber());
        bookService.save(book);
        // 更新 recommendation 状态为已通过
        recommendation.setStatus("已通过");
        recommendationService.updateById(recommendation);
        return "redirect:/recommendation/list";
    }
    @PostMapping("/disagree")
    public String disagree(@RequestParam Integer id) {
        Recommendation recommendation = recommendationService.getById(id);
        recommendation.setStatus("已拒绝");
        recommendationService.updateById(recommendation);
        return "redirect:/recommendation/list";
    }

    /**
     * 管理员点击【同意】按钮后，将荐购书籍添加到书籍表
     */
    @GetMapping("/addFromRecommend")
    public String addFromRecommend(@RequestParam Integer id) {
        Recommendation rec = recommendationService.getById(id);
        if (rec != null && (rec.getStatus() == null || rec.getStatus().isEmpty())) {
            // 状态为空才允许添加书籍
            Book book = new Book();
            book.setName(rec.getName());
            book.setSid(rec.getSid());
            book.setAuthor(rec.getAuthor());
            book.setPublish(rec.getPublish());
            book.setEdition(rec.getEdition());
            book.setNumber(rec.getNumber());

            bookService.save(book);

            rec.setStatus("已通过");
            recommendationService.updateById(rec);
        }

        return "redirect:/recommendation/sysadminList";
    }





}

