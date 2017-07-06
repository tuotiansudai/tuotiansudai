let commonFun = require('publicJs/commonFun');
require('wapSiteStyle/account/settings.scss');

let $settingBox = $('#settingBox');

let UrlOption = {
    //关闭免密投资
    disabled:'no-password-invest/disabled',
    //打开免密投资
    enabled:'no-password-invest/enabled',
    //发送手机短信验证码
    sendCaptcha:'no-password-invest/send-captcha',
    //刷新图形验证码
    imageCaptcha:'no-password-invest/image-captcha',

    //去联动优势授权
    agreement:'/agreement'
};

//开启免密投资
let $btnOpenNopwd = $('.update-payment-nopwd',$settingBox);

$btnOpenNopwd.on('click',function() {
    let isOpen = $btnOpenNopwd.hasClass('opened'); //
    let firstopen = $btnOpenNopwd.data('firstopen'); //用来判断是否第一次开启
    if(isOpen) {
        //之前是开启的状态，现在做的是要去关闭
        $btnOpenNopwd.removeClass('opened');


    } else {
        //之前是开闭的状态，现在做的是要去开启
        $btnOpenNopwd.addClass('opened');
        OpenNoPasswordInvest(firstopen);

    }
});

//去开启免密投资业务，第一次开启免密投资，需要去联动优势授权，之后不需要
function OpenNoPasswordInvest(firstopen) {

    if(firstopen) {
        CommonLayerTip({
            btn: ['确定','取消'],
            content: $('#turnOnNoPassword')
        },function() {
            //确定打开，去联动优势授权
            commonFun.useAjax({
                type: 'POST',
                url:UrlOption['agreement']
            },function() {
                //授权成功
                CommonLayerTip({
                    btn: ['我知道了'],
                    content: $('#noPasswordInvestDOM')
                });

            });
        });
    } else {

        //如果是第二次打开 ，无需走联动优势，直接打开
        commonFun.useAjax({
            type: 'POST',
            url:UrlOption['enabled']
        },function() {
            CommonLayerTip({
                btn: ['我知道了'],
                content: '<div class="tip-result-success pad"> <em class="icon-success"></em><span>免密支付已开启</span></div>',
            },function() {
                location.reload();
                layer.closeAll();
            });

        });
    }
}

//去关闭免密投资业务
function turnOffNoPassword() {
    CommonLayerTip({
        btn: ['确定', '取消'],
        content: $('#turnOffNoPassword')
    },function() {
        //确定关闭免密支付，然后会弹出需要输入验证码的框

        CommonLayerTip({
            btn: ['确定', '取消'],
            content: $('#turnOnSendCaptcha')
        },function() {
            //确认， 这里没做
        });

    });
}

function CommonLayerTip(option,firstCallback,secondCallback) {
    layer.closeAll();
    let defaultOption = {
        btn:['确定', '取消'],
        content:$('#turnOnSendCaptcha'),
        area:['280px', '230px']
    };

    let optionOk = $.extend({},defaultOption,option);
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        area: btn.area,
        shadeClose: false,
        skin: 'tip-square-box',
        btn: optionOk.btn,
        content: optionOk.content,
        btn1: function () {
            firstCallback && firstCallback();
        },
        btn2: function () {
            secondCallback && secondCallback();
        }
    });
}

// 下面的代码是安心签的

// $btnOpenNopwd.on('click',function() {
//     let isOpen = $btnOpenNopwd.hasClass('opened');
//
//     if(isOpen) {
//         //目前是开启的状态，现在做的是要去关闭
//
//         layer.open({
//             type: 1,
//             title: false,
//             closeBtn: 0,
//             area: ['280px', '230px'],
//             shadeClose: false,
//             skin: 'tip-square-box',
//             btn: ['确定','取消'],
//             content: $('.tip-to-close'),
//             btn1:function() {
//                 //确定关闭
//                 switchSkipAuth(false);
//
//
//             },
//             btn2:function() {
//                 //取消,无需做额外的操作
//             }
//         });
//
//     } else {
//         //目前是开闭的状态，现在做的是要去开启
//
//         layer.open({
//             type: 1,
//             title: false,
//             closeBtn: 0,
//             area: ['280px', '230px'],
//             shadeClose: false,
//             skin: 'tip-square-box',
//             btn: ['去授权','取消'],
//             content: $('.tip-to-open'),
//             btn1:function() {
//                 //确定关闭
//                 switchSkipAuth(false);
//
//             },
//             btn2:function() {
//                 //取消,无需做额外的操作
//             }
//         });
//
//     }
// });
//
// //改变安心签的状态，开启还是关闭
// function switchSkipAuth(openStatue=true) {
//     commonFun.useAjax({
//             url:'/anxinSign/switchSkipAuth',
//             type:'POST',
//             data:{
//                 "open":openStatue
//             }
//         },function(data) {
//         if(data.success) {
//             layer.closeAll();
//             let timMsg = '免密支付已关闭';
//             if(openStatue) {
//                 // 开启成功
//                 timMsg = '免密支付已开启';
//             } else {
//                 // 关闭成功
//             }
//
//             layer.open({
//                 type: 1,
//                 title: false,
//                 closeBtn: 0,
//                 area: ['280px', '180px'],
//                 shadeClose: false,
//                 skin: 'tip-square-box',
//                 btn: ['我知道了'],
//                 content: `<div class="tip-result-success pad"> <em class="icon-success"></em><span>${timMsg}</span></div>`
//             });
//         }
//     })
// }