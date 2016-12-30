package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.message.repository.model.AnnounceModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnounceMapper {

    AnnounceModel findById(long id);

    int findAnnounceCount(@Param("title") String title);

    List<AnnounceModel> findAnnounce(@Param("title") String title,
                                     @Param("index") int index,
                                     @Param("pageSize") int pageSize);

    void create(AnnounceModel announceModel);

    void update(AnnounceModel announceModel);

    void delete(long id);
}
