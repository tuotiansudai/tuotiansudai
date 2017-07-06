
require('wapSiteStyle/account/settings.scss');

let $settingBox = $('#settingBox');
//开启免密投资
let $btnOpenNopwd = $('.update-payment-nopwd',$settingBox);

$btnOpenNopwd.on('click',function() {
    let isOpen = $btnOpenNopwd.hasClass('opened');

    if(isOpen) {
        //目前是开启的状态，现在做的是要去关闭

        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['280px', '230px'],
            shadeClose: false,
            skin: 'tip-square-box',
            btn: ['确定','取消'],
            content: $('.tip-to-close'),
            btn1:function() {
                //确定


            },
            btn2:function() {
                //取消
            }
        });

    } else {
        //目前是开闭的状态，现在做的是要去开启


    }
});