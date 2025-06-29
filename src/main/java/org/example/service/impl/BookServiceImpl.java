package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.Book;
import org.example.entity.Sort;
import org.example.mapper.BookMapper;
import org.example.mapper.SortMapper;
import org.example.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.vo.BookVO;
import org.example.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2025-05-18
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private BookMapper bookMapper;



    @Autowired
    private SortMapper sortMapper;
    @Override
    public PageVO pageList(Integer currentPage) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.gt("number", 0);
        Page<Book> page = new Page<>(currentPage, 10); //定义分页对象，当前页/每页显示多少条数据
        Page<Book> resultPage = this.bookMapper.selectPage(page, bookQueryWrapper); //包含分页信息的查询
        PageVO pageVO = new PageVO();  //返回页面的数据对象
        pageVO.setCurrentPage(resultPage.getCurrent()); //当前页
        pageVO.setTotalPage(resultPage.getPages());  //总页数
        List<BookVO> result = new ArrayList<>();
        for (Book book : resultPage.getRecords()) {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(book, bookVO);
            QueryWrapper<Sort> sortQueryWrapper = new QueryWrapper<>();
            sortQueryWrapper.eq("id", book.getSid());
            Sort sort = this.sortMapper.selectOne(sortQueryWrapper);
            bookVO.setSname(sort.getName());
            result.add(bookVO);
        }
        pageVO.setData(result);
        return pageVO;
    }

    @Override
    public PageVO searchByKeyWord(String keyWord, Integer currentPage) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.gt("number", 0);  //查询number>0 （查询所有）
        //追加条件，在所有的数据里面，再通过关键字查询
        //StringUtils.isNotBlank(keyWord)表示判断输入的keyWord是否为空
        bookQueryWrapper.like(StringUtils.isNotBlank(keyWord), "name", keyWord)
                .or()
                .like(StringUtils.isNotBlank(keyWord), "author", keyWord)
                .or()
                .like(StringUtils.isNotBlank(keyWord), "publish", keyWord);
        Page<Book> page = new Page<>(currentPage, 5);
        Page<Book> resultPage = this.bookMapper.selectPage(page, bookQueryWrapper);
        PageVO pageVO = new PageVO();
        pageVO.setCurrentPage(resultPage.getCurrent());
        pageVO.setTotalPage(resultPage.getPages());
        List<BookVO> result = new ArrayList<>();
        for (Book book : resultPage.getRecords()) {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(book, bookVO);
            QueryWrapper<Sort> sortQueryWrapper = new QueryWrapper<>();
            sortQueryWrapper.eq("id", book.getSid());
            Sort sort = this.sortMapper.selectOne(sortQueryWrapper);
            bookVO.setSname(sort.getName());
            result.add(bookVO);
        }
        pageVO.setData(result);
        return pageVO;
    }

    @Override
    public PageVO searchBySort(Integer sid, Integer currentPage) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.gt("number", 0);
        bookQueryWrapper.eq("sid",sid); //添加查询条件，通过sid查类别
        Page<Book> page = new Page<>(currentPage, 5);
        Page<Book> resultPage = this.bookMapper.selectPage(page, bookQueryWrapper);
        PageVO pageVO = new PageVO();
        pageVO.setCurrentPage(resultPage.getCurrent());
        pageVO.setTotalPage(resultPage.getPages());
        List<BookVO> result = new ArrayList<>();
        for (Book book : resultPage.getRecords()) {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(book, bookVO);
            QueryWrapper<Sort> sortQueryWrapper = new QueryWrapper<>();
            sortQueryWrapper.eq("id", book.getSid());
            Sort sort = this.sortMapper.selectOne(sortQueryWrapper);
            bookVO.setSname(sort.getName());
            result.add(bookVO);
        }
        pageVO.setData(result);
        return pageVO;
    }

}
