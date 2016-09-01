/**
 * name: [活动结束弹框]
 * tip:  凡是可点击的活动链接DOM的id为  actorFinish
 * time: 2016-09-01
 * Author: xuqiang
 */
define(['jquery','layerWrapper'], function ($,layer) {
    $('#actorFinish').on('click', function(event) {
        event.preventDefault();
        layer.alert('当前活动已结束！',{title:'温馨提示'}); 
    });
});