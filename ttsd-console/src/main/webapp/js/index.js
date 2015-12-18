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
    $('.start-date,.end-date').datetimepicker({
        format: 'YYYY-MM-DD',
        maxDate: 'now'
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

    initStartDate=loadEcharts.datetimeFun.getBeforeDate(6);
    initEndDate=loadEcharts.datetimeFun.getBeforeDate(0);

    $('.start-date').val(initStartDate);
    $('.end-date').val(initEndDate);

    function showReport(form,url,reportbox,name,category) {
        var Btn=$(form).find(':button');
        Btn.click(function() {
            var dataFormat=$(form).serialize();
            $.ajax({
                type: 'GET',
                data:dataFormat,
                url: url,
                dataType: 'json'
            }).done(function (data) {

                switch(category){
                    case 'Lines':

                        break;
                }
                var option = loadEcharts.ChartOptionTemplates.Lines(data, name, true),
                    container =document.getElementById(reportbox),
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

    /*用户账户余额时间分布*/
    showReport('#formUserAccountReport','/bi/user-account-trend','userAccountDistribution','用户账户余额(元)','Lines');

    /*用户续投情况*/
    showReport('#formUserInvestViscosityReport','/bi/user-invest-viscosity','userInvestViscosity','用户(人)','bar');

    /*用户投资金额时间分布*/
    showReport('#formUserInvestAmountReport','/bi/user-invest-amount-trend','userInvestAmountDistribution','用户投资金额(元)');

    /*用户投资次数时间分布*/
    showReport('#formUserInvestCountReport','/bi/user-invest-count-trend','userInvestCountDistribution','用户投资次数(次)');

    /*实名认证用户年龄分布*/
    showReport('#formRegisterUserAgeReport','/bi/register-user-age-trend','registerUserAgeDistribution','用户(人)');

    /*投资人用户年龄分布*/
    showReport('#formInvestorUserAgeReport','/bi/investor-user-age-trend','investorUserAgeDistribution','用户(人)');

});
