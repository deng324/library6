package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.Back;
import org.example.entity.Book;
import org.example.entity.Borrow;
import org.example.entity.User;
import org.example.mapper.BackMapper;
import org.example.mapper.BookMapper;
import org.example.mapper.BorrowMapper;
import org.example.mapper.UserMapper;
import org.example.service.BackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.vo.BackVO;
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
public class BackServiceImpl extends ServiceImpl<BackMapper, Back> implements BackService {

    @Autowired
    private BackMapper backMapper;
    @Autowired
    private BorrowMapper borrowMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;
    @Override
    public List<BackVO> backList() {
        QueryWrapper<Back> backQueryWrapper = new QueryWrapper<>();
        backQueryWrapper.eq("status", 0);
        List<Back> backList = this.backMapper.selectList(backQueryWrapper);
        List<BackVO> backVOList = new ArrayList<>();
        for (Back back : backList) {
            BackVO backVO = new BackVO();
            Borrow borrow = this.borrowMapper.selectById(back.getBrid());
            User user = this.userMapper.selectById(borrow.getUid());
            backVO.setUserName(user.getUsername());
            Book book = this.bookMapper.selectById(borrow.getBid());
            BeanUtils.copyProperties(book, backVO);
            backVO.setBookName(book.getName());
            BeanUtils.copyProperties(back, backVO);
            backVOList.add(backVO);
        }
        return backVOList;
    }
}
