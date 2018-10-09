package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BlacklistModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistMapper {
    boolean userIsInBlacklist(String loginName);

    void massInsertBlacklist(List<BlacklistModel> blacklistModels);
}
