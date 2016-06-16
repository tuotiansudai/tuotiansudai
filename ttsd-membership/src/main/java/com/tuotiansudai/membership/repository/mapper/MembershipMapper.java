package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.MembershipModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipMapper {

    MembershipModel findById(long id);

    MembershipModel findByLevel(long level);

    List<Integer> findAllLevels();
    
    MembershipModel findByExperience(long experience);
}
