package org.example.service.impl;

import org.example.entity.Recommendation;
import org.example.mapper.RecommendationMapper;
import org.example.service.RecommendationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RecommendationServiceImpl extends ServiceImpl<RecommendationMapper, Recommendation> implements RecommendationService {
}
