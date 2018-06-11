require("activityStyle/july_activity_2018.scss");
require('publicJs/plugins/jquery.qrcode.min');
let commonFun= require('publicJs/commonFun');
var tpl = require('art-template/dist/template');
require('swiper/dist/css/swiper.css')
require('publicJs/login_tip');

//支持红方蓝方
let $supportBtn = $('.support-btn');
let $redSquare = $('.red-square');
let $blueSquare = $('.blue-square');
let $redAmount = $('#redAmount');
let $blueAmount = $('#blueAmount');
let $redCount = $('#redCount');
let $blueCount = $('#blueCount');
let $myAmount = $('#myAmount');
let $currentRate = $('#currentRate');
let $currentAward = $('#currentAward');//返现

let sourceKind = globalFun.parseURL(location.href);
let equipment = globalFun.equipment();
let url;
if (sourceKind.port) {
    url = sourceKind.protocol + '://' + sourceKind.host + ':' + sourceKind.port;
} else {
    url = sourceKind.protocol + '://' + sourceKind.host;
}

let Swiper = require('swiper/dist/js/swiper.jquery.min');
let slideLen = 5;
if($(document).width() <= 1024){
    slideLen = 3;
}else {
    slideLen = 5;
}
let qrcodeUrl = require('../images/2018/july-activity/qrcode.png');
$('#qrcodeImg').attr('src',qrcodeUrl);
let teamName = {
    'aiji':'埃及队',
    'deguo':'德国队',
    'agenting':'阿根廷队',
    'aodaliya':'澳大利亚队',
    'banama':'巴拿马队',
    'baxi':'巴西队',
    'bilishi':'比利时队',
    'bingdao':'冰岛队',
    'bolan':'波兰队',
    'danmai':'丹麦队',//10
    'eluosi':'俄罗斯队',//
    'faguo':'法国队',
    'gelunbiya':'哥伦比亚队',
    'gesidani':'哥斯达尼加队',
    'hanguo':'韩国队',
    'keluodiya':'克罗地亚队',
    'bilu':'秘鲁队',
    'moluoge':'摩洛哥队',
    'moxige':'墨西哥队',
    'niriliya':'尼日利亚队',//20
    'putaoya':'葡萄牙队',
    'riben':'日本队',
    'ruidian':'瑞典队',
    'ruishi':'瑞士队',
    'saierweiya':'塞尔维亚队',
    'saineijiaer':'塞内加尔队',
    'shatealabo':'沙特阿拉伯队',
    'tunisi':'突尼斯队',
    'wulagui':'乌拉圭队',
    'xibanya':'西班牙队',//30
    'yilang':'伊朗队',
    'yinggelan':'英格兰队'

}
let activityStatus = commonFun.activityStatus($('.july-container'));
$('#openBall').on('click',function () {
    $.when(commonFun.isUserLogin())
        .done(function () {
            if(activityStatus!== 'activity-ing') {
                layer.msg('不在活动时间范围内');
            }else {
                commonFun.useAjax({
                    url: '/activity/third-anniversary/draw',
                    type: 'POST'
                }, function (res) {
                    if(res.status == true){
                        let records = {
                            list:res.data,
                            teamName:teamName
                        }
                        $('#getLogos').html(tpl('getLogoTpl', records));

                        $('.tip-wrap').show();
                        getMyTeamLogos();
                    }else {
                        layer.msg(res.message)
                    }
                }
                )
            }
        })
        .fail(function () {
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: ['auto', 'auto'],
                content: $('#loginTip')
            });
        })
})
$('.known-btn').on('click',function () {
    $('.tip-wrap').hide();
})
$.when(commonFun.isUserLogin())
    .done(function () {
        getMyTeamLogos();
    })

function getMyTeamLogos(){
    commonFun.useAjax({
        url: '/activity/third-anniversary/prizes',
        type: 'GET'
    }, function (res) {
        if(res.status == true){
            let records = {
                list:res.data,
                teamName:teamName
            }
            $('#myTeamLogos').html(tpl('myTeamLogoTpl', records));
            var mySwiper = new Swiper ('.my-team-logos', {
                direction: 'horizontal',
                loop: true,
                autoplay:0,
                slidesPerGroup: slideLen,
                slidesPerView:slideLen,
                nextButton: '.nextBtn',
                prevButton: '.prevBtn',
                freeMode:true,
                pagination : '.swiper-pagination',
                paginationClickable:true
            });
        }else {
            layer.msg(res.message)
        }

    }
    )

}
function toLogin(){
    if (sourceKind.params.source == 'app') {
        location.href = "app/tuotian/login";
    }else{
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['auto', 'auto'],
            content: $('#loginTip')
        });
    }

}


//点击支持按钮
$supportBtn.on('click',function (e) {
    e.preventDefault();
    e.stopPropagation();
    let _self = $(this);
    $.when(commonFun.isUserLogin())
        .done(function () {
            if(_self.hasClass('disabled')||_self.hasClass('already_support')){
                layer.msg('您已支持！')
                return;
            }
            let url;
            if(_self.hasClass('red-square')){
                url = '/activity/third-anniversary/select?isRed=true';
            }else if(_self.hasClass('blue-square')){
                url = '/activity/third-anniversary/select?isRed=false';
            }
            commonFun.useAjax({
                    url: url,
                    type: 'POST'
                }, function (res) {
                    if(res.status == true){
                        $redAmount.text(res.data.redAmount);
                        $blueAmount.text(res.data.blueAmount);
                        $redCount.text(res.data.redCount);
                        $blueCount.text(res.data.blueCount);
                        $myAmount.text(res.data.myAmount);
                        $currentRate.text(res.data.currentRate);
                        $currentAward.text(res.data.currentAward);
                        let percent = res.data.redAmount/res.data.redAmount+res.data.blueAmount;
                        $('.percent-con').css('width',percent);
                        if(selectResult == 'RED'||selectResult == 'BLUE'){
                            _self.addClass('disabled');
                            layer.msg('支持成功！')
                        }

                        // res.data
                        // "redAmount":"红方总额",
                        //     "blueAmount":"蓝方总额",
                        //     "redCount":"红方支持总人数",
                        //     "blueCount":"蓝方支持总人数",
                        //     "isSelect":true/false,  //是否选择
                        //     "selectResult":"RED"/"BLUE", //选择方
                        //     "myAmount":2,     //我的总年化投资额
                        //     "currentRate":"0.5%",
                            // "currentAward":"23.12"
                    }else {
                        layer.msg('支持失败')
                    }

                }
            )

        })
        .fail(function () {
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: ['auto', 'auto'],
                content: $('#loginTip')
            });
        })
})
supportSquare();
//判断选择的是哪一方
function supportSquare(){
    commonFun.useAjax({
        url:'/activity/third-anniversary/select-result',
        type: 'GET'
    },function (res) {
        if(res.status == true){
            $.when(commonFun.isUserLogin())
                .done(function () {
                    if(res.data.selectResult == 'RED'){
                        $redSquare.addClass('already_support');
                        $blueSquare.addClass('disabled');
                    }else if(res.data.selectResult == 'BLUE'){
                        $blueSquare.addClass('already_support');
                        $redSquare.addClass('disabled');
                    }
                    $redAmount.text(res.data.redAmount);
                    $blueAmount.text(res.data.blueAmount);
                    $redCount.text(res.data.redCount);
                    $blueCount.text(res.data.blueCount);
                    $myAmount.text(res.data.myAmount);
                    $currentRate.text(res.data.currentRate);
                    $currentAward.text(res.data.currentAward);
                    let percent = (parseFloat(res.data.redAmount)/parseFloat(res.data.redAmount)+parseFloat(res.data.blueAmount))*100;
                   
                    $('.percent-con').css('width',percent+'%');
                })

        }


    })
}


