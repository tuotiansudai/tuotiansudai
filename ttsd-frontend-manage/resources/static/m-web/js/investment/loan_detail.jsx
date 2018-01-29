require('mWebStyle/investment/experience_loan.scss');
require('mWebStyle/investment/loan_detail.scss');
require('mWebStyle/investment/project_detail.scss');
require('mWebStyle/investment/buy_loan.scss');
require('mWebJs/plugins/smartphoto/smartphoto.min.css');
let smartPhoto = require('mWebJs/plugins/smartphoto/jquery-smartphoto.min.js');
require('mWebJsModule/anxin_agreement_pop');
let anxinModule = require('webJsModule/anxin_signed');
require('webJs/plugins/autoNumeric');
let loanId = $('input[name="loanId"]',$buyDetail).val();
var tpl = require('art-template/dist/template');
let $loanDetail = $('#loanDetail'),
    $buyDetail = $('#buyDetail'),
    $iconHelp = $('.icon-help', $loanDetail);

$iconHelp.on('click',function() {
    $('.invest-refer-box',$loanDetail).toggle();
})
//点击立即投资进入购买详情页
$('#toInvest').on('click',function () {
    //pushHistory('#buyDetail');
    location.hash='buyDetail';

})
//借款详情

let commonFun = require('publicJs/commonFun');
let menuClick = require('mWebJsModule/menuClick');

let $projectDetail = $('#projectDetail'),//项目详情模块
    $recordTop = $('.record-top',$projectDetail);

menuClick({
    pageDom:$projectDetail
});
//虚以待位显示灰色图标，否则显示有色图标
$recordTop.find('i').each(function (index,item) {
    if($(item).next().find('em').text() !== '虚以待位'){
        $(item).addClass('on');
    }else {
        $(item).removeClass('on');
    }
})
$recordTop.find('span').on('click',function() {

    let kind = $(this).data('kind');

    let kindObj = {
        'xianfeng':{
            title:'拓荒先锋',
            content:'第一位投资的用户，<br/>获得<i>0.2%加息券，50元红包。</i>'
        },
        'biaowang':{
            title:'拓天标王',
            content:'单标累计投资最高的用户，<br/>获得<i>0.5%加息券，100元红包。</i>'
        },
        'dingying':{
            title:'一锤定音',
            content:'最后一位投资的用户，<br/>获得<i>0.2%加息券，50元红包。</i>'
        }
    };
    commonFun.CommonLayerTip({
        btn: ['我知道了'],
        area:['280px', '190px'],
        content: `<div class="record-tip-box"><span class="kaola"></span> <b class="pop-title">${kindObj[kind].title}</b> <span>${kindObj[kind].content}</span></div> `,
    },function() {
        layer.closeAll();
    })

});

//直投项目购买详情


let $btnWapNormal = $('.btn-wap-normal',$buyDetail),
    $amountInputElement = $('.input-amount',$buyDetail),
    minAmount = parseInt($amountInputElement.data('min-invest-amount')),//起投金额
    leftInvest = parseInt($amountInputElement.data('amount-need-raised')),//剩余可投
    duration = parseInt($amountInputElement.data('duration')) ;//项目最长期限

$buyDetail.find('.bg-square-box').append(commonFun.repeatBgSquare(33));

$amountInputElement.autoNumeric('init');

function getInvestAmount() {
    var amount = 0;
    if($amountInputElement.length == 0){
        return;
    }
    if (!isNaN($amountInputElement.autoNumeric("get"))) {
        amount = parseInt(($amountInputElement.autoNumeric("get") * 100).toFixed(0));
    }

    return amount;
}


//点击项目详情去项目详情模块

$('#to_project_detail').on('click',function () {
    location.hash='projectDetail'
})

$('#apply_materal_btn').click(function () {
    $('#apply_material').show();
})
$('#btn-detail-toggle').click(function () {
    $('#apply_material').hide();
})

    //交易记录ajax请求
    let $boxContent = $('#box_content');
    let ajaxUrl = $boxContent.data('url');
    let $scroll = $boxContent.find('#scroll');
    let $content = $boxContent.find('#content');
    let pageNum = 1;
    let $pullUpDOM = $('#pullUp');
    $('#transaction_record').on('click',function () {
        getMoreRecords();
        //交易记录滚动加载更多
        setTimeout(function () {
            var myScroll = new IScroll('#box_content', {
                probeType: 2,
                mouseWheel: true
            });
            myScroll.on('scrollEnd', function () {
                //如果滑动到底部，则加载更多数据（距离最底部10px高度）
                $pullUpDOM.show();
                if ((this.y - this.maxScrollY) <= 10) {
                    pageNum++;

                    getMoreRecords();
                    myScroll.refresh();
                }

            });
        },1000)




    })

    function getMoreRecords(){
        commonFun.useAjax(
            {
                url:ajaxUrl,
                type:'get',
                data:{
                    index:pageNum
                }
            },
            function (res) {
                $pullUpDOM.hide();
                if(pageNum == 1){
                    if(res.data.records.length > 0){
                        var html = tpl('recordsTpl', res.data);
                        $content.prepend(html)
                    }else {
                        $content.html('<div class="no-records"><div class="icon"></div><p>暂无交易记录</p></div>')
                    }
                }else {
                    if(res.data.records.length > 0){
                        var html = tpl('recordsTpl', res.data);
                        $content.prepend(html)
                    }else {
                        $('#pullUp').find('.pullUpLabel').html('没有更多数据了');
                    }
                }


            }
        )
    }

//转让购买详情
//承接记录
$('#look_continue_record').click(function () {
    $('#transferingDetail').hide();
    $('.buy-transfer').hide();
    $('#continue_record').show();
})
//回款计划
$('#look_repay_plan').click(function () {
    $('.buy-transfer').hide();
    $('#cotinue_record').hide();
   $('#transferingDetail').hide();
    $('#repay_plan').show();
})

//优惠券
if($('#couponText').text() == '无可用优惠券'){
    $('#couponText').css('color','#64646D')
}
let $selectCoupon = $('#select_coupon');
$selectCoupon.on('click',function () {
    location.hash='selectCoupon'
//无优惠券列表是显示缺省
    if($('.coupon-list-container').find('>li').length == 0){
        let $noCouponDOM = $('<div class="noCoupon"><div class="noCouponIcon"></div><p>暂无可用的优惠券</p></div>')
        $('.coupon-list-container').html($noCouponDOM)
    }
})
function validateHash() {
    if(location.hash == ''){
        $loanDetail.show().siblings('.show-page').hide();

    }else if(location.hash == '#projectDetail'){

        $projectDetail.show().siblings('.show-page').hide();

    }else if(location.hash == '#buyDetail'){
        $buyDetail.show().siblings('.show-page').hide();

    }else if(location.hash == '#selectCoupon'){
        $('#couponList').show().siblings('.show-page').hide();
    }
}
validateHash();//根据hash值让对应页面显示隐藏
$(window).on('hashchange',function () {
    validateHash();
})
//判断预期收益
var calExpectedInterest = function() {
    commonFun.useAjax({
        url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + getInvestAmount(),
        type: 'GET',
    },function(amount) {
        $("#expectedEarnings").text(amount);
    });
};


//立即使用优惠券
let $couponInfo = $('#couponInfo');
let userCouponId,
    minInvestAmount,
    couponType,
    productTypeUsable,
    couponEndTime,
    dataCouponDesc;
//默认选择的优惠券
let couponId = $('#couponId').val();
$('.to-use_coupon').each(function (index,item) {
    if($(item).data('user-coupon-id') == couponId){
        $(item).addClass('selected');
        return;
    }
})
$('.to-use_coupon').click(function () {
    let _self = $(this);
    if(_self.hasClass('disabled')){
        return;
    }else if(_self.hasClass('selected')){
        _self.removeClass('selected');//如果已经选择，则取消选择
        $('#couponId').val('');
        return;
    }
    $('.to-use_coupon').each(function (index,item) {
        $(item).removeClass('selected');

    })


    _self.addClass('selected');
    $('#couponId').val(_self.data('user-coupon-id'));
    location.hash='buyDetail';

    $('#couponText').text(_self.data('coupon-desc'));

})
//优惠券后退按钮
$('#iconCoupon').click(function () {
    location.hash='buyDetail';
})
//不使用优惠券
$('#noUse').click(function () {
    $('.to-use_coupon').each(function (index,item) {
        $(item).removeClass('selected');
        $('#couponText').text('请选择优惠券');
        $('#couponId').val('');

    })
    location.hash='buyDetail';
})
let $couponExpectedInterest = $(".experience-income");
//计算加息券或者投资红包的预期收益
let calExpectedCouponInterest = function() {
    if($('#maxBenifit').val() == ''){
        $couponExpectedInterest.text("");
    }else {
        commonFun.useAjax({
            url: '/calculate-expected-coupon-interest/loan/' + loanId + '/amount/' + getInvestAmount(),
            data: 'couponIds='+$('#maxBenifit').val(),
            type: 'GET'
        },function(amount) {console.log(amount)
            $couponExpectedInterest.text("+" + amount);
        });
    }

};
//页面加载判断预期收益
if($buyDetail.length !==0){
    maxBenifitUserCoupon();
}

function maxBenifitUserCoupon() {
    commonFun.useAjax({
        url: '/loan/' + loanId + '/amount/' + getInvestAmount() + "/max-benefit-user-coupon",
        type: 'GET',
    },function(maxBenefitUserCouponId) {
        $('#couponText').css('color','#FF473C')
        if (!isNaN(parseInt(maxBenefitUserCouponId))) {
            $('#couponId').val(maxBenefitUserCouponId);
            $('.to-use_coupon').each(function (index,item) {
                $(item).removeClass('selected');
                if($(item).data('user-coupon-id') == maxBenefitUserCouponId){
                    $(item).addClass('selected');
                    $('#maxBenifit').val($(item).data('coupon-id'));
                    $('#couponText').text($(item).data('coupon-desc'));
                }


            })
            calExpectedCouponInterest();
        } else {
            $('#couponText').text('无可用优惠券');
            $('#couponText').css('color','#64646D');
            $couponExpectedInterest.text("");
            $('.to-use_coupon').each(function (index,item) {
                $(item).addClass('disabled');
            })
        }
    })
}
couponSelect();
//优惠券显示的判断
function couponSelect() {
    let value = getInvestAmount();
    $('.to-use_coupon').each(function (index,item) {
        $(item).addClass('disabled');
        if($(item).data('min-invest-amount') <= value/100 && parseInt($(item).data('min-product-type'))  <= duration){
            $(item).removeClass('disabled');
        }

    })
}
function testAmount() {
    let value = getInvestAmount();
    calExpectedInterest();
    maxBenifitUserCoupon();
    couponSelect();
    if(value/100  == 0){
        $btnWapNormal.prop('disabled',true).text('请输入正确的金额');
    } else if(value/100 <minAmount){
        $btnWapNormal.prop('disabled',true).text('输入金额应大于起投金额');
    }else if(value/100 >leftInvest){
        $btnWapNormal.prop('disabled',true).text('输入金额应小于项目可投金额');
    }
    else {
        $btnWapNormal.prop('disabled',false).text('立即投资');
    }

}
//输入金额判断
$amountInputElement
    .on('keyup',function() {
        testAmount();
    })
//立即投资提交表单
let noPasswordInvest = $amountInputElement.data('no-password-invest');//是否开通免密支付
let isInvestor = 'INVESTOR' === $buyDetail.data('user-role');
let isAuthentication = 'USER' === $buyDetail.data('authentication');
let $investForm = $('#investForm');//立即投资表单

$('#investSubmit').on('click', function(event) {
    event.preventDefault();
    let investAmount = getInvestAmount()/100;
    $.when(commonFun.isUserLogin())
        .done(function() {
            if (isInvestor) {
                let accountAmount = parseInt($amountInputElement.data("user-balance")) || 0;
                if (investAmount > accountAmount) {
                    commonFun.CommonLayerTip({
                        btn: ['确定','取消'],
                        area:['280px', '160px'],
                        content: `<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <span>您的账户余额不足，请先进行充值</span></div> `,
                    },function() {
                        location.href = '/m/recharge';//去充值
                    })
                     return false;
                }
                noPasswordInvest ? sendSubmitRequest() : $investForm.submit();
                return;
            }
            if (isAuthentication) {
                location.href = '/m/register/account';//去实名认证
            }
        })
        .fail(function() {
            //判断是否需要弹框登陆
            location.href='/m/login'
        });
});

//发送投资提交请求
function sendSubmitRequest(){
    $amountInputElement.val($amountInputElement.autoNumeric("get"));//格式化还原金额
    commonFun.useAjax({
        url: '/no-password-invest',
        data: $investForm.serialize(),
        type: 'POST'
    },function(response) {

        let data = response.data;
        if (data.status) {
            location.href = "/m/callback/invest_project_transfer_nopwd?" + $.param(data.extraValues);
        }  else {
            commonFun.CommonLayerTip({
                btn: ['我知道了'],
                area:['280px', '160px'],
                content: `<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <span>${data.message}</span></div> `,
            },function() {
                layer.closeAll();
            })
        }
    });
}
//点击购买详情后退按钮
$('#iconBuy',$buyDetail).click(function () {
    location.href='/m/loan/'+loanId;
});
//点击直投详情页后退按钮进入列表页
$('#iconDetail',$loanDetail).click(function () {
    location.href = '/m/loan-list'
});
//点击项目详情进入直投项目详情页
$('#iconProjectDetail',$projectDetail).click(function () {
    location.href='/m/loan/'+loanId;
});
$('#clearFont').click(function () {
    $amountInputElement.val('');
});

//优惠券兑换
$('#exchangeCoupon').on('click',function () {
    location.href = '/m/my-treasure/coupon-exchange'
})

//转让购买
let $transferDetail = $('#transfer_details'),//转让购买页
    $toBuyTransfer = $('#to_buy_transfer'),//立即投资
    $transferSubmit = $('#transferSubmit'),//转让按钮
    $isAnxinAuthenticationRequired = $('#isAnxinAuthenticationRequired');

$toBuyTransfer.on('click',function () {
    $transferDetail.show().siblings().hide();
})
$transferSubmit.on('click',function (e) {
    e.preventDefault();
    $.when(commonFun.isUserLogin())
        .fail(function() {
            //判断是否需要弹框登陆
            location.href = '/m/login'
        })
        .done(function() {
            submitData();
        });
})
function submitData() {
    var transferApplicationId = parseInt($("#transferApplicationId").val()),
        transferAmount = $("#amount").val(),
        userBalance = $("#userBalance").val();
    commonFun.useAjax({
        url: '/transfer/' + transferApplicationId + '/purchase-check',
        type: 'GET'
    },function(data) {

        if (data.message == "SUCCESS") {
            commonFun.CommonLayerTip({
                        btn: ['确定'],
                        area:['280px', '160px'],
                        content: `<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <span>该项目已被承接，请选择其他项目</span></div> `,
                    },function () {
                layer.closeAll();
                location.href = "/m/transfer-list";
            })

        } else if (data.message == "CANCEL") {
            commonFun.CommonLayerTip({
                btn: ['确定'],
                area:['280px', '160px'],
                content: `<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <span>该项目已被取消，请选择其他项目。</span></div> `,
            },function () {
                layer.closeAll();
                location.href = "/m/transfer-list";
            })

        } else if (data.message == "MULTITERM") {
            commonFun.CommonLayerTip({
                btn: ['确定'],
                area:['280px', '160px'],
                content: `<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <span>该项目已被承接或已取消，请选择其他项目</span></div> `,
            },function () {
                layer.closeAll();
                location.href = "/m/transfer-list";
            })

        } else {
            var $transferForm = $('#transferForm');
            if ($transferForm.attr('action') === '/transfer/purchase') {

                var isInvestor = 'INVESTOR' === $transferDetail.data('user-role');
                if (!isInvestor) {
                    location.href = '/m/login?redirect=' + encodeURIComponent(location.href);
                    return false;
                }

                var accountAmount = parseInt((userBalance * 100).toFixed(0)) || 0;
                if (parseInt((transferAmount * 100).toFixed(0)) > accountAmount) {
                    commonFun.CommonLayerTip({
                        btn: ['确定','取消'],
                        area:['280px', '160px'],
                        content: `<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <span>您的账户余额不足，请先进行充值</span></div> `,
                    },function() {
                        location.href = '/m/recharge';//去充值
                    })
                    return false;
                }
            }
            if($isAnxinAuthenticationRequired.val()=='false'){
                $transferForm.submit();
            }else{
                anxinModule.getSkipPhoneTip();
                return false;
            }

        }
    });
}
$transferDetail.find('.bg-square-box').append(commonFun.repeatBgSquare(33));
//回退按钮
let transferApplicationId = $("#transferApplicationId").val();
$('#iconTransferDetail').on('click',function () {
    location.href = '/m/transfer/'+transferApplicationId;

})
//协议
$('.init-checkbox-style').initCheckbox(function(event) {
    //如果安心签协议未勾选，马上投资按钮需要置灰
    let checkboxBtn = event.children[0];
    let checkBool = $(checkboxBtn).prop('checked');
    if(checkboxBtn.id=='skipCheck') {
        $('.btn-wap-normal').prop('disabled',!checkBool);
    }
});
//转让详情页回退按钮
$('#iconTransferM').on('click',function () {
    location.href = '/m/transfer-list'
})
//回款计划回退按钮
$('#iconReplay').on('click',function () {
    location.href = '/m/transfer/'+transferApplicationId;
})
//债权承接记录
$('#iconContinue').on('click',function () {
    location.href = '/m/transfer/'+transferApplicationId;
})
//转让详情
if($('.money').length){
    $('.money').autoNumeric('init');
}
//收起详情


//项目材料图片预览
$(function(){

    $(".js-img-viwer").smartPhoto({
        nav:false,
        resizeStyle:'fit'
    });

});

$('#agrement').on('click',function () {
    location.href = $(this).data('url');
})

