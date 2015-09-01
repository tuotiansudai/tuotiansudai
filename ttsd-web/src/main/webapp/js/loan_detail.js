/**
 * Created by belen on 15/8/19.
 */
require(['jquery', 'csrf'], function ($) {
    $(function () {
        //初始化标的比例（进度条）
        var java_point = 15;
        if(java_point<=50){
            $('.chart-box .rount').css('webkitTransform',"rotate(" + 3.6*java_point + "deg)");
            $('.chart-box .rount2').hide();
        }else{
            $('.chart-box .rount').css('webkitTransform',"rotate(180deg)");
            $('.chart-box .rount2').show();
            $('.chart-box .rount2').css('webkitTransform',"rotate("+ 3.6*(java_point-50)+"deg)");
        }
    })




})