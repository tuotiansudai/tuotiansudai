/**
 * [name]:spring festival activity
 * [author]:xuqiang
 * [date]:2017-01-09
 */
require(['jquery', 'layerWrapper','commonFun','jquery.ajax.extension','logintip'], function ($,layer,commonFun) {
    $(function() {
        var sourceKind=globalFun.parseURL(location.href);
        //判断是否登录
        $.when(commonFun.isUserLogin())
         .done(function(){
             $('.check-in',$('.spring-festival-container')).show();
         })
         .fail(function(){
             $('#loginCheck').show();
         });

        //签到前判断设备
        $('#loginCheck').on('click', function(event) {
            event.preventDefault();
            if(sourceKind.params.source=='app') {
                location.href="/login";
            } else {
                $('.no-login-text',$('.spring-festival-container')).trigger('click');  //弹框登录
            }
        });

        //登录后签到事件
        $('#checkIn').on('click', function(event) {
            event.preventDefault();
            var $self=$(this);
            $.ajax({
                url:'/point/sign-in',
                type:'POST',
                dataType: 'json'
            })
            .done(function(data) {
                if(data.data.message != '' && data.data.message != null){
                    layer.msg(data.data.message);
                }else{
                    taskDraw($self);
                }
            })
            .fail(function() {
                //layer.msg('请求失败,请重试!');
            });

        });

        //领福袋
        $('#drawBtn').on('click', function(event) {
            event.preventDefault();
            taskDraw($(this));
        });
        //抽奖
        function taskDraw(dom){
            $.ajax({
                url: '/activity/point-draw/task-draw',
                type: 'POST',
                dataType: 'json',
                data: {
                    "activityCategory":"SPRING_FESTIVAL_ACTIVITY"
                }
            })
            .done(function(data) {
                if(data.returnCode == 1){
                    layer.msg('今日已签到，请明天再来！');
                }else if(data.returnCode == 0){
                    $('#numBite').text(data.prizeValue.split(':')[1]=='投资红包'?'元':'%');
                    $('#bagType').text(data.prizeValue.split(':')[1]);
                    $('#numText').text(data.prizeValue.split(':')[0]);
                    dom.addClass('active').text('已签到');
                    layer.open({
                        type: 1,
                        move:false,
                        area:$(window).width()>700?['400px','300px']:['280px','300px'],
                        title:false,
                        content: $('#moneyTip')
                    });
                }
            })
            .fail(function() {
                //layer.msg('请求失败，请重试！');
            });
        }
    });        
});