//package com.tuotiansudai.paywrapper.service.impl;
//
//import cfca.sadk.algorithm.common.PKIException;
//import cfca.trustsign.common.vo.cs.CreateContractVO;
//import cfca.trustsign.common.vo.cs.SignInfoVO;
//import cfca.trustsign.common.vo.response.tx3.Tx3202ResVO;
//import com.google.common.base.Strings;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.Lists;
//import com.tuotiansudai.cfca.dto.AnxinContractType;
//import com.tuotiansudai.cfca.dto.ContractResponseView;
//import com.tuotiansudai.cfca.service.AnxinSignConnectService;
//import com.tuotiansudai.client.SmsWrapperClient;
//import com.tuotiansudai.dto.BaseDto;
//import com.tuotiansudai.dto.sms.GenerateContractErrorNotifyDto;
//import com.tuotiansudai.job.AnxinContractHandleJob;
//import com.tuotiansudai.job.JobType;
//import com.tuotiansudai.paywrapper.service.AnxinSignService;
//import com.tuotiansudai.repository.mapper.*;
//import com.tuotiansudai.repository.model.*;
//import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
//import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
//import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
//import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
//import com.tuotiansudai.util.AmountConverter;
//import com.tuotiansudai.util.JobManager;
//import com.tuotiansudai.util.UUIDGenerator;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.log4j.Logger;
//import org.joda.time.DateTime;
//import org.joda.time.LocalDate;
//import org.quartz.SchedulerException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.text.MessageFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//public class AnxinSignServiceImpl implements AnxinSignService {
//
//    static Logger logger = Logger.getLogger(AnxinSignServiceImpl.class);
//
//
//}
