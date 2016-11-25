package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinQueryContractBatchRequestModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinQueryContractBatchRequestMapper {

    @Insert("INSERT INTO query_contract_batch_request (business_id,batch_no,tx_time,created_time)" +
            "VALUES(#{businessId},#{batchNo},#{txTime},#{createdTime})")
    void create(AnxinQueryContractBatchRequestModel anxinQueryContractBatchRequestModel);

}
