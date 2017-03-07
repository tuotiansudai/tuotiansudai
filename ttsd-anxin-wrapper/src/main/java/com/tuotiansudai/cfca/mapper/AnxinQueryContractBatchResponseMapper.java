package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinQueryContractBatchResponseModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinQueryContractBatchResponseMapper {

    @Insert("INSERT INTO query_contract_batch_response(business_id,batch_no,tx_time,ret_code,ret_message,template_id,is_sign,contract_no,file_id,code,message,sign_infos,investment_info,created_time)" +
            "VALUES(#{businessId},#{batchNo},#{txTime},#{retCode},#{retMessage},#{templateId},#{isSign},#{contractNo},#{fileId},#{code},#{message},#{signInfos},#{investmentInfo},#{createdTime})")
    void create(AnxinQueryContractBatchResponseModel anxinQueryContractBatchResponseModel);

}
