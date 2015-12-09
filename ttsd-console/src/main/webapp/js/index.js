require(['jquery','loadEcharts','bootstrapDatetimepicker'],function($,loadEcharts) {

    var initStartDate,initEndDate;
    $('.start-date,.end-date').datetimepicker({
        format: 'YYYY-MM-DD',
        maxDate: 'now'
    });

    loadEcharts.ChartsProvince(function(data) {
        var provinceList=[],i= 0,len=data.length;
        provinceList.push('<option value="">请选择</option>');
        for(;i<len;i++) {
            provinceList.push('<option value="">'+data[i]+'</option>');
        }
        $('select[name="province"]').each(function(index,option) {
            $(option).empty().append(provinceList.join(''));

        });
    });

    initStartDate=loadEcharts.datetimeFun.getBeforeDate(7);
    initEndDate=loadEcharts.datetimeFun.getBeforeDate(0);

    $('.start-date').val(initStartDate);
    $('.end-date').val(initEndDate);

    function showReport(form,url,reportbox,name) {
        var Btn=$(form).find(':button');
        Btn.click(function() {
            var dataFormat=$(form).serialize();
            $.ajax({
                type: 'GET',
                data:dataFormat,
                url: url,
                dataType: 'json'
            }).done(function (data) {
                var option = loadEcharts.ChartOptionTemplates.Lines(data, name, true),
                    container =document.getElementById(reportbox),
                    opt = loadEcharts.ChartConfig(container, option);
                loadEcharts.Charts.RenderChart(opt);
            });
        }).trigger('click');

    }

    /*用户地域分布*/
    showReport('#formUserAreaReport','/bi/user-distribution','userAreaDistribution','用户地域');

    /*用户注册时间分布*/
    showReport('#formUserDateReport','/bi/user-register-trend','userDateDistribution','用户(人)');

    /*用户充值时间分布*/
    showReport('#formUserRechargeReport','/bi/user-recharge-trend','UserRechargeDistribution','用户充值(元)');

    /*用户提现时间分布*/
    showReport('#formWithdrawReport','/bi/user-withdraw-trend','userWithdrawDistribution','用户提现(元)');

    /*用户账户余额时间分布*/
    showReport('#formUserAccountReport','/bi/user-account-trend','userAccountDistribution','用户账户余额(元)');

});
