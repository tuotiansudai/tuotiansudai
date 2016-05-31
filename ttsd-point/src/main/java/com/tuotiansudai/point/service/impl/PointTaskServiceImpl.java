package com.tuotiansudai.point.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.point.dto.PointTaskDto;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PointTaskServiceImpl implements PointTaskService {

    static Logger logger = Logger.getLogger(PointTaskServiceImpl.class);

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    private List<InvestTaskAward> firstInvestAward = null;

    private List<InvestTaskAward> eachInvestAward = null;

    @Override
    @Transactional
    public void completeTask(PointTask pointTask, String loginName) {
        if (isCompletedTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            userPointTaskMapper.create(new UserPointTaskModel(loginName,  pointTaskModel.getId()));
            pointBillService.createPointBill(loginName,pointTaskModel.getId(), PointBusinessType.TASK, pointTaskModel.getPoint());
        }

        logger.debug(MessageFormat.format("{0} has completed task {1}", loginName, pointTask.name()));
    }

    @Override
    public List<PointTaskDto> displayPointTask(int index, int pageSize,final String loginName) {
        List<PointTaskModel> pointTaskModels = pointTaskMapper.findPointTaskPagination((index - 1) * pageSize, pageSize);
        if(CollectionUtils.isEmpty(pointTaskModels)){
            return Lists.newArrayList();
        }

        return Lists.transform(pointTaskModels, new Function<PointTaskModel, PointTaskDto>() {
            @Override
            public PointTaskDto apply(PointTaskModel pointTaskModel) {
                PointTaskDto pointTaskDto = new PointTaskDto(pointTaskModel);
                UserPointTaskModel userPointTaskModel = userPointTaskMapper.findByLoginNameAndId(pointTaskModel.getId(),loginName);
                pointTaskDto.setCompleted(userPointTaskModel != null);
                return pointTaskDto;
            }
        });
    }

    private boolean isCompletedTaskConditions(final PointTask pointTask, String loginName) {
        List<UserPointTaskModel> completedTask = userPointTaskMapper.findByLoginName(loginName);
        boolean isCompleted = Iterators.tryFind(completedTask.iterator(), new Predicate<UserPointTaskModel>() {
            @Override
            public boolean apply(UserPointTaskModel input) {
                return input.getPointTask().getName() == pointTask;
            }
        }).isPresent();

        if (isCompleted) {
            return false;
        }

        switch (pointTask) {
            case REGISTER:
                return accountMapper.findByLoginName(loginName) != null;
            case BIND_EMAIL:
                return !Strings.isNullOrEmpty(userMapper.findByLoginName(loginName).getEmail());
            case BIND_BANK_CARD:
                return bankCardMapper.findPassedBankCardByLoginName(loginName) != null;
            case FIRST_RECHARGE:
                return rechargeMapper.findSumSuccessRechargeByLoginName(loginName) > 0;
            case FIRST_INVEST:
                return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) > 0;
            case SUM_INVEST_10000:
                return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) > 1000000;
        }

        return false;
    }


    @Override
    public void completeNewTask(PointTask pointTask, InvestModel investModel) {
        List<UserPointTaskModel> userPointTaskModelList = isCompletedNewTaskConditions(pointTask,investModel);
        for(UserPointTaskModel userPointTaskModel : userPointTaskModelList){
            if(userPointTaskMapper.findByLoginNameAndIdAndTaskLevel(userPointTaskModel.getLoginName(),userPointTaskModel.getPointTaskId(),userPointTaskModel.getTaskLevel()) != 0){
                continue;
            }
            userPointTaskMapper.create(userPointTaskModel);
            pointBillService.createPointBill(investModel.getLoginName(),userPointTaskModel.getPointTaskId(), PointBusinessType.TASK, userPointTaskModel.getPoint());
        }
        logger.debug(MessageFormat.format("{0} has completed task {1}", investModel.getLoginName(), pointTask.name()));
    }

    private List<UserPointTaskModel> isCompletedNewTaskConditions(final PointTask pointTask, InvestModel investModel) {
        List<UserPointTaskModel> userPointTaskModelList = new ArrayList<>();
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
        List<ReferrerRelationModel> referrerRelationModelList;
        InvestTaskAward investTaskAward;
        switch (pointTask) {
            case EACH_SUM_INVEST:
                long investAmount = investMapper.sumSuccessInvestAmountByLoginName(null,investModel.getLoginName());
                investTaskAward = createGreaterUserPointTask(getEachInvestAward(),investAmount);
                if(investTaskAward != null){
                    userPointTaskModelList.add(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),investTaskAward.getPoint(),investTaskAward.getLevel()));
                }
                break;
            case FIRST_SINGLE_INVEST:
                investTaskAward = compareInvestAward(getFirstInvestAward(),investModel.getAmount());
                if(investTaskAward != null && userPointTaskMapper.findMaxTaskLevelByLoginName(investModel.getLoginName(),pointTaskModel.getId()) != investTaskAward.getLevel()){
                    userPointTaskModelList.add(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),investTaskAward.getPoint(),investTaskAward.getLevel()));
                }
                break;
            case EACH_REFERRER_INVEST:
                referrerRelationModelList = referrerRelationMapper.findByLoginName(investModel.getLoginName());
                if(CollectionUtils.isNotEmpty(referrerRelationModelList) && investModel.getAmount() > 100000){
                    userPointTaskModelList = createUserPointTaskModel(referrerRelationModelList,pointTaskModel,0,userPointTaskModelList);
                }
                break;
            case FIRST_REFERRER_INVEST:
                referrerRelationModelList = referrerRelationMapper.findByLoginName(investModel.getLoginName());
                if(CollectionUtils.isNotEmpty(referrerRelationModelList) && investMapper.sumSuccessInvestAmountByLoginName(null, investModel.getLoginName()) > 0){
                    userPointTaskModelList = createUserPointTaskModel(referrerRelationModelList,pointTaskModel,0,userPointTaskModelList);
                }
                break;
            case FIRST_INVEST_180:
                if(investMapper.countInvestSuccessByProductType(ProductType._180,investModel.getLoginName()) > 0){
                    userPointTaskModelList.add(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),pointTaskModel.getPoint(),0));
                }
            case FIRST_INVEST_360:
                if(investMapper.countInvestSuccessByProductType(ProductType._360,investModel.getLoginName()) > 0){
                    userPointTaskModelList.add(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),pointTaskModel.getPoint(),0));
                }
                break;
        }
        return userPointTaskModelList;
    }

    private List<UserPointTaskModel> createUserPointTaskModel(List<ReferrerRelationModel> referrerRelationModelList,PointTaskModel pointTaskModel,long level,List<UserPointTaskModel> userPointTaskModelList){
        for(ReferrerRelationModel referrerRelationModel : referrerRelationModelList){
            userPointTaskModelList.add(new UserPointTaskModel(referrerRelationModel.getReferrerLoginName(),pointTaskModel.getId(),pointTaskModel.getPoint(),level));
        }
        return userPointTaskModelList;
    }

    private InvestTaskAward createGreaterUserPointTask(List<InvestTaskAward> investTaskAwardList,long investAmount){
        if(investAmount < investTaskAwardList.get(0).getBeginMoney()){
            return null;
        }

        for(InvestTaskAward investTaskAward : investTaskAwardList){
            if(investAmount >= investTaskAward.getBeginMoney() && investAmount < investTaskAward.getEndMoney()){
                return investTaskAward;
            }
        }

        InvestTaskAward investTaskAward = investTaskAwardList.get(0);
        InvestTaskAward newInvestTaskAward;
        boolean flag = true;
        long count = 1;
        while (flag){
            newInvestTaskAward = new InvestTaskAward(investTaskAward.getLevel() + count,
                                                        investTaskAward.getPoint() * count,
                                                        investTaskAward.getEndMoney(),
                                                        investTaskAward.getEndMoney() * count);
            if(investAmount > newInvestTaskAward.getBeginMoney() && investAmount < newInvestTaskAward.getEndMoney()){
                return newInvestTaskAward;
            }
            count ++ ;
        }
        return null;
    }



    private InvestTaskAward compareInvestAward(List<InvestTaskAward> investTaskAwards,long investAmount){
        for(InvestTaskAward investTaskAward : investTaskAwards){
            if(investAmount > investTaskAward.getBeginMoney() && investAmount < investTaskAward.getEndMoney()){
                return investTaskAward;
            }
        }
        return null;
    }

    private List<InvestTaskAward> getFirstInvestAward(){
        if(firstInvestAward == null){
            firstInvestAward = new ArrayList<>();
            firstInvestAward.add(new InvestTaskAward(1,2000,1000000,5000000));
            firstInvestAward.add(new InvestTaskAward(2,5000,5000000,10000000));
            firstInvestAward.add(new InvestTaskAward(3,10000,10000000,20000000));
            firstInvestAward.add(new InvestTaskAward(4,20000,20000000,50000000));
            firstInvestAward.add(new InvestTaskAward(5,50000,50000000,100000000));
            firstInvestAward.add(new InvestTaskAward(6,100000,100000000,1000000000));
        }
        return firstInvestAward;
    }

    private List<InvestTaskAward> getEachInvestAward(){
        if(eachInvestAward == null){
            eachInvestAward = new ArrayList<>();
            eachInvestAward.add(new InvestTaskAward(1,1000,500000,1000000));
            eachInvestAward.add(new InvestTaskAward(2,2000,1000000,5000000));
            eachInvestAward.add(new InvestTaskAward(3,5000,5000000,10000000));
            eachInvestAward.add(new InvestTaskAward(4,10000,10000000,50000000));
            eachInvestAward.add(new InvestTaskAward(5,50000,50000000,100000000));
            eachInvestAward.add(new InvestTaskAward(6,100000,100000000,200000000));
        }
        return eachInvestAward;
    }

    class InvestTaskAward{
        private long level;
        private long point;
        private long beginMoney;
        private long endMoney;

        InvestTaskAward(long level,long point,long beginMoney,long endMoney){
            this.level = level;
            this.point = point;
            this.beginMoney = beginMoney;
            this.endMoney = endMoney;
        }

        public long getLevel() { return level; }

        public void setLevel(long level) { this.level = level; }

        public long getPoint() { return point; }

        public void setPoint(long point) { this.point = point; }

        public long getBeginMoney() { return beginMoney; }

        public long getEndMoney() {return endMoney; }
    }

}
