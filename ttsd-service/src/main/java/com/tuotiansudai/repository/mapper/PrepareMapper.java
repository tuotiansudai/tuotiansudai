package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.PrepareModel;
import org.springframework.stereotype.Repository;

@Repository
public interface PrepareMapper {

    void create(PrepareModel prepareModel);
}
