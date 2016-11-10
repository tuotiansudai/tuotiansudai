package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinQueryContractRequestModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinQueryContractRequestMapper {

    @Insert("INSERT INTO anxin_query_contract_request (batch_no,tx_time,json_data,created_time)" +
            "VALUES(#{batchNo},#{txTime},#{jsonDate},#{createdTime})")
    void create(AnxinQueryContractRequestModel anxinQueryContractRequestModel);

}
