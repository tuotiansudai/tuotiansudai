/**
 * Created by CBJ on 2015/10/26.
 */
require(['jquery','layer'], function ($,layer) {
    $(function () {
    var $InfoBox=$('#personInfoBox'),
        $setPhone=$('.setPhone',$InfoBox),
        $setEmail=$('.setEmail',$InfoBox),
        $setBankCard=$('.setBankCard',$InfoBox),
        $setPass=$('.setPass',$InfoBox);

        $setPhone.on('click', function(){
            layer.open({
                type: 1,
                area: ['500px', '200px'],
                shadeClose: true,
                content: $('#setPhonelDOM')
            });
        });

        $setEmail.on('click', function(){
            layer.open({
                type: 1,
                area: ['500px', '200px'],
                shadeClose: true,
                content: $('#changeEmailDOM')
            });
        });

        $setBankCard.on('click', function(){
            layer.open({
                type: 1,
                area: ['500px', '200px'],
                shadeClose: true,
                content: $('#setBankCardDOM')
            });
        });

        $setPass.on('click', function(){
            layer.open({
                type: 1,
                area: ['600px', '300px'],
                shadeClose: true,
                content: $('#changePassDOM')
            });
        });
    });
});
