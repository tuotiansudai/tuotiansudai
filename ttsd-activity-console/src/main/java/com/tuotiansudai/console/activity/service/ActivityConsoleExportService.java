package com.tuotiansudai.console.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.AnnualPrizeDto;
import com.tuotiansudai.activity.repository.dto.AutumnExportDto;
import com.tuotiansudai.activity.repository.dto.NotWorkDto;
import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.UserRegisterInfo;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.ExportCsvUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleExportService {

    @Autowired
    private IPhone7InvestLotteryMapper iPhone7InvestLotteryMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ActivityConsoleNotWorkService activityConsoleNotWorkService;

    @Autowired
    private ActivityConsoleUserLotteryService activityConsoleUserLotteryService;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private ActivityConsoleAnnualService activityConsoleAnnualService;

    @Autowired
    private ActivityConsoleMothersService activityConsoleMothersService;

    @Autowired
    private ActivityConsoleDragonBoatService activityConsoleDragonBoatService;

    @Autowired
    private ActivityConsoleHouseDecorateService activityConsoleHouseDecorateService;

    @Autowired
    private ActivityConsoleIphoneXService activityConsoleIphoneXService;

    @Autowired
    private ActivityConsoleZeroShoppingService activityConsoleZeroShoppingService;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    public List<AutumnExportDto> getAutumnExport() {
        List<AutumnExportDto> autumnExportDtoList = Lists.newArrayList();
        int activityDays = (int) DateUtil.differenceDay(activityAutumnStartTime, activityAutumnEndTime) + 1;
        for (int i = 0; i < activityDays; i++) {
            Date startTime = new DateTime(activityAutumnStartTime).plusDays(i).withTimeAtStartOfDay().toDate();
            Date endTime = new DateTime(activityAutumnStartTime).plusDays(i).withTime(23, 59, 59, 0).toDate();

            Map<String, List<String>> allFamilyAndNum = getAllFamilyMap(activityAutumnStartTime, endTime);

            if (allFamilyAndNum.size() == 0) continue;
            for (Map.Entry<String, List<String>> entry1 : allFamilyAndNum.entrySet()) {
                long totalAmount = 0;
                List<InvestModel> currentHomeInvestModelList = Lists.newArrayList();
                for (String loginName : entry1.getValue()) {
                    totalAmount += investMapper.sumInvestAmount(null, loginName, null, null, null, startTime, endTime, InvestStatus.SUCCESS, null);
                    List<InvestModel> investModelList = investMapper.findSuccessInvestByInvestTime(loginName, true, true, startTime, endTime);
                    if (investModelList == null || investModelList.size() == 0) {
                        InvestModel investModel = new InvestModel();
                        investModel.setLoginName(loginName);
                        investModel.setAmount(0);
                        investModelList.add(investModel);
                    }
                    currentHomeInvestModelList.addAll(investModelList);
                }
                for (InvestModel investModel : currentHomeInvestModelList) {
                    AutumnExportDto autumnExportDto = new AutumnExportDto();
                    autumnExportDto.setName(entry1.getKey());
                    autumnExportDto.setTotalAmount(totalAmount);
                    autumnExportDto.setInvestTime(startTime);
                    if (totalAmount >= 5000000) {
                        autumnExportDto.setPrize("50元红包");
                    } else if (totalAmount >= 2000000 && totalAmount < 5000000) {
                        autumnExportDto.setPrize("15元红包");
                    } else if (totalAmount >= 1000000 && totalAmount < 2000000) {
                        autumnExportDto.setPrize("5元红包");
                    } else {
                        autumnExportDto.setPrize("");
                    }

                    UserModel userModel = userMapper.findByLoginName(investModel.getLoginName());
                    autumnExportDto.setLoginName(userModel == null ? "" : userModel.getMobile());
                    autumnExportDto.setInvestAmount(investModel.getAmount());
                    autumnExportDtoList.add(autumnExportDto);
                }
            }
        }
        return autumnExportDtoList;
    }

    public List<List<String>> buildAutumnList(List<AutumnExportDto> records) {

        Comparator<AutumnExportDto> comparator = new Comparator<AutumnExportDto>() {
            public int compare(AutumnExportDto autumnExportDto1, AutumnExportDto autumnExportDto2) {
                //按日期
                if (!autumnExportDto1.getName().equals(autumnExportDto2.getName())) {
                    //
                    return (int) (autumnExportDto1.getInvestTime().getTime() - autumnExportDto2.getInvestTime().getTime());
                }
                //日期相同按名称
                else if (autumnExportDto1.getName() != autumnExportDto2.getLoginName()) {
                    return autumnExportDto1.getName().compareTo(autumnExportDto2.getName());

                }
                return -1;
            }
        };

        Collections.sort(records, comparator);
        List<List<String>> rows = Lists.newArrayList();


        for (AutumnExportDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getName());
            row.add(AmountConverter.convertCentToString(record.getTotalAmount()));
            row.add(new DateTime(record.getInvestTime()).toString("yyyy-MM-dd"));
            row.add(record.getPrize());
            row.add(record.getLoginName());
            row.add(AmountConverter.convertCentToString(record.getInvestAmount()));
            rows.add(row);
        }
        return rows;

    }

    public List<List<String>> iphone7LotteryStat() {
        List<IPhone7InvestLotteryStatView> list = iPhone7InvestLotteryMapper.allStatInvest();
        return list.stream().map(r -> {
            UserModel userModel = userMapper.findByLoginName(r.getLoginName());
            return Arrays.asList(
                    userModel.getMobile(),
                    userModel.getUserName(),
                    new DecimalFormat("0.00").format(((double) r.getInvestAmountTotal()) / 100),
                    String.valueOf(r.getInvestCount()));
        }).collect(Collectors.toList());
    }

    public List<List<String>> buildPrizeList(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime) {
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, prizeType, startTime, endTime, null, null);
        List<List<String>> rows = Lists.newArrayList();
        userLotteryPrizeViews.forEach(userLotteryPrizeView -> rows.add(Lists.newArrayList(
                Strings.isNullOrEmpty(userLotteryPrizeView.getUserName()) ? "未实名认证" : userLotteryPrizeView.getUserName(),
                userLotteryPrizeView.getMobile(),
                userLotteryPrizeView.getLoginName(),
                new DateTime(userLotteryPrizeView.getLotteryTime()).toString("yyyy-MM-dd"),
                userLotteryPrizeView.getPrize().getDescription())));
        return rows;
    }

    public List<List<String>> buildHeadlineTodayList(String mobile, ActivityCategory prizeType, Date startTime, Date endTime, String authenticationType) {
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, null, prizeType, startTime, endTime, null, null);
        List<List<String>> rows = Lists.newArrayList();
        userLotteryPrizeViews.stream()
                .filter(userLotteryPrizeView -> activityConsoleUserLotteryService.isSpecialAuthType(authenticationType, userLotteryPrizeView))
                .forEach(userLotteryPrizeView -> rows.add(Lists.newArrayList(
                        userLotteryPrizeView.getMobile(),
                        new DateTime(userLotteryPrizeView.getLotteryTime()).toString("yyyy-MM-dd HH:mm:ss"),
                        userLotteryPrizeView.getUserName(),
                        investMapper.sumSuccessInvestCountByLoginName(userLotteryPrizeView.getLoginName()) > 0 ? "是" : "否")));
        return rows;
    }

    private Map<String, List<String>> getAllFamilyMap(Date activityMinAutumnStartTime, Date activityMinAutumnEndTime) {
        List<UserRegisterInfo> userModels = userMapper.findAllUserHasReferrerByRegisterTime(activityMinAutumnStartTime, activityMinAutumnEndTime);

        Map<String, List<String>> allFamily = new LinkedHashMap<>();

        if (userModels.size() == 0) {
            return Maps.newConcurrentMap();
        }

        for (UserRegisterInfo userModel : userModels) {
            if (Strings.isNullOrEmpty(userModel.getReferrer())) {
                allFamily.put(userModel.getLoginName(), Lists.newArrayList(userModel.getLoginName()));
                continue;
            }
            if (allFamily.values().size() == 0) {
                allFamily.put(userModel.getReferrer(), Lists.newArrayList(userModel.getReferrer(), userModel.getLoginName()));
                continue;
            }
            boolean isFamily = false;
            for (List<String> family : allFamily.values()) {
                if (family.contains(userModel.getReferrer())) {
                    isFamily = true;
                    family.add(userModel.getLoginName());
                    break;
                }
            }

            if (!isFamily) {
                allFamily.put(userModel.getReferrer(), Lists.newArrayList(userModel.getReferrer(), userModel.getLoginName()));
            }

        }

        Map<String, List<String>> allFamilyAndNum = new LinkedHashMap<>();
        int num = 0;
        for (String key : allFamily.keySet()) {
            List<String> family = allFamily.get(key);
            if (family.size() == 1) {
                continue;
            }
            num++;
            allFamilyAndNum.put(MessageFormat.format("团员{0}号家庭", String.valueOf(num)), allFamily.get(key));
        }

        return allFamilyAndNum;

    }

    public List<List<String>> buildNotWorkCsvList() {
        //全部导出
        final int index = 1;
        final int pageSize = 1000000;

        List<NotWorkDto> notWorkDtos = activityConsoleNotWorkService.findNotWorkPagination(index, pageSize).getRecords();

        return notWorkDtos.stream().map(ExportCsvUtil::dtoToStringList).collect(Collectors.toList());
    }

    public List<List<String>> buildAnnualCsvList() {
        //全部导出
        final int index = 1;
        final int pageSize = 1000000;
        List<AnnualPrizeDto> annualPrizeDtos = activityConsoleAnnualService.findAnnualList(index, pageSize, null).getRecords();

        return annualPrizeDtos.stream().map(ExportCsvUtil::dtoToStringList).collect(Collectors.toList());
    }

    public List<List<String>> buildMothersDayCsvList() {
        return activityConsoleMothersService.list(1, Integer.MAX_VALUE).getRecords().stream().map(ExportCsvUtil::dtoToStringList).collect(Collectors.toList());
    }

    public List<List<String>> buildDragonBoatCsvList() {
        List<DragonBoatFestivalModel> list = activityConsoleDragonBoatService.getList(1, Integer.MAX_VALUE).getRecords();

        return list.stream().map(a -> a.convertToView()).map(ExportCsvUtil::dtoToStringList).collect(Collectors.toList());
    }

    public List<List<String>> buildHouseDecorateCsvList(){
        return activityConsoleHouseDecorateService.list(1,Integer.MAX_VALUE).getRecords().stream().map(ExportCsvUtil::dtoToStringList).collect(Collectors.toList());
    }

    public List<List<String>> buildIphoneXCsvList(){
        return activityConsoleIphoneXService.list(1,Integer.MAX_VALUE).getRecords().stream().map(ExportCsvUtil::dtoToStringList).collect(Collectors.toList());
    }

    public List<List<String>> buildZeroShoppingCsvList() {
        return activityConsoleZeroShoppingService.userPrizeList(1, Integer.MAX_VALUE, null, null, null).getRecords().stream().map(ExportCsvUtil::dtoToStringList).collect(Collectors.toList());
    }
}
