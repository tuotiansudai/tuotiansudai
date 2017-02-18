require(['jquery', 'load_echarts','layerWrapper','template','jquery.ajax.extension'], function ($,loadEcharts,layer,tpl) {
    $(function () {
    var $tMonthBox=$('#tMonthBox'),
        $signBtn = $('#signBtn'),
        $signTip = $('#signLayer'),
        $closeSign = $('#closeSign'),
        $switchMenu=$('ul',$tMonthBox);
        $switchMenu.find('li').last().addClass('current');
        $('table',$tMonthBox).eq(1).show().siblings('table').hide();
        $switchMenu.find('li').click(function(index) {
            var $this=$(this),
                num=$switchMenu.find('li').index(this);
            $this.addClass('current').siblings('li').removeClass('current');
            $('table',$tMonthBox).eq(num).show().siblings('table').hide();
        });

        // 资产总额饼状图报表
        (function(loadEcharts) {
            var data = [{ name: '可用金额', value: pydata.balance },
                { name: '待收投资本金', value: pydata.expectedTotalCorpus },
                { name: '待收预期收益', value: pydata.expectedTotalInterest}];

            var option = loadEcharts.optionCategory.PieOption(data),
                opt = loadEcharts.ChartConfig('ReportShow', option);
            loadEcharts.RenderChart(opt);

        })(loadEcharts);


        tipshow('#tMonthBox','.month-title',6);
        tipshow('.newProjects','.trade-detail',15);
        $('.birth-icon').on('mouseenter',function() {
            layer.closeAll('tips');
            var num = parseFloat($(this).attr('data-benefit'));
            var benefit = num + 1;
            layer.tips('您已享受生日福利，首月收益翻'+benefit+'倍', $(this), {
                tips: [1, '#efbf5c'],
                time: 2000,
                tipsMore: true,
                area: 'auto',
                maxWidth: '500'
            });
        });

        /**
         * @dom  {[string]}		当前列表的DOM节点
         * @active  {[string]}	触发事件的DOM节点
         * @length  {[number]}	限制文字长度触发提示框
         * @return {[function]}
         */
        function tipshow(dom,active,length){
            $(dom).on('mouseenter',active,function() {
                layer.closeAll('tips');
                if($(this).text().length>length){
                    layer.tips($(this).text(), $(this), {
                        tips: [1, '#efbf5c'],
                        time: 2000,
                        tipsMore: true,
                        area: 'auto',
                        maxWidth: '500'
                    });
                }
            });
        }
        $signBtn.on('click', function(event) {
            event.preventDefault();
            var _this = $(this),
                $signText = $(".sign-text"),
                $tomorrowText = $(".tomorrow-text"),
                $signPoint = $(".sign-point"),
                $introText = $('.intro-text'),
                $nextText = $('.next-text'),
                $signBtn = $("#signBtn");

            $.ajax({
                url: _this.attr('data-url'),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(response) {
                if (response.data.status) {
                    response.data.signIn == true ? $signText.html("您今天已签到") : $signText.html("签到成功");
                    $tomorrowText.html("明日签到可获得" + response.data.nextSignInPoint + "积分");
                    if(response.data.full == true){
                        $introText.html('已连续签到365天，获得全勤奖！');
                        $nextText.html('365元现金红包');
                    }
                    else{
                        $introText.html(response.data.currentRewardDesc);
                        $nextText.html(response.data.nextRewardDesc);
                    }
                    $signBtn.parent().addClass("no-click").find('span').removeClass('will-sign').addClass('finish-sign').html("已签到");
                    $signPoint.find('span').html('+'+response.data.signInPoint);
                    $signTip.fadeIn('fast');
                    $("#MyAvailablePoint").text(Math.round($("#MyAvailablePoint").text()) + Math.round(response.data.signInPoint));
                } else {
                    location.href='/register/account?redirect=/account';
                }
            })
        });
        //hide sign tip
        $closeSign.on('click', function (event) {
            event.preventDefault();
            $signTip.fadeOut('fast', function() {
                $(this).find('.add-dou').css({
                    'bottom': '0',
                    'opacity': '1'
                });
            });
        });
    });
});