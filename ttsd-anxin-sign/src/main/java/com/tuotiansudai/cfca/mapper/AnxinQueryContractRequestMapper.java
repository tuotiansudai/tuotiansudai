package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinQueryContractRequestModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinQueryContractRequestMapper {

    @Insert("INSERT INTO anxin_query_contract_request (business_id,batch_no,tx_time,created_time)" +
            "VALUES(#{businessId},#{batchNo},#{txTime},#{createdTime})")
    void create(AnxinQueryContractRequestModel anxinQueryContractRequestModel);

}
