package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AnnounceModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnounceMapper {

    int findAnnounceCount(@Param("id") Long id, @Param("title") String title);

    List<AnnounceModel> findAnnounce(@Param("id") Long id,
                                     @Param("title") String title,
                                     @Param("index") int index,
                                     @Param("pageSize") int pageSize);

    void create(AnnounceModel announceModel);

    void update(AnnounceModel announceModel);

    void delete(long id);

    AnnounceModel findById(long id);

}
