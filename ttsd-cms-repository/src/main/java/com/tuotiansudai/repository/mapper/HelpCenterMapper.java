package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.HelpCenterModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpCenterMapper {

    void create(HelpCenterModel helpCenterModel);

    List<HelpCenterModel> findAllHelpCenterByTitleOrCategoryOrHot(@Param(value = "keywords") String keywords,
                                                                  @Param(value = "category") String category,
                                                                  @Param(value = "hot") String hot);

    List<String> findAllHelpCenterCategory();
}
