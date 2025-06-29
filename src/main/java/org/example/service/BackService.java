package org.example.service;

import org.example.entity.Back;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.vo.BackVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2025-05-18
 */
public interface BackService extends IService<Back> {
    public List<BackVO> backList();

}
