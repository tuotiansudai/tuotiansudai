package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinCreateContractBatchResponseModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinCreateContractBatchResponseMapper {

    @Insert("INSERT INTO create_contract_batch_response (business_id,order_id,batch_no,ret_code,ret_message,contract_no,tx_time,template_id,is_sign,sign_infos,investment_info,created_time) " +
            "VALUES (#{businessId},#{orderId},#{batchNo},#{retCode},#{retMessage},#{contractNo},#{txTime},#{templateId},#{isSign},#{signInfos},#{investmentInfo},#{createdTime})")
    void create(AnxinCreateContractBatchResponseModel anxinCreateContractBatchResponseModel);
}
