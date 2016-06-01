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
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

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
        switch (pointTask) {
            case EACH_SUM_INVEST:
                long investAmount = investMapper.sumSuccessInvestAmountByLoginName(null,investModel.getLoginName());
                userPointTaskModelList = createGreaterUserPointTask(getEachInvestAward(),investAmount,userPointTaskModelList,pointTaskModel,investModel);
                break;
            case FIRST_SINGLE_INVEST:
                userPointTaskModelList = compareInvestAward(getFirstInvestAward(),investModel.getAmount(),userPointTaskModelList,pointTaskModel,investModel);
                break;
            case EACH_REFERRER_INVEST:
                referrerRelationModelList = referrerRelationMapper.findByLoginName(investModel.getLoginName());
                if(CollectionUtils.isNotEmpty(referrerRelationModelList) && investModel.getAmount() > 100000){
                    long awardCount = investModel.getAmount()/100000;
                    if(awardCount > 0){
                        Map<String,Long> pointTaskLevel = new HashedMap();
                        for(int i = 0; i < awardCount; i ++){
                            for(ReferrerRelationModel referrerRelationModel : referrerRelationModelList){
                                long level = getMaxTaskLevel(pointTaskLevel,referrerRelationModel.getReferrerLoginName(),pointTaskModel.getId());
                                userPointTaskModelList.add(new UserPointTaskModel(referrerRelationModel.getReferrerLoginName(),pointTaskModel.getId(),pointTaskModel.getPoint(),level));
                            }
                        }
                    }
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

    private long getMaxTaskLevel(Map<String,Long> pointTaskLevel,String referrerLoginName,long taskId){
        if(pointTaskLevel.get(referrerLoginName) == null){
            pointTaskLevel.put(referrerLoginName,userPointTaskMapper.findMaxTaskLevelByLoginName(referrerLoginName,taskId));
        }
        pointTaskLevel.put(referrerLoginName,pointTaskLevel.get(referrerLoginName) + 1);
        return pointTaskLevel.get(referrerLoginName);
    }

    private List<UserPointTaskModel> createUserPointTaskModel(List<ReferrerRelationModel> referrerRelationModelList,PointTaskModel pointTaskModel,long level,List<UserPointTaskModel> userPointTaskModelList){
        for(ReferrerRelationModel referrerRelationModel : referrerRelationModelList){
            userPointTaskModelList.add(new UserPointTaskModel(referrerRelationModel.getReferrerLoginName(),pointTaskModel.getId(),pointTaskModel.getPoint(),level));
        }
        return userPointTaskModelList;
    }

    private List<UserPointTaskModel> createGreaterUserPointTask(List<InvestTaskAward> investTaskAwardList,long investAmount,List<UserPointTaskModel> userPointTaskModelList,PointTaskModel pointTaskModel,InvestModel investModel){
        if(investAmount < investTaskAwardList.get(0).getInvestMoney()){
            return userPointTaskModelList;
        }
        boolean isMaxInvestMoney = true;
        for(InvestTaskAward investTaskAward : investTaskAwardList){
            if(investAmount >= investTaskAward.getInvestMoney()){
                userPointTaskModelList.add(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),investTaskAward.getPoint(),investTaskAward.getLevel()));
            }else{
                isMaxInvestMoney = false;
                break;
            }
        }

        if(isMaxInvestMoney){
            InvestTaskAward newInvestTaskAward = investTaskAwardList.get(investTaskAwardList.size() - 1);
            while (isMaxInvestMoney){
                newInvestTaskAward = new InvestTaskAward(newInvestTaskAward.getLevel() + 1,
                        newInvestTaskAward.getPoint() + newInvestTaskAward.getPoint(),
                        newInvestTaskAward.getInvestMoney() + newInvestTaskAward.getInvestMoney());
                if(investAmount > newInvestTaskAward.getInvestMoney()){
                    userPointTaskModelList.add(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),newInvestTaskAward.getPoint(),newInvestTaskAward.getLevel()));
                    continue;
                }
                isMaxInvestMoney = false;
            }
        }

        return userPointTaskModelList;
    }



    private List<UserPointTaskModel> compareInvestAward(List<InvestTaskAward> investTaskAwards,long investAmount,List<UserPointTaskModel> userPointTaskModelList,PointTaskModel pointTaskModel,InvestModel investModel){
        for(InvestTaskAward investTaskAward : investTaskAwards){
            if(investAmount >= investTaskAward.getInvestMoney()){
                userPointTaskModelList.add(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),investTaskAward.getPoint(),investTaskAward.getLevel()));
            }else{
                break;
            }
        }
        return userPointTaskModelList;
    }

    private List<InvestTaskAward> getFirstInvestAward(){
        if(firstInvestAward == null){
            firstInvestAward = new ArrayList<>();
            firstInvestAward.add(new InvestTaskAward(1,2000,1000000));
            firstInvestAward.add(new InvestTaskAward(2,5000,5000000));
            firstInvestAward.add(new InvestTaskAward(3,10000,10000000));
            firstInvestAward.add(new InvestTaskAward(4,20000,20000000));
            firstInvestAward.add(new InvestTaskAward(5,50000,50000000));
            firstInvestAward.add(new InvestTaskAward(6,100000,100000000));
        }
        return firstInvestAward;
    }

    private List<InvestTaskAward> getEachInvestAward(){
        if(eachInvestAward == null){
            eachInvestAward = new ArrayList<>();
            eachInvestAward.add(new InvestTaskAward(1,1000,500000));
            eachInvestAward.add(new InvestTaskAward(2,2000,1000000));
            eachInvestAward.add(new InvestTaskAward(3,5000,5000000));
            eachInvestAward.add(new InvestTaskAward(4,10000,10000000));
            eachInvestAward.add(new InvestTaskAward(5,50000,50000000));
            eachInvestAward.add(new InvestTaskAward(6,100000,100000000));
        }
        return eachInvestAward;
    }

    class InvestTaskAward{
        private long level;
        private long point;
        private long investMoney;

        InvestTaskAward(long level,long point,long investMoney){
            this.level = level;
            this.point = point;
            this.investMoney = investMoney;
        }

        public long getLevel() { return level; }

        public void setLevel(long level) { this.level = level; }

        public long getPoint() { return point; }

        public void setPoint(long point) { this.point = point; }

        public long getInvestMoney() { return investMoney; }
    }

}
