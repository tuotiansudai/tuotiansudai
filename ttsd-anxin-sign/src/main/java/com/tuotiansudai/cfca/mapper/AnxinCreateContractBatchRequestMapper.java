package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinCreateContractBatchRequestModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinCreateContractBatchRequestMapper {

    @Insert("INSERT INTO create_contract_batch_request (business_id,order_id,batch_no,contract_no,tx_time,template_id,is_sign,sign_infos,investment_info,updated_time,created_time) " +
            "VALUES (#{businessId},#{orderId},#{batchNo},#{contractNo},#{txTime},#{templateId},#{isSign},#{signInfos},#{investmentInfo},#{updatedTime},#{createdTime})")
    void create(AnxinCreateContractBatchRequestModel anxinCreateContractBatchRequestModel);

}
