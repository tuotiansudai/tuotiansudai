package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinQueryContractResponseModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinQueryContractResponseMapper {

    @Insert("INSERT INTO anxin_query_contract_response(request_id,batch_no,contract_no,contract_type,tx_time,is_sign,ret_code,ret_message,json_data,created_time)" +
            "VALUES(#{requestId},#{batchNo},#{contractNo},#{contractType},#{txTime},#{isSign},#{retCode},#{retMessage},#{jsonData},#{createdTime})")
    void create(AnxinQueryContractResponseModel anxinQueryContractResponseModel);

}
