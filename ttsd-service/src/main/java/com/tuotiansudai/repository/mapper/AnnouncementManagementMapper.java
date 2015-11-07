package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AnnouncementManagementModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementManagementMapper {

    int findAnnouncementManagementCount(@Param("id") Long id,@Param("title") String title);

    List<AnnouncementManagementModel> findAnnouncementManagement(@Param("id") Long id,@Param("title") String title,@Param("startLimit") int startLimit,@Param("endLimit") int endLimit);

    void create(AnnouncementManagementModel announcementManagementModel);

    void update(AnnouncementManagementModel announcementManagementModel);

    void delete(long id);

    AnnouncementManagementModel findById(long id);

}
