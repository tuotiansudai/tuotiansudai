package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.BaseResponse;
import com.tuotiansudai.util.RedisWrapperClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ThirdAnniversaryService {

    private final String TEAM_LOG = "TEAM:{0}:{1}";

    private final Map<Integer, String> FOOTBALL_TEAMS = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(1,"agenting")
            .put(2,"aiji")
            .put(3,"aodaliya")
            .put(4,"banama")
            .put(5,"baxi")
            .put(6,"bilishi")
            .put(7,"bilu")
            .put(8,"bingdao")
            .put(9,"bolan")
            .put(10,"danmai")
            .put(11,"deguo")
            .put(12,"eluosi")
            .put(13,"faguo")
            .put(14,"gelunbiya")
            .put(15,"gesidani")
            .put(16,"hanguo")
            .put(17,"keluodiya")
            .put(18,"moluoge")
            .put(19,"moxige")
            .put(20,"niriliya")
            .put(21,"putaoya")
            .put(22,"riben")
            .put(23,"ruidian")
            .put(24,"ruishi")
            .put(25,"saierweiya")
            .put(26,"saineijiaer")
            .put(27,"shatealabo")
            .put(28,"tunisi")
            .put(29,"wulagui")
            .put(30,"xibanya")
            .put(31,"yilang")
            .put(32,"yinggelan")
            .build());

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();


    public BaseResponse<List<Map<String, Integer>>> getTeamLogos(String loginName){

    }

    public BaseResponse draw(String loginName){
        
    }
}
