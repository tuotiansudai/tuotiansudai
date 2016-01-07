package com.tuotiansudai.api.dto;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.util.AutoInvestMonthPeriod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoInvestPeriodDto {
    private String pid;
    private String pName;
    private boolean selected = false;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static List<AutoInvestPeriodDto> generateSelectedAutoInvestPeriodDto(int mergedPeriodsValue){
        final AutoInvestMonthPeriod mergedPeriod = new AutoInvestMonthPeriod(mergedPeriodsValue,"");
        List<AutoInvestMonthPeriod> allPeriods = Arrays.asList(AutoInvestMonthPeriod.AllPeriods);
        List<AutoInvestPeriodDto> selectedPeriods = Lists.transform(allPeriods, new Function<AutoInvestMonthPeriod, AutoInvestPeriodDto>() {

            @Override
            public AutoInvestPeriodDto apply(AutoInvestMonthPeriod input) {
                AutoInvestPeriodDto autoInvestPeriodDto = new AutoInvestPeriodDto();
                autoInvestPeriodDto.setPid("" + input.getPeriodValue());
                autoInvestPeriodDto.setpName("" + input.getPeriodName());
                if(mergedPeriod.contains(Integer.parseInt(autoInvestPeriodDto.getPid()))){
                    autoInvestPeriodDto.setSelected(true);

                }else{
                    autoInvestPeriodDto.setSelected(false);
                }
                return autoInvestPeriodDto;
            }
        });

        return selectedPeriods;
    }
}
