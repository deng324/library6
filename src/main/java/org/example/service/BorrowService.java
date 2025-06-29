package org.example.service;

import org.example.entity.Borrow;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.vo.AdminBorrowVO;
import org.example.vo.BorrowVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2025-05-18
 */
public interface BorrowService extends IService<Borrow> {
    public void add(Integer uid,Integer bid);

    public List<BorrowVO> borrowList(Integer uid);

    public List<BorrowVO> backList(Integer uid);

    public List<AdminBorrowVO> adminBorrowList();

}
