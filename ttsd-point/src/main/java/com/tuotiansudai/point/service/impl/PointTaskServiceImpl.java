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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    private boolean isNewCompletedTaskConditions(final PointTask pointTask, String loginName, InvestModel investModel) {
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

        PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
        List<ReferrerRelationModel> referrerRelationModelList;
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
            case EVERT_SUM_INVEST:
                return false;
            case FIRST_SINGLE_INVEST:
                InvestTaskAward investTaskAward = getFirstInvestAwardLevel(investModel.getAmount());
                return false;
            case EVERY_INVITE_INVEST:
                referrerRelationModelList = referrerRelationMapper.findByLoginName(loginName);
                if(CollectionUtils.isNotEmpty(referrerRelationModelList) && investModel.getAmount() > 1000){
                    createUserPointTaskModel(referrerRelationModelList,pointTaskModel,0);
                }
                return true;
            case FIRST_INVITE_FRIEND:
                referrerRelationModelList = referrerRelationMapper.findByLoginName(loginName);
                if(CollectionUtils.isNotEmpty(referrerRelationModelList) && investMapper.sumSuccessInvestAmountByLoginName(null, loginName) > 0){
                    createUserPointTaskModel(referrerRelationModelList,pointTaskModel,0);
                }
                return true;
            case FIRST_INVEST_180D:
                if(investMapper.countInvestSuccessByProductType(ProductType._180,investModel.getLoginName()) > 0){
                    userPointTaskMapper.create(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),pointTaskModel.getPoint(),0));
                }
                return false;
            case FIRST_INVEST_360D:
                if(investMapper.countInvestSuccessByProductType(ProductType._360,investModel.getLoginName()) > 0){
                    userPointTaskMapper.create(new UserPointTaskModel(investModel.getLoginName(),pointTaskModel.getId(),pointTaskModel.getPoint(),0));
                }
                return false;

        }

        return false;
    }

    private void createUserPointTaskModel(List<ReferrerRelationModel> referrerRelationModelList,PointTaskModel pointTaskModel,long level){
        for(ReferrerRelationModel referrerRelationModel : referrerRelationModelList){
            userPointTaskMapper.create(new UserPointTaskModel(referrerRelationModel.getReferrerLoginName(),pointTaskModel.getId(),pointTaskModel.getPoint(),level));
        }
    }

    private InvestTaskAward getFirstInvestAwardLevel(long investAmount){
        if(investAmount > 0){
            firstInvestAward = getFirstInvestAward();
            for(InvestTaskAward investTaskAward : firstInvestAward){
                if(investAmount > investTaskAward.getBeginMoney() && investAmount < investTaskAward.getEndMoney()){
                    return investTaskAward;
                }
            }
        }
        return null;
    }

    private List<InvestTaskAward> getFirstInvestAward(){
        if(firstInvestAward == null){
            firstInvestAward = new ArrayList<>();
            firstInvestAward.add(new InvestTaskAward(1,2000,10000,50000));
            firstInvestAward.add(new InvestTaskAward(2,5000,50000,100000));
            firstInvestAward.add(new InvestTaskAward(3,10000,100000,200000));
            firstInvestAward.add(new InvestTaskAward(4,20000,200000,500000));
            firstInvestAward.add(new InvestTaskAward(5,50000,500000,1000000));
            firstInvestAward.add(new InvestTaskAward(6,100000,1000000,10000000));
        }
        return firstInvestAward;
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

        public void setBeginMoney(long beginMoney) { this.beginMoney = beginMoney; }

        public long getEndMoney() {return endMoney; }

        public void setEndMoney(long endMoney) { this.endMoney = endMoney; }
    }

}
