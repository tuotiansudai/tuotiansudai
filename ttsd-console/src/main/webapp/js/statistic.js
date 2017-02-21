require(['jquery','loadEcharts','bootstrapDatetimepicker'],function($,loadEcharts) {

    var initStartDate,initEndDate,
        $sideNav=$('.sidenav'),
        winScrollTop,
        headerHeight=$('#top').height()+20,
        panelHeight=$('.panel-success').eq(0).height();
    $('.panel-success').eq(0).height();

    $(window).scroll(function() {

        winScrollTop=$(window).scrollTop();
        if(winScrollTop>headerHeight) {
            $sideNav.css({'position':'fixed','left':'15px','top':'10px','width':'14.6%'});
        }
        else {
            $sideNav.removeAttr('style');
        }
    });
    $sideNav.find('li').on('click',function(index) {
        var num=$sideNav.find('li').index(this),heightHack;
        $(this).addClass('active').siblings('li').removeClass('active');
        switch(num) {
            case 1:
                heightHack=20;
                break;
            case 2:
                heightHack=40;
                break;
            case 3:
                heightHack=60;
                break;
            default:
                heightHack=0;
                break;
        }
        $(window).scrollTop(headerHeight+panelHeight*num+heightHack);
        return false;
    });

    var preDate = new Date(new Date().getTime() - 24*60*60*1000);
    $('#repayStartTime').datetimepicker({
        format: 'YYYY-MM-DD',
        minDate : '2016-01-01',
        maxDate : preDate
    });
    $('#repayEndTime').datetimepicker({
        format: 'YYYY-MM-DD',
        minDate : '2016-01-01',
        maxDate : preDate
    });

    $('.start-date,.end-date').datetimepicker({
        format: 'YYYY-MM-DD'
    });

    $('.anxin-start-date,.anxin-end-date').datetimepicker({
        format: 'YYYY-MM-DD',
        minDate : '2016-11-15',
        maxDate : preDate
    });

    loadEcharts.ChartsProvince(function(data) {
        var provinceList=[],i= 0,len=data.length;
        provinceList.push('<option value="">全部省份</option>');
        for(;i<len;i++) {
            provinceList.push('<option value="'+data[i]+'">'+data[i]+'</option>');
        }
        $('select[name="province"]').each(function(index,option) {
            $(option).empty().append(provinceList.join(''));

        });
    });

    loadEcharts.ChartsChannels(function(data) {
        var channelList=[],i= 0,len=data.length;
        channelList.push('<option value="">全部渠道</option>');
        for(;i<len;i++) {
            channelList.push('<option value="'+data[i]+'">'+data[i]+'</option>');
        }
        $('select[name="channel"]').each(function(index,option) {
            $(option).empty().append(channelList.join(''));
        });
    });

    initStartDate=loadEcharts.datetimeFun.getBeforeDate(6);
    initEndDate=loadEcharts.datetimeFun.getBeforeDate(0);
    $('.start-date').val(initStartDate);
    $('.end-date').val(initEndDate);

    $('.anxin-start-date').val("2016-11-15");
    $('.anxin-end-date').val(loadEcharts.datetimeFun.getBeforeDate(0));

    $('#repayStartTime').val('2015-01-01');
    $('#repayEndTime').val(loadEcharts.datetimeFun.getBeforeDate(1));

    $('#expenseStartTime').val('2015-01-01');
    $('#expenseEndTime').val(loadEcharts.datetimeFun.getBeforeDate(1));



    $('.granularity-select').on('change', function(){
        if ($(this).val() == 'Hourly') {
            $(this).parent().find('.start-time-word').html('查询时间');
            $(this).parent().find('.over-end-date').hide();
        } else {
            $(this).parent().find('.start-time-word').html('开始时间');
            $(this).parent().find('.over-end-date').show();
        }
    });

    function showReport(form,url,reportbox,name,category,xAxisName) {
        var Btn=$(form).find(':button');
        Btn.click(function() {
            var dataFormat=$(form).serialize(),
                reportBoxDOM=$('#'+reportbox);
            reportBoxDOM
                .empty()
                .removeAttr('_echarts_instance_')
                .removeAttr('style')
                .css({'width':'100%','height':'400px'})
                .html('<span class="loading-report">加载中...</span>');
            $.ajax({
                type: 'GET',
                data:dataFormat,
                url: url,
                dataType: 'json'
            }).done(function (data) {
                var option;
                if(data.length==0) {
                    reportBoxDOM.html('<span class="loading-report">没有数据</span>');
                    return;
                }
                switch(category){
                    case 'Lines':
                        option = loadEcharts.ChartOptionTemplates.Lines(data, name);
                        break;
                    case 'bar':
                        option = loadEcharts.ChartOptionTemplates.Bar(data, name,xAxisName);
                        break;
                    case 'mBar':
                        option = loadEcharts.ChartOptionTemplates.mBar(data, name,xAxisName);
                        break;
                    case 'kBar':
                        option = loadEcharts.ChartOptionTemplates.kBar(data, name);
                        break;
                    case 'pie':
                        option = loadEcharts.ChartOptionTemplates.Pie(data, name);
                        break;
                    case 'repayLines':
                        option = loadEcharts.ChartOptionTemplates.Lines(data, name);
                        option.title.text = '';
                        break;
                    case 'aBar':
                        option = loadEcharts.ChartOptionTemplates.aBar(data, name,xAxisName);
                        break;
                    default:break;
                }
                var container =reportBoxDOM[0],
                    opt = loadEcharts.ChartConfig(container, option);
                loadEcharts.Charts.RenderChart(opt);

            });
        }).trigger('click');

    }

    /*用户注册时间分布*/
    showReport('#formUserDateReport','/bi/user-register-trend','userDateDistribution','用户(人)','Lines');

    /*用户充值时间分布*/
    showReport('#formUserRechargeReport','/bi/user-recharge-trend','UserRechargeDistribution','用户充值(元)','Lines');

    /*用户提现时间分布*/
    showReport('#formWithdrawReport','/bi/user-withdraw-trend','userWithdrawDistribution','用户提现(元)','Lines');

    /*提现人数分布*/
    showReport('#formWithdrawUserCountReport','/bi/withdraw-user-count-trend','withdrawUserCountDistribution','提现人数(人)','Lines');

    /*用户续投情况*/
    showReport('#formUserInvestViscosityReport','/bi/user-invest-viscosity','userInvestViscosity','投资人数(人)','bar','投资标的数');

    /*用户投资金额时间分布*/
    showReport('#formUserInvestAmountReport','/bi/user-invest-amount-trend','userInvestAmountDistribution','用户投资金额(元)','Lines');

    /*用户投资次数时间分布*/
    showReport('#formUserInvestCountReport','/bi/user-invest-count-trend','userInvestCountDistribution','用户投资(人次)','Lines');

    /*实名认证用户年龄分布*/
    showReport('#formRegisterUserAgeReport','/bi/register-user-age-trend','registerUserAgeDistribution','实名认证用户(人)','pie');

    /*投资用户年龄分布*/
    showReport('#formInvestorUserAgeReport','/bi/investor-user-age-trend','investorUserAgeDistribution','投资用户(人)','pie');

    /*标的资金分布*/
    showReport('#formLoanAmountReport','/bi/loan-amount-distribution','loanAmountDistribution','标的金额(元)','mBar','标的期数');

    /*标的满标周期分布*/
    showReport('#formLoanRaisingTimeCostingReport','/bi/loan-raising-time-costing-trend','loanRaisingTimeCostingDistribution','小时','kBar');

    /*平台待收 总待收-总入金+回款*/
    showReport('#platformSumRepayByTimeReport','/bi/platform-repay-time','platformSumRepayByTimeDistribution','金额','repayLines');

    /*平台支出*/
    showReport('#platformOut','/bi/platform-out','platformOutDistribution','金额','Lines');

    /*安心签用户状态统计*/
    showReport('#anxinUserStatus','/bi/anxin-user-status-statistics','anxinUserStatusStatistics','数量(个)','aBar','用户状态');
});
