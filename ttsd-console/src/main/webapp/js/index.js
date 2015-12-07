require(['jquery','loadEcharts','bootstrapDatetimepicker'],function($,loadEcharts) {

    $('.start-date,.end-date').datetimepicker({
        format: 'YYYY-MM-DD',
        maxDate: 'now'
    });

    var $formUserAreaReport=$('#formUserAreaReport'),
        $UserAreaCategory=$('.search-category',$formUserAreaReport),
        $UserAreaStartDate=$('.start-date',$formUserAreaReport),
        $UserAreaEndDate=$('.end-date',$formUserAreaReport),
        $UserAreaBtn=$(':button',$formUserAreaReport),
        UserAreaCategory,dataUserArea;

    var $formUserDateReport=$('#formUserDateReport'),
        $UserDateCategory=$('.search-category',$formUserDateReport),
        $UserDateStartDate=$('.start-date',$formUserDateReport),
        $UserDateEndDate=$('.end-date',$formUserDateReport),
        $UserDateBtn=$(':button',$formUserDateReport),
        UserDateCategory,dataUserDate;

    var $userRechargeReport=$('#formUserRechargeReport'),
        $userRechargeCategory=$('.search-category',$userRechargeReport),
        $userRechargeStartDate=$('.start-date',$userRechargeReport),
        $userRechargeEndDate=$('.end-date',$userRechargeReport),
        $userRechargeBtn=$(':button',$userRechargeReport),
        UserRechargeCategory,dataUserRecharge;

    var $WithdrawReport=$('#formWithdrawReport'),
        $WithdrawCategory=$('.search-category',$WithdrawReport),
        $WithdrawStartDate=$('.start-date',$WithdrawReport),
        $WithdrawEndDate=$('.end-date',$WithdrawReport),
        $WithdrawBtn=$(':button',$WithdrawReport),
        WithdrawCategory,dataWithdraw;

    var $UserAccountReport=$('#formUserAccountReport'),
        $UserAccountCategory=$('.search-category',$UserAccountReport),
        $UserAccountStartDate=$('.start-date',$UserAccountReport),
        $UserAccountEndDate=$('.end-date',$UserAccountReport),
        $UserAccountBtn=$(':button',$UserAccountReport),
        UserAccountCategory,dataUserAccount;

    /*用户地域分布*/
    $UserAreaCategory.change(function() {
        UserAreaCategory=$UserAreaCategory.val();
        if(!UserAreaCategory) {
            UserAreaCategory='D';
        }
        switch(UserAreaCategory) {
            case 'D':
                $UserDateStartDate.attr('type','date');
                $UserDateEndDate.attr('type','date');
                dataUserArea=[
                    { name: '2014-01-01', value: 200, group: '北京' },
                    { name: '2014-01-01', value: 400, group: '天津' },
                    { name: '2014-01-01', value: 350, group: '上海' },
                    { name: '2014-01-01', value: 260, group: '重庆' },
                    { name: '2014-01-01', value: 185, group: '河北' },
                    { name: '2014-01-01', value: 140, group: '其他' },

                    { name: '2014-01-02', value: 282, group: '北京' },
                    { name: '2014-01-02', value: 150, group: '天津' },
                    { name: '2014-01-02', value: 450, group: '上海' },
                    { name: '2014-01-02', value: 350, group: '重庆' },
                    { name: '2014-01-02', value: 150, group: '河北' },
                    { name: '2014-01-02', value: 240, group: '其他' },

                    { name: '2014-01-03', value: 200, group: '北京' },
                    { name: '2014-01-03', value: 240, group: '天津' },
                    { name: '2014-01-03', value: 160, group: '上海' },
                    { name: '2014-01-03', value: 346, group: '重庆' },
                    { name: '2014-01-03', value: 357, group: '河北' },
                    { name: '2014-01-03', value: 764, group: '其他' },

                    { name: '2014-01-04', value: 457, group: '北京' },
                    { name: '2014-01-04', value: 246, group: '天津' },
                    { name: '2014-01-04', value: 369, group: '上海' },
                    { name: '2014-01-04', value: 168, group: '重庆' },
                    { name: '2014-01-04', value: 375, group: '河北' },
                    { name: '2014-01-04', value: 448, group: '其他' },

                    { name: '2014-01-05', value: 568, group: '北京' },
                    { name: '2014-01-05', value: 680, group: '天津' },
                    { name: '2014-01-05', value: 256, group: '上海' },
                    { name: '2014-01-05', value: 108, group: '重庆' },
                    { name: '2014-01-05', value: 160, group: '河北' },
                    { name: '2014-01-05', value: 367, group: '其他' }
                ];
                break;
            case 'W':
                $UserDateStartDate.attr('type','week');
                $UserDateEndDate.attr('type','week');
                dataUserDate=[
                    { name: '周一', value: 600, group: '北京' },
                    { name: '周一', value: 580, group: '天津' },
                    { name: '周一', value: 680, group: '上海' },
                    { name: '周一', value: 468, group: '重庆' },
                    { name: '周一', value: 236, group: '河北' },
                    { name: '周一', value: 452, group: '其他' },

                    { name: '周二', value: 568, group: '北京' },
                    { name: '周二', value: 458, group: '天津' },
                    { name: '周二', value: 380, group: '上海' },
                    { name: '周二', value: 689, group: '重庆' },
                    { name: '周二', value: 454, group: '河北' },
                    { name: '周二', value: 279, group: '其他' },

                    { name: '周三', value: 478, group: '北京' },
                    { name: '周三', value: 278, group: '天津' },
                    { name: '周三', value: 364, group: '上海' },
                    { name: '周三', value: 108, group: '重庆' },
                    { name: '周三', value: 278, group: '河北' },
                    { name: '周三', value: 150, group: '其他' },

                    { name: '周四', value: 453, group: '北京' },
                    { name: '周四', value: 235, group: '天津' },
                    { name: '周四', value: 568, group: '上海' },
                    { name: '周四', value: 257, group: '重庆' },
                    { name: '周四', value: 473, group: '河北' },
                    { name: '周四', value: 275, group: '其他' },

                    { name: '周五', value: 245, group: '北京' },
                    { name: '周五', value: 143, group: '天津' },
                    { name: '周五', value: 235, group: '上海' },
                    { name: '周五', value: 654, group: '重庆' },
                    { name: '周五', value: 356, group: '河北' },
                    { name: '周五', value: 265, group: '其他' },

                    { name: '周六', value: 356, group: '北京' },
                    { name: '周六', value: 346, group: '天津' },
                    { name: '周六', value: 432, group: '上海' },
                    { name: '周六', value: 643, group: '重庆' },
                    { name: '周六', value: 265, group: '河北' },
                    { name: '周六', value: 245, group: '其他' },

                    { name: '周日', value: 154, group: '北京' },
                    { name: '周日', value: 234, group: '天津' },
                    { name: '周日', value: 543, group: '上海' },
                    { name: '周日', value: 245, group: '重庆' },
                    { name: '周日', value: 362, group: '河北' },
                    { name: '周日', value: 263, group: '其他' }
                ];
                break;
            case 'M':
                $UserDateStartDate.attr('type','month');
                $UserDateEndDate.attr('type','month');
                dataUserDate=[
                    { name: '1月', value: 1000, group: '北京' },
                    { name: '1月', value: 2353, group: '天津' },
                    { name: '1月', value: 2311, group: '上海' },
                    { name: '1月', value: 1780, group: '重庆' },
                    { name: '1月', value: 800, group: '河北' },
                    { name: '1月', value: 1075, group: '其他' },

                    { name: '2月', value: 2853, group: '北京' },
                    { name: '2月', value: 2421, group: '天津' },
                    { name: '2月', value: 1643, group: '上海' },
                    { name: '2月', value: 1432, group: '重庆' },
                    { name: '2月', value: 1643, group: '河北' },
                    { name: '2月', value: 1643, group: '其他' },

                    { name: '3月', value: 1843, group: '北京' },
                    { name: '3月', value: 2000, group: '天津' },
                    { name: '3月', value: 2421, group: '上海' },
                    { name: '3月', value: 1453, group: '重庆' },
                    { name: '3月', value: 1642, group: '河北' },
                    { name: '3月', value: 1307, group: '其他' },

                    { name: '4月', value: 3245, group: '北京' },
                    { name: '4月', value: 2432, group: '天津' },
                    { name: '4月', value: 1896, group: '上海' },
                    { name: '4月', value: 1653, group: '重庆' },
                    { name: '4月', value: 2654, group: '河北' },
                    { name: '4月', value: 1065, group: '其他' },

                    { name: '5月', value: 2433, group: '北京' },
                    { name: '5月', value: 1643, group: '天津' },
                    { name: '5月', value: 1685, group: '上海' },
                    { name: '5月', value: 1567, group: '重庆' },
                    { name: '5月', value: 1489, group: '河北' },
                    { name: '5月', value: 1435, group: '其他' }
                ];
                break;
        }
    }).trigger('change');
    $UserAreaBtn.click(function() {
        var dataFormat={"startDate":$UserAreaStartDate.val(),"endDate":$UserAreaEndDate.val()};
        //$.ajax({
        //    type: 'POST',
        //    data:dataFormat,
        //    url: url,
        //    dataType: 'json'
        //}).done(function (data) {
        //
        //
        //});

        var optionUserArea = loadEcharts.ChartOptionTemplates.Lines(dataUserArea, '用户地域', true),
            containerUserArea = $("#userAreaDistribution")[0],
            optUserArea = loadEcharts.ChartConfig(containerUserArea, optionUserArea);
        loadEcharts.Charts.RenderChart(optUserArea);
    }).trigger('click');

    /*用户时间分布*/

    $UserDateCategory.change(function() {

        UserDateCategory=$UserDateCategory.val();
        if(!UserDateCategory) {
            UserDateCategory='D';
        }
        switch(UserDateCategory) {
            case 'D':
                dataUserDate=[
                    { name: '2014-01-01', value: 200, group: '北京' },
                    { name: '2014-01-01', value: 400, group: '天津' },
                    { name: '2014-01-01', value: 350, group: '上海' },
                    { name: '2014-01-01', value: 260, group: '重庆' },
                    { name: '2014-01-01', value: 185, group: '河北' },
                    { name: '2014-01-01', value: 140, group: '其他' },

                    { name: '2014-01-02', value: 282, group: '北京' },
                    { name: '2014-01-02', value: 150, group: '天津' },
                    { name: '2014-01-02', value: 450, group: '上海' },
                    { name: '2014-01-02', value: 350, group: '重庆' },
                    { name: '2014-01-02', value: 150, group: '河北' },
                    { name: '2014-01-02', value: 240, group: '其他' },

                    { name: '2014-01-03', value: 200, group: '北京' },
                    { name: '2014-01-03', value: 240, group: '天津' },
                    { name: '2014-01-03', value: 160, group: '上海' },
                    { name: '2014-01-03', value: 346, group: '重庆' },
                    { name: '2014-01-03', value: 357, group: '河北' },
                    { name: '2014-01-03', value: 764, group: '其他' },

                    { name: '2014-01-04', value: 457, group: '北京' },
                    { name: '2014-01-04', value: 246, group: '天津' },
                    { name: '2014-01-04', value: 369, group: '上海' },
                    { name: '2014-01-04', value: 168, group: '重庆' },
                    { name: '2014-01-04', value: 375, group: '河北' },
                    { name: '2014-01-04', value: 448, group: '其他' },

                    { name: '2014-01-05', value: 568, group: '北京' },
                    { name: '2014-01-05', value: 680, group: '天津' },
                    { name: '2014-01-05', value: 256, group: '上海' },
                    { name: '2014-01-05', value: 108, group: '重庆' },
                    { name: '2014-01-05', value: 160, group: '河北' },
                    { name: '2014-01-05', value: 367, group: '其他' }
                ];
                break;
            case 'W':
                dataUserDate=[
                    { name: '周一', value: 600, group: '北京' },
                    { name: '周一', value: 580, group: '天津' },
                    { name: '周一', value: 680, group: '上海' },
                    { name: '周一', value: 468, group: '重庆' },
                    { name: '周一', value: 236, group: '河北' },
                    { name: '周一', value: 452, group: '其他' },

                    { name: '周二', value: 568, group: '北京' },
                    { name: '周二', value: 458, group: '天津' },
                    { name: '周二', value: 380, group: '上海' },
                    { name: '周二', value: 689, group: '重庆' },
                    { name: '周二', value: 454, group: '河北' },
                    { name: '周二', value: 279, group: '其他' },

                    { name: '周三', value: 478, group: '北京' },
                    { name: '周三', value: 278, group: '天津' },
                    { name: '周三', value: 364, group: '上海' },
                    { name: '周三', value: 108, group: '重庆' },
                    { name: '周三', value: 278, group: '河北' },
                    { name: '周三', value: 150, group: '其他' },

                    { name: '周四', value: 453, group: '北京' },
                    { name: '周四', value: 235, group: '天津' },
                    { name: '周四', value: 568, group: '上海' },
                    { name: '周四', value: 257, group: '重庆' },
                    { name: '周四', value: 473, group: '河北' },
                    { name: '周四', value: 275, group: '其他' },

                    { name: '周五', value: 245, group: '北京' },
                    { name: '周五', value: 143, group: '天津' },
                    { name: '周五', value: 235, group: '上海' },
                    { name: '周五', value: 654, group: '重庆' },
                    { name: '周五', value: 356, group: '河北' },
                    { name: '周五', value: 265, group: '其他' },

                    { name: '周六', value: 356, group: '北京' },
                    { name: '周六', value: 346, group: '天津' },
                    { name: '周六', value: 432, group: '上海' },
                    { name: '周六', value: 643, group: '重庆' },
                    { name: '周六', value: 265, group: '河北' },
                    { name: '周六', value: 245, group: '其他' },

                    { name: '周日', value: 154, group: '北京' },
                    { name: '周日', value: 234, group: '天津' },
                    { name: '周日', value: 543, group: '上海' },
                    { name: '周日', value: 245, group: '重庆' },
                    { name: '周日', value: 362, group: '河北' },
                    { name: '周日', value: 263, group: '其他' }
                ];
                break;
            case 'M':
                dataUserDate=[
                    { name: '1月', value: 1000, group: '北京' },
                    { name: '1月', value: 2353, group: '天津' },
                    { name: '1月', value: 2311, group: '上海' },
                    { name: '1月', value: 1780, group: '重庆' },
                    { name: '1月', value: 800, group: '河北' },
                    { name: '1月', value: 1075, group: '其他' },

                    { name: '2月', value: 2853, group: '北京' },
                    { name: '2月', value: 2421, group: '天津' },
                    { name: '2月', value: 1643, group: '上海' },
                    { name: '2月', value: 1432, group: '重庆' },
                    { name: '2月', value: 1643, group: '河北' },
                    { name: '2月', value: 1643, group: '其他' },

                    { name: '3月', value: 1843, group: '北京' },
                    { name: '3月', value: 2000, group: '天津' },
                    { name: '3月', value: 2421, group: '上海' },
                    { name: '3月', value: 1453, group: '重庆' },
                    { name: '3月', value: 1642, group: '河北' },
                    { name: '3月', value: 1307, group: '其他' },

                    { name: '4月', value: 3245, group: '北京' },
                    { name: '4月', value: 2432, group: '天津' },
                    { name: '4月', value: 1896, group: '上海' },
                    { name: '4月', value: 1653, group: '重庆' },
                    { name: '4月', value: 2654, group: '河北' },
                    { name: '4月', value: 1065, group: '其他' },

                    { name: '5月', value: 2433, group: '北京' },
                    { name: '5月', value: 1643, group: '天津' },
                    { name: '5月', value: 1685, group: '上海' },
                    { name: '5月', value: 1567, group: '重庆' },
                    { name: '5月', value: 1489, group: '河北' },
                    { name: '5月', value: 1435, group: '其他' }
                ];
                break;
        }
    }).trigger('change');
    $UserDateBtn.click(function() {

        var dataFormat={"startDate":$UserDateStartDate.val(),"endDate":$UserAreaEndDate.val()};

        var optionUserDate = loadEcharts.ChartOptionTemplates.Lines(dataUserDate, '用户(人)', true),
            containerUserDate = $("#userDateDistribution")[0],
            optUserDate = loadEcharts.ChartConfig(containerUserDate, optionUserDate);
            loadEcharts.Charts.RenderChart(optUserDate);

        //$.ajax({
        //        type: 'POST',
        //        data:dataFormat,
        //        url: url,
        //        dataType: 'json'
        //    }).done(function (data) {
        //});



    }).trigger('click');

    /*用户充值时间分布*/
    $userRechargeCategory.change(function() {
        UserRechargeCategory=$userRechargeCategory.val();
        if(!UserRechargeCategory) {
            UserRechargeCategory='D';
        }
        switch(UserRechargeCategory) {
            case 'D':
                dataUserRecharge=[
                    { name: '2014-01-01', value: 200, group: '北京' },
                    { name: '2014-01-01', value: 400, group: '天津' },
                    { name: '2014-01-01', value: 350, group: '上海' },
                    { name: '2014-01-01', value: 260, group: '重庆' },
                    { name: '2014-01-01', value: 185, group: '河北' },
                    { name: '2014-01-01', value: 140, group: '其他' },

                    { name: '2014-01-02', value: 282, group: '北京' },
                    { name: '2014-01-02', value: 150, group: '天津' },
                    { name: '2014-01-02', value: 450, group: '上海' },
                    { name: '2014-01-02', value: 350, group: '重庆' },
                    { name: '2014-01-02', value: 150, group: '河北' },
                    { name: '2014-01-02', value: 240, group: '其他' },

                    { name: '2014-01-03', value: 200, group: '北京' },
                    { name: '2014-01-03', value: 240, group: '天津' },
                    { name: '2014-01-03', value: 160, group: '上海' },
                    { name: '2014-01-03', value: 346, group: '重庆' },
                    { name: '2014-01-03', value: 357, group: '河北' },
                    { name: '2014-01-03', value: 764, group: '其他' },

                    { name: '2014-01-04', value: 457, group: '北京' },
                    { name: '2014-01-04', value: 246, group: '天津' },
                    { name: '2014-01-04', value: 369, group: '上海' },
                    { name: '2014-01-04', value: 168, group: '重庆' },
                    { name: '2014-01-04', value: 375, group: '河北' },
                    { name: '2014-01-04', value: 448, group: '其他' },

                    { name: '2014-01-05', value: 568, group: '北京' },
                    { name: '2014-01-05', value: 680, group: '天津' },
                    { name: '2014-01-05', value: 256, group: '上海' },
                    { name: '2014-01-05', value: 108, group: '重庆' },
                    { name: '2014-01-05', value: 160, group: '河北' },
                    { name: '2014-01-05', value: 367, group: '其他' }
                ];
                break;
            case 'W':
                dataUserRecharge=[
                    { name: '周一', value: 600, group: '北京' },
                    { name: '周一', value: 580, group: '天津' },
                    { name: '周一', value: 680, group: '上海' },
                    { name: '周一', value: 468, group: '重庆' },
                    { name: '周一', value: 236, group: '河北' },
                    { name: '周一', value: 452, group: '其他' },

                    { name: '周二', value: 568, group: '北京' },
                    { name: '周二', value: 458, group: '天津' },
                    { name: '周二', value: 380, group: '上海' },
                    { name: '周二', value: 689, group: '重庆' },
                    { name: '周二', value: 454, group: '河北' },
                    { name: '周二', value: 279, group: '其他' },

                    { name: '周三', value: 478, group: '北京' },
                    { name: '周三', value: 278, group: '天津' },
                    { name: '周三', value: 364, group: '上海' },
                    { name: '周三', value: 108, group: '重庆' },
                    { name: '周三', value: 278, group: '河北' },
                    { name: '周三', value: 150, group: '其他' },

                    { name: '周四', value: 453, group: '北京' },
                    { name: '周四', value: 235, group: '天津' },
                    { name: '周四', value: 568, group: '上海' },
                    { name: '周四', value: 257, group: '重庆' },
                    { name: '周四', value: 473, group: '河北' },
                    { name: '周四', value: 275, group: '其他' },

                    { name: '周五', value: 245, group: '北京' },
                    { name: '周五', value: 143, group: '天津' },
                    { name: '周五', value: 235, group: '上海' },
                    { name: '周五', value: 654, group: '重庆' },
                    { name: '周五', value: 356, group: '河北' },
                    { name: '周五', value: 265, group: '其他' },

                    { name: '周六', value: 356, group: '北京' },
                    { name: '周六', value: 346, group: '天津' },
                    { name: '周六', value: 432, group: '上海' },
                    { name: '周六', value: 643, group: '重庆' },
                    { name: '周六', value: 265, group: '河北' },
                    { name: '周六', value: 245, group: '其他' },

                    { name: '周日', value: 154, group: '北京' },
                    { name: '周日', value: 234, group: '天津' },
                    { name: '周日', value: 543, group: '上海' },
                    { name: '周日', value: 245, group: '重庆' },
                    { name: '周日', value: 362, group: '河北' },
                    { name: '周日', value: 263, group: '其他' }
                ];
                break;
            case 'M':
                dataUserRecharge=[
                    { name: '1月', value: 1000, group: '北京' },
                    { name: '1月', value: 2353, group: '天津' },
                    { name: '1月', value: 2311, group: '上海' },
                    { name: '1月', value: 1780, group: '重庆' },
                    { name: '1月', value: 800, group: '河北' },
                    { name: '1月', value: 1075, group: '其他' },

                    { name: '2月', value: 2853, group: '北京' },
                    { name: '2月', value: 2421, group: '天津' },
                    { name: '2月', value: 1643, group: '上海' },
                    { name: '2月', value: 1432, group: '重庆' },
                    { name: '2月', value: 1643, group: '河北' },
                    { name: '2月', value: 1643, group: '其他' },

                    { name: '3月', value: 1843, group: '北京' },
                    { name: '3月', value: 2000, group: '天津' },
                    { name: '3月', value: 2421, group: '上海' },
                    { name: '3月', value: 1453, group: '重庆' },
                    { name: '3月', value: 1642, group: '河北' },
                    { name: '3月', value: 1307, group: '其他' },

                    { name: '4月', value: 3245, group: '北京' },
                    { name: '4月', value: 2432, group: '天津' },
                    { name: '4月', value: 1896, group: '上海' },
                    { name: '4月', value: 1653, group: '重庆' },
                    { name: '4月', value: 2654, group: '河北' },
                    { name: '4月', value: 1065, group: '其他' },

                    { name: '5月', value: 2433, group: '北京' },
                    { name: '5月', value: 1643, group: '天津' },
                    { name: '5月', value: 1685, group: '上海' },
                    { name: '5月', value: 1567, group: '重庆' },
                    { name: '5月', value: 1489, group: '河北' },
                    { name: '5月', value: 1435, group: '其他' }
                ];
                break;
        }
    }).trigger('change');
    $userRechargeBtn.click(function() {
        var dataFormat={"startDate":$userRechargeStartDate.val(),"endDate":$userRechargeEndDate.val()};

        var option = loadEcharts.ChartOptionTemplates.Lines(dataUserRecharge, '用户充值(元)', true),
            container = $("#UserRechargeDistribution")[0],
            opt = loadEcharts.ChartConfig(container, option);
        loadEcharts.Charts.RenderChart(opt);
    }).trigger('click');

    /*用户提现时间分布*/
    $WithdrawCategory.change(function() {
        WithdrawCategory=$WithdrawCategory.val();
        if(!WithdrawCategory) {
            WithdrawCategory='D';
        }
        switch(WithdrawCategory) {
            case 'D':
                $WithdrawStartDate.attr('type','date');
                $WithdrawEndDate.attr('type','date');
                dataWithdraw=[
                    { name: '2014-01-01', value: 200, group: '北京' },
                    { name: '2014-01-01', value: 400, group: '天津' },
                    { name: '2014-01-01', value: 350, group: '上海' },
                    { name: '2014-01-01', value: 260, group: '重庆' },
                    { name: '2014-01-01', value: 185, group: '河北' },
                    { name: '2014-01-01', value: 140, group: '其他' },

                    { name: '2014-01-02', value: 282, group: '北京' },
                    { name: '2014-01-02', value: 150, group: '天津' },
                    { name: '2014-01-02', value: 450, group: '上海' },
                    { name: '2014-01-02', value: 350, group: '重庆' },
                    { name: '2014-01-02', value: 150, group: '河北' },
                    { name: '2014-01-02', value: 240, group: '其他' },

                    { name: '2014-01-03', value: 200, group: '北京' },
                    { name: '2014-01-03', value: 240, group: '天津' },
                    { name: '2014-01-03', value: 160, group: '上海' },
                    { name: '2014-01-03', value: 346, group: '重庆' },
                    { name: '2014-01-03', value: 357, group: '河北' },
                    { name: '2014-01-03', value: 764, group: '其他' },

                    { name: '2014-01-04', value: 457, group: '北京' },
                    { name: '2014-01-04', value: 246, group: '天津' },
                    { name: '2014-01-04', value: 369, group: '上海' },
                    { name: '2014-01-04', value: 168, group: '重庆' },
                    { name: '2014-01-04', value: 375, group: '河北' },
                    { name: '2014-01-04', value: 448, group: '其他' },

                    { name: '2014-01-05', value: 568, group: '北京' },
                    { name: '2014-01-05', value: 680, group: '天津' },
                    { name: '2014-01-05', value: 256, group: '上海' },
                    { name: '2014-01-05', value: 108, group: '重庆' },
                    { name: '2014-01-05', value: 160, group: '河北' },
                    { name: '2014-01-05', value: 367, group: '其他' }
                ];
                break;
            case 'W':
                $WithdrawStartDate.attr('type','week');
                $WithdrawEndDate.attr('type','week');
                dataWithdraw=[
                    { name: '周一', value: 600, group: '北京' },
                    { name: '周一', value: 580, group: '天津' },
                    { name: '周一', value: 680, group: '上海' },
                    { name: '周一', value: 468, group: '重庆' },
                    { name: '周一', value: 236, group: '河北' },
                    { name: '周一', value: 452, group: '其他' },

                    { name: '周二', value: 568, group: '北京' },
                    { name: '周二', value: 458, group: '天津' },
                    { name: '周二', value: 380, group: '上海' },
                    { name: '周二', value: 689, group: '重庆' },
                    { name: '周二', value: 454, group: '河北' },
                    { name: '周二', value: 279, group: '其他' },

                    { name: '周三', value: 478, group: '北京' },
                    { name: '周三', value: 278, group: '天津' },
                    { name: '周三', value: 364, group: '上海' },
                    { name: '周三', value: 108, group: '重庆' },
                    { name: '周三', value: 278, group: '河北' },
                    { name: '周三', value: 150, group: '其他' },

                    { name: '周四', value: 453, group: '北京' },
                    { name: '周四', value: 235, group: '天津' },
                    { name: '周四', value: 568, group: '上海' },
                    { name: '周四', value: 257, group: '重庆' },
                    { name: '周四', value: 473, group: '河北' },
                    { name: '周四', value: 275, group: '其他' },

                    { name: '周五', value: 245, group: '北京' },
                    { name: '周五', value: 143, group: '天津' },
                    { name: '周五', value: 235, group: '上海' },
                    { name: '周五', value: 654, group: '重庆' },
                    { name: '周五', value: 356, group: '河北' },
                    { name: '周五', value: 265, group: '其他' },

                    { name: '周六', value: 356, group: '北京' },
                    { name: '周六', value: 346, group: '天津' },
                    { name: '周六', value: 432, group: '上海' },
                    { name: '周六', value: 643, group: '重庆' },
                    { name: '周六', value: 265, group: '河北' },
                    { name: '周六', value: 245, group: '其他' },

                    { name: '周日', value: 154, group: '北京' },
                    { name: '周日', value: 234, group: '天津' },
                    { name: '周日', value: 543, group: '上海' },
                    { name: '周日', value: 245, group: '重庆' },
                    { name: '周日', value: 362, group: '河北' },
                    { name: '周日', value: 263, group: '其他' }
                ];
                break;
            case 'M':
                $WithdrawStartDate.attr('type','month');
                $WithdrawEndDate.attr('type','month');
                dataWithdraw=[
                    { name: '1月', value: 1000, group: '北京' },
                    { name: '1月', value: 2353, group: '天津' },
                    { name: '1月', value: 2311, group: '上海' },
                    { name: '1月', value: 1780, group: '重庆' },
                    { name: '1月', value: 800, group: '河北' },
                    { name: '1月', value: 1075, group: '其他' },

                    { name: '2月', value: 2853, group: '北京' },
                    { name: '2月', value: 2421, group: '天津' },
                    { name: '2月', value: 1643, group: '上海' },
                    { name: '2月', value: 1432, group: '重庆' },
                    { name: '2月', value: 1643, group: '河北' },
                    { name: '2月', value: 1643, group: '其他' },

                    { name: '3月', value: 1843, group: '北京' },
                    { name: '3月', value: 2000, group: '天津' },
                    { name: '3月', value: 2421, group: '上海' },
                    { name: '3月', value: 1453, group: '重庆' },
                    { name: '3月', value: 1642, group: '河北' },
                    { name: '3月', value: 1307, group: '其他' },

                    { name: '4月', value: 3245, group: '北京' },
                    { name: '4月', value: 2432, group: '天津' },
                    { name: '4月', value: 1896, group: '上海' },
                    { name: '4月', value: 1653, group: '重庆' },
                    { name: '4月', value: 2654, group: '河北' },
                    { name: '4月', value: 1065, group: '其他' },

                    { name: '5月', value: 2433, group: '北京' },
                    { name: '5月', value: 1643, group: '天津' },
                    { name: '5月', value: 1685, group: '上海' },
                    { name: '5月', value: 1567, group: '重庆' },
                    { name: '5月', value: 1489, group: '河北' },
                    { name: '5月', value: 1435, group: '其他' }
                ];
                break;
        }
    }).trigger('change');
    $WithdrawBtn.click(function() {
        var dataFormat={"startDate":$WithdrawStartDate.val(),"endDate":$WithdrawEndDate.val()};

        var option = loadEcharts.ChartOptionTemplates.Lines(dataWithdraw, '用户提现(元)', false),
            container = $("#userWithdrawDistribution")[0],
            opt = loadEcharts.ChartConfig(container, option);
        loadEcharts.Charts.RenderChart(opt);
    }).trigger('click');

    /*用户账户余额时间分布*/
    $UserAccountCategory.change(function() {
        UserAccountCategory=$UserAccountCategory.val();
        if(!UserAccountCategory) {
            UserAccountCategory='D';
        }
        switch(UserAccountCategory) {
            case 'D':
                dataUserAccount=[
                    { name: '2014-01-01', value: 200, group: '北京' },
                    { name: '2014-01-01', value: 400, group: '天津' },
                    { name: '2014-01-01', value: 350, group: '上海' },
                    { name: '2014-01-01', value: 260, group: '重庆' },
                    { name: '2014-01-01', value: 185, group: '河北' },
                    { name: '2014-01-01', value: 140, group: '其他' },

                    { name: '2014-01-02', value: 282, group: '北京' },
                    { name: '2014-01-02', value: 150, group: '天津' },
                    { name: '2014-01-02', value: 450, group: '上海' },
                    { name: '2014-01-02', value: 350, group: '重庆' },
                    { name: '2014-01-02', value: 150, group: '河北' },
                    { name: '2014-01-02', value: 240, group: '其他' },

                    { name: '2014-01-03', value: 200, group: '北京' },
                    { name: '2014-01-03', value: 240, group: '天津' },
                    { name: '2014-01-03', value: 160, group: '上海' },
                    { name: '2014-01-03', value: 346, group: '重庆' },
                    { name: '2014-01-03', value: 357, group: '河北' },
                    { name: '2014-01-03', value: 764, group: '其他' },

                    { name: '2014-01-04', value: 457, group: '北京' },
                    { name: '2014-01-04', value: 246, group: '天津' },
                    { name: '2014-01-04', value: 369, group: '上海' },
                    { name: '2014-01-04', value: 168, group: '重庆' },
                    { name: '2014-01-04', value: 375, group: '河北' },
                    { name: '2014-01-04', value: 448, group: '其他' },

                    { name: '2014-01-05', value: 568, group: '北京' },
                    { name: '2014-01-05', value: 680, group: '天津' },
                    { name: '2014-01-05', value: 256, group: '上海' },
                    { name: '2014-01-05', value: 108, group: '重庆' },
                    { name: '2014-01-05', value: 160, group: '河北' },
                    { name: '2014-01-05', value: 367, group: '其他' }
                ];
                break;
            case 'W':
                dataUserAccount=[
                    { name: '周一', value: 600, group: '北京' },
                    { name: '周一', value: 580, group: '天津' },
                    { name: '周一', value: 680, group: '上海' },
                    { name: '周一', value: 468, group: '重庆' },
                    { name: '周一', value: 236, group: '河北' },
                    { name: '周一', value: 452, group: '其他' },

                    { name: '周二', value: 568, group: '北京' },
                    { name: '周二', value: 458, group: '天津' },
                    { name: '周二', value: 380, group: '上海' },
                    { name: '周二', value: 689, group: '重庆' },
                    { name: '周二', value: 454, group: '河北' },
                    { name: '周二', value: 279, group: '其他' },

                    { name: '周三', value: 478, group: '北京' },
                    { name: '周三', value: 278, group: '天津' },
                    { name: '周三', value: 364, group: '上海' },
                    { name: '周三', value: 108, group: '重庆' },
                    { name: '周三', value: 278, group: '河北' },
                    { name: '周三', value: 150, group: '其他' },

                    { name: '周四', value: 453, group: '北京' },
                    { name: '周四', value: 235, group: '天津' },
                    { name: '周四', value: 568, group: '上海' },
                    { name: '周四', value: 257, group: '重庆' },
                    { name: '周四', value: 473, group: '河北' },
                    { name: '周四', value: 275, group: '其他' },

                    { name: '周五', value: 245, group: '北京' },
                    { name: '周五', value: 143, group: '天津' },
                    { name: '周五', value: 235, group: '上海' },
                    { name: '周五', value: 654, group: '重庆' },
                    { name: '周五', value: 356, group: '河北' },
                    { name: '周五', value: 265, group: '其他' },

                    { name: '周六', value: 356, group: '北京' },
                    { name: '周六', value: 346, group: '天津' },
                    { name: '周六', value: 432, group: '上海' },
                    { name: '周六', value: 643, group: '重庆' },
                    { name: '周六', value: 265, group: '河北' },
                    { name: '周六', value: 245, group: '其他' },

                    { name: '周日', value: 154, group: '北京' },
                    { name: '周日', value: 234, group: '天津' },
                    { name: '周日', value: 543, group: '上海' },
                    { name: '周日', value: 245, group: '重庆' },
                    { name: '周日', value: 362, group: '河北' },
                    { name: '周日', value: 263, group: '其他' }
                ];
                break;
            case 'M':
                dataUserAccount=[
                    { name: '1月', value: 1000, group: '北京' },
                    { name: '1月', value: 2353, group: '天津' },
                    { name: '1月', value: 2311, group: '上海' },
                    { name: '1月', value: 1780, group: '重庆' },
                    { name: '1月', value: 800, group: '河北' },
                    { name: '1月', value: 1075, group: '其他' },

                    { name: '2月', value: 2853, group: '北京' },
                    { name: '2月', value: 2421, group: '天津' },
                    { name: '2月', value: 1643, group: '上海' },
                    { name: '2月', value: 1432, group: '重庆' },
                    { name: '2月', value: 1643, group: '河北' },
                    { name: '2月', value: 1643, group: '其他' },

                    { name: '3月', value: 1843, group: '北京' },
                    { name: '3月', value: 2000, group: '天津' },
                    { name: '3月', value: 2421, group: '上海' },
                    { name: '3月', value: 1453, group: '重庆' },
                    { name: '3月', value: 1642, group: '河北' },
                    { name: '3月', value: 1307, group: '其他' },

                    { name: '4月', value: 3245, group: '北京' },
                    { name: '4月', value: 2432, group: '天津' },
                    { name: '4月', value: 1896, group: '上海' },
                    { name: '4月', value: 1653, group: '重庆' },
                    { name: '4月', value: 2654, group: '河北' },
                    { name: '4月', value: 1065, group: '其他' },

                    { name: '5月', value: 2433, group: '北京' },
                    { name: '5月', value: 1643, group: '天津' },
                    { name: '5月', value: 1685, group: '上海' },
                    { name: '5月', value: 1567, group: '重庆' },
                    { name: '5月', value: 1489, group: '河北' },
                    { name: '5月', value: 1435, group: '其他' }
                ];
                break;
        }
    }).trigger('change');
    $UserAccountBtn.click(function() {
        var dataFormat={"startDate":$UserAccountStartDate.val(),"endDate":$UserAccountEndDate.val()};

        var optionWithdraw = loadEcharts.ChartOptionTemplates.Lines(dataUserAccount, '用户账户余额(元)', true),
            containerWithdraw = $("#userAccountDistribution")[0],
            optWithdraw = loadEcharts.ChartConfig(containerWithdraw, optionWithdraw);
        loadEcharts.Charts.RenderChart(optWithdraw);
    }).trigger('click');

});
