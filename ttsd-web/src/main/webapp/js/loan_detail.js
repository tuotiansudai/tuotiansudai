/**
 * Created by belen on 15/8/19.
 */
require(['jquery', 'csrf'], function ($) {
    $(function () {
        //初始化标的比例（进度条）
        var java_point = 15; //后台传递数据
        if(java_point<=50){
            $('.chart-box .rount').css('webkitTransform',"rotate(" + 3.6*java_point + "deg)");
            $('.chart-box .rount2').hide();
        }else{
            $('.chart-box .rount').css('webkitTransform',"rotate(180deg)");
            $('.chart-box .rount2').show();
            $('.chart-box .rount2').css('webkitTransform',"rotate("+ 3.6*(java_point-50)+"deg)");
        }

        // tab select
        var tab_li = $('.nav li');
        tab_li.click(function () {
            var _index = $(this).index();
            $(this).addClass('current').siblings().removeClass('current');
            $('.loan-list .loan-list-con').eq(_index).show().siblings().hide();
        });
        $('.img-list li').click(function(){
            var _imgSrc = $(this).find('img').attr('src');
            $('.content img').attr('src',_imgSrc);
            $('.layer-box').show();
            return false;
        });
        $('.layer-wrapper').click(function () {
            $('.layer-box').hide();
        })







    });
})