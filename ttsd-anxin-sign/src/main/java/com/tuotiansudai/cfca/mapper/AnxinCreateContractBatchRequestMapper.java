package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinCreateContractBatchRequestModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinCreateContractBatchRequestMapper {

    @Insert("INSERT INTO create_contract_batch_request (business_id,order_id,batch_no,tx_time,template_id,is_sign,sign_infos,investment_info,created_time) " +
            "VALUES (#{businessId},#{orderId},#{batchNo},#{txTime},#{templateId},#{isSign},#{signInfos},#{investmentInfo},#{createdTime})")
    void create(AnxinCreateContractBatchRequestModel anxinCreateContractBatchRequestModel);

}
