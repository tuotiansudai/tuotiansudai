package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AnnouncementManagementModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementManagementMapper {

    int findAnnouncementManagementCount(@Param("id") long id,@Param("title") String title);

    List<AnnouncementManagementModel> findAnnouncementManagement(@Param("id") long id,@Param("title") String title,@Param("startLimit") int startLimit,@Param("endLimit") int endLimit);

}
