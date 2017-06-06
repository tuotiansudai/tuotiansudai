package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CelebrationSingleService {

   @Autowired
   private CelebrationSingleService singleService;

   public int findUserDrewChange(String loginName){

      if(Strings.isNullOrEmpty(loginName)){
         return -1;
      }
      return singleService.findUserDrewChange(loginName);
   }

}
