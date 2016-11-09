package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.HelpCategory;
import com.tuotiansudai.repository.model.HelpCenterModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpCenterMapper {

    List<HelpCenterModel> findAllHelpCenterByTitleOrCategoryOrHot(@Param(value = "title") String title,
                                                                  @Param(value = "category") HelpCategory category,
                                                                  @Param(value = "hot") boolean hot);

    List<HelpCenterModel> findAllHelpCenterCategory();
}
