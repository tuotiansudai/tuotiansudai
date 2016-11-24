/**
 * name: [活动结束弹框]
 * tip:  凡是可点击的活动链接DOM的id为  actorFinish
 * time: 2016-09-01
 * Author: xuqiang
 */
define(['jquery','layerWrapper'], function ($,layer) {
    $('.actor-finish').on('click', function(event) {
        event.preventDefault();
        layer.open({
            type: 1,
            move:false,
            area:['320px','auto'],
            title:'温馨提示',
            content: '<p style="padding:20px 0;text-align:center;">活动已经结束了哦!</p>'
            +'<p style="text-align:center;">'
            +'<a href="/activity/activity-center" class="btn" style="background: #ff752a;border: 1px solid #f46916;color: #fff;">查看其他活动</a></p>'
        });
    });
});