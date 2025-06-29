package org.example.service;

import org.example.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.vo.PageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2025-05-18
 */
public interface BookService extends IService<Book> {
    public PageVO pageList(Integer currentPage);
    public PageVO searchByKeyWord(String keyWord,Integer currentPage);

    public PageVO searchBySort(Integer sid,Integer currentPage);

}
