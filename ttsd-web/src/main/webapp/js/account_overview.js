require(['jquery','echarts','commonFun', 'jquery.ajax.extension','layerWrapper'], function ($) {
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

        var data = [{ name: '可用金额', value: pydata.balance },
                { name: '待收本金', value: pydata.collectingPrincipal },
                { name: '待收利息', value: pydata.collectingInterest}],
         option = MyChartsObject.ChartOptionTemplates.Pie(data,'YTTTTT'),
         container = $("#ReportShow")[0],
         opt = MyChartsObject.ChartConfig(container, option);
        MyChartsObject.Charts.RenderChart(opt);


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
        $signBtn.on('click', function (event) {
            event.preventDefault();
            var _this = $(this),
                signText = $(".sign-text");
                tomorrowText = $(".tomorrow-text");
                $addDou = $(".add-dou");
            if(_this.hasClass('active')){
                return false;
            }else{
                $.ajax({
                    url: _this.data('url'),
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                })
                    .done(function (response) {
                        if (response.data.status) {
                            signText.html("签到成功，领取" + response.data.signInPoint + "财豆！");
                            tomorrowText.html("明日可领" + response.data.nextSignInPoint + "财豆");
                            $addDou.html("+" + response.data.signInPoint);
                            $signTip.fadeIn('fast', function () {
                                $(this).find('.add-dou').animate({
                                    'bottom': '50px',
                                    'opacity': '0'
                                }, 800);
                            });
                            _this.removeClass("will-sign").addClass("finish-sign").html("已签到");
                            _this.addClass('active');
                            _this.parent('.sign-top').addClass('no-click');
                        }
                    })
            }

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