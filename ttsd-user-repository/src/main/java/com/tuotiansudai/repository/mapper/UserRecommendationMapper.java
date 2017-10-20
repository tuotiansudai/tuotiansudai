package com.tuotiansudai.repository.mapper;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserRecommendationMapper {
    List<String> findAllRecommendation(HashMap<String, Object> districtName);
}
