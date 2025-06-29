package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.Book;
import org.example.entity.Borrow;
import org.example.entity.Sort;
import org.example.entity.User;
import org.example.mapper.BookMapper;
import org.example.mapper.BorrowMapper;
import org.example.mapper.SortMapper;
import org.example.mapper.UserMapper;
import org.example.service.BorrowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.vo.AdminBorrowVO;
import org.example.vo.BorrowVO;
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
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BorrowMapper borrowMapper;
    @Autowired
    private SortMapper sortMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public void add(Integer uid, Integer bid) {
        Borrow borrow = new Borrow(); //定义一个Borrow对象
        borrow.setBid(bid);  //设置书籍id(要借阅的书籍id)
        borrow.setUid(uid);  //设置用户id（借书的用户id）
        this.borrowMapper.insert(borrow); //将Borrow对象加入数据库
        Book book = this.bookMapper.selectById(bid);
        book.setNumber(book.getNumber()-1);  //同时需要修改书籍的数量，数量-1
        this.bookMapper.updateById(book);

    }

    @Override
    public List<BorrowVO> borrowList(Integer uid) {
        QueryWrapper<Borrow> borrowQueryWrapper = new QueryWrapper<>();
        borrowQueryWrapper.eq("uid", uid);
        List<Borrow> borrowList = this.borrowMapper.selectList(borrowQueryWrapper);
        List<BorrowVO> borrowVOList = new ArrayList<>();
        for (Borrow borrow : borrowList) {
            BorrowVO borrowVO = new BorrowVO();
            BeanUtils.copyProperties(borrow, borrowVO);
            Book book = this.bookMapper.selectById(borrow.getBid());
            BeanUtils.copyProperties(book, borrowVO);
            borrowVO.setBookName(book.getName());
//            borrowVO.setStartTime(borrow.getStartTime());  // 显式设置申请时间
            Sort sort = this.sortMapper.selectById(book.getSid());
            borrowVO.setSortName(sort.getName());
            borrowVOList.add(borrowVO);
        }
        return borrowVOList;
    }

    @Override
    public List<BorrowVO> backList(Integer uid) {
        QueryWrapper<Borrow> borrowQueryWrapper = new QueryWrapper<>();
        borrowQueryWrapper.eq("uid", uid);  //用户id
        borrowQueryWrapper.eq("status", 1); //查询状态为1的书籍，即审批通过的书籍
        List<Borrow> borrowList = this.borrowMapper.selectList(borrowQueryWrapper);
        List<BorrowVO> borrowVOList = new ArrayList<>();
        for (Borrow borrow : borrowList) {
            BorrowVO borrowVO = new BorrowVO();
            BeanUtils.copyProperties(borrow, borrowVO);
            Book book = this.bookMapper.selectById(borrow.getBid());
            BeanUtils.copyProperties(book, borrowVO);
            borrowVO.setId(borrow.getId());
            borrowVO.setBookName(book.getName());
            borrowVOList.add(borrowVO);
        }
        return borrowVOList;
    }

    @Override
    public List<AdminBorrowVO> adminBorrowList() {
        QueryWrapper<Borrow> borrowQueryWrapper = new QueryWrapper<>();
        borrowQueryWrapper.eq("status", 0);  //查找状态为0的书籍（需要审核的书籍）
        List<Borrow> borrowList = this.borrowMapper.selectList(borrowQueryWrapper);
        List<AdminBorrowVO> adminBorrowVOList = new ArrayList<>();
        //需要做转换，转换成页面展示的内容
        for (Borrow borrow : borrowList) {
            AdminBorrowVO adminBorrowVO = new AdminBorrowVO(); //创建AdminBorrowVO对象，（即页面展示内容的对象）
            BeanUtils.copyProperties(borrow, adminBorrowVO);
            User user = this.userMapper.selectById(borrow.getUid());  //通过借书id找到借书的用户
            adminBorrowVO.setUserName(user.getUsername());  //拿到的用户名设置到adminBorrowVO对象的用户名中
            Book book = this.bookMapper.selectById(borrow.getBid()); //通过借书ID查询到对应的书籍
            adminBorrowVO.setBookName(book.getName());  //将书籍的名字设置到adminBorrowVO对象的书籍名字当中
            BeanUtils.copyProperties(book, adminBorrowVO);
            Sort sort = this.sortMapper.selectById(book.getSid()); //通过书籍的sid查询到Sort
            adminBorrowVO.setSortName(sort.getName()); //将Sort的名字设置到adminBorrowVO对象的SortName当中，即显示类别的中文名字
            adminBorrowVO.setId(borrow.getId());
            adminBorrowVOList.add(adminBorrowVO);
        }
        return adminBorrowVOList;
    }
}
