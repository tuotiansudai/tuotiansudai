import 'webModule/drag';
import {ValidatorForm} from 'publicJs/validator';
import {useAjax,refreshCaptcha} from 'publicJs/common';

let $redEnvelopFrame=$('#redEnvelopFloatFrame');

// 回到顶部
(function() {
    let $backTopBtn=$redEnvelopFrame.find('.back-top');
    $(window).scrollTop()>($(window).height()/2)?$backTopBtn.fadeIn('fast'):$backTopBtn.fadeOut('fast');
    $(window).scroll(function() {
        if($(window).scrollTop()>($(window).height()/2)){
            $backTopBtn.fadeIn('fast');
        }else{
            $backTopBtn.fadeOut('fast');
        }
    });
    $backTopBtn.find('.nav-text').on('click', function(event) {//back top
        event.preventDefault();
        $('body,html').animate({scrollTop:0},'fast');
    });
})();

// **************** 投资计算器开始****************
(function() {
    let countForm=globalFun.$('#countForm');
    let $countFormOut=$(countForm).parents('.count-form');
    let $calBtn=$('.cal-btn',$redEnvelopFrame);
    let $closeBtn=$('.count-form .close-count',$redEnvelopFrame);
    let errorCountDom=$(countForm).find('.error-box');
    //弹出计算器
    $calBtn.on('click', function(event) {
        event.preventDefault();
        $(this).addClass('active');
        $countFormOut.show();
    });

    //关闭计算器
    $closeBtn.on('click', function(event) {
        event.preventDefault();
        var $navList=$('.fix-nav-list li',$redEnvelopFrame);
        $countFormOut.hide();
        $navList.removeClass('active');
    });

    //可拖拽计算器
    $countFormOut.dragging({
        move : 'both',
        randomPosition : false,
        hander: '.hander'
    });

    //充值计算器表单
    $("#resetBtn").on('click', function(event) {
        event.preventDefault();
        $(countForm).find('input:text').val('');
        $('#resultNum').text('0');
    });

    //验证表单
    let countValidator = new ValidatorForm();

    countValidator.add(countForm.money, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入投资金额！'
    }, {
        strategy: 'isNumber',
        errorMsg: '请输入有效的数字！'
    }]);

    countValidator.add(countForm.month, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入投资时长！'
    }, {
        strategy: 'isNumber',
        errorMsg: '请输入有效的数字！'
    }]);

    countValidator.add(countForm.bite, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入年化利率！'
    }, {
        strategy: 'isNumber',
        errorMsg: '请输入有效的数字！'
    }]);

    let reInputs=$(countForm).find('input:text');
    reInputs=Array.from(reInputs);

    for (var el of reInputs) {
        el.addEventListener("blur", function() {
            let errorMsg = countValidator.start(this);
            if(errorMsg) {
                errorCountDom.text(errorMsg);
            }
            else {
                errorCountDom.text('');
            }
        })
    }
    countForm.onsubmit = function(event) {
        event.preventDefault();
        let errorMsg;
        for(let i=0,len=reInputs.length;i<len;i++) {
            errorMsg = countValidator.start(reInputs[i]);
            if(errorMsg) {
                errorCountDom.text(errorMsg);
                break;
            }
        }
        if (!errorMsg) {
            //计算本息
            var moneyNum=Math.round(countForm.money.value),
                monthNum=Math.round(countForm.month.value),
                biteNum=Math.round(countForm.bite.value)/100,
                $resultNum=$('#resultNum'),
                resultNum=moneyNum+moneyNum*monthNum*biteNum*30*0.9/365;
            $resultNum.text(resultNum.toFixed(2));
        }
    }

})();


//意见反馈
(function() {
    let $feedbackConatiner=$('#feedbackConatiner');
    let feedForm=globalFun.$('#feedForm');
    let $typeList=$('.type-list',$feedbackConatiner);
    let imageCaptcha=globalFun.$('#imageCaptcha');

    //弹出已经反馈层
    $('.fix-nav-list .show-feed',$redEnvelopFrame).on('click', function(event) {
        event.preventDefault();
        //刷新验证码

        refreshCaptcha(imageCaptcha,'/feedback/captcha?');
        var $self=$(this);
        $self.addClass('active');
        $feedbackConatiner.show();
    });

    //模拟select下拉框
    $('dt,i',$typeList).on('click', function(event) {
        event.preventDefault();
        var $self=$(this),
            $list=$self.siblings('dd');
        $list.slideToggle('fast');
    });

    $('dd',$typeList).on('click', function(event) {
        event.preventDefault();
        var $self=$(this),
            $parent=$self.parent('.type-list'),
            $dt=$parent.find('dt'),
            $dd=$parent.find('dd');
        $dt.text($self.text()).attr('data-type',$self.attr('data-type'));
        $dd.hide();
    });

})();


// define(['jquery', 'layerWrapper','jquery.validate', 'jquery.validate.extension','drag'], function ($,layer) {
//     (function() {
//         var $redEnvelopFrame=$('#redEnvelopFloatFrame');
//         var $closeBtn=$('.count-form .close-count',$redEnvelopFrame),
//             $countForm=$('.count-form',$redEnvelopFrame),
//             $calBtn=$('.cal-btn',$redEnvelopFrame);
//

//
//         $("#countForm").validate({
//             debug:true,
//             rules: {
//                 money: {
//                     required: true,
//                     number: true
//                 },
//                 month: {
//                     required: true,
//                     number: true
//                 },
//                 bite: {
//                     required: true,
//                     number: true
//                 }
//             },
//             messages: {
//                 money: {
//                     required: '请输入投资金额！',
//                     number: '请输入有效的数字！'
//                 },
//                 month: {
//                     required: '请输入投资时长！',
//                     number: '请输入有效的数字！'
//                 },
//                 bite: {
//                     required: '请输入年化利率！',
//                     number: '请输入有效的数字！'
//                 }
//             },
//             submitHandler: function(form) {
//                 var moneyNum=Math.round($('#moneyNum').val()),
//                     monthNum=Math.round($('#monthNum').val()),
//                     biteNum=Math.round($('#biteNum').val())/100,
//                     $resultNum=$('#resultNum'),
//                     resultNum=moneyNum+moneyNum*monthNum*biteNum*30*0.9/365;
//                 $resultNum.text(resultNum.toFixed(2));
//             },
//             errorPlacement: function(error, element) {
//                 error.insertAfter(element.parent());
//             }
//         });
//         //close calculator
//         $closeBtn.on('click', function(event) {
//             event.preventDefault();
//             var $navList=$('.fix-nav-list li');
//             $countForm.hide();
//             $navList.removeClass('active');
//         });
//         //calculator show
//         $calBtn.on('click', function(event) {
//             event.preventDefault();
//             $(this).addClass('active');
//             $countForm.show();
//         });
//         //reset form
//         $("#resetBtn").on('click', function(event) {
//             event.preventDefault();
//             $countForm.find('.int-text').val('');
//             $('#resultNum').text('0');
//         });
//         //calculator drag
//         $countForm.dragging({
//             move : 'both',
//             randomPosition : false,
//             hander: '.hander'
//         });
//
//         //feedback click
//         $('.type-list dt,.type-list i').on('click', function(event) {
//             event.preventDefault();
//             var $self=$(this),
//                 $list=$self.siblings('dd');
//             $list.slideToggle('fast');
//         });
//
//         //give dt value by dd
//         $('.type-list dd').on('click', function(event) {
//             event.preventDefault();
//             var $self=$(this),
//                 $parent=$self.parent('.type-list'),
//                 $dt=$parent.find('dt'),
//                 $dd=$parent.find('dd');
//             $dt.text($self.text()).attr('data-type',$self.attr('data-type'));
//             $dd.hide();
//         });
//         //close tip
//         $('.feed-close',$redEnvelopFrame).on('click', function(event) {
//             event.preventDefault();
//             var $self=$(this),
//                 $showFeed=$('.fix-nav-list .show-feed',$redEnvelopFrame),
//                 $tipDom=$self.closest('.feedback-model');
//             $tipDom.hide();
//             $showFeed.removeClass('active');
//         });
//
//         //show feedback
//         $('.fix-nav-list .show-feed').on('click', function(event) {
//             event.preventDefault();
//             $("#captcha",$redEnvelopFrame).attr('src', '/feedback/captcha?' + new Date().getTime().toString());
//             var $self=$(this),
//                 $feedBack=$('.feedback-container',$redEnvelopFrame);
//             $self.addClass('active');
//             $feedBack.show();
//         });
//         //change captcha images
//         $('#captcha').on('click', function(event) {
//             event.preventDefault();
//             $(this).attr('src', '/feedback/captcha?' + new Date().getTime().toString());
//         });
//
//         //hide captcha error
//         $('#captchaText',$redEnvelopFrame).on('focusin', function(event) {
//             event.preventDefault();
//             $('#captchaError',$redEnvelopFrame).hide();
//         });
//
//         $("#feedForm").validate({
//             debug:true,
//             ignore: ".ignore",
//             rules: {
//                 content: {
//                     required: true,
//                     minlength:14,
//                     maxlength:200
//                 },
//                 captcha: {
//                     required: true,
//                     maxlength:5
//                 }
//             },
//             messages: {
//                 content: {
//                     required: '内容不能为空！',
//                     minlength:'文字限制最小为14',
//                     maxlength:'文字限制最大为200'
//                 },
//                 captcha: {
//                     required: '验证码不能为空！',
//                     maxlength:'请输入5位验证码！'
//                 }
//             },
//             submitHandler: function(form) {
//                 $.ajax({
//                     url: '/feedback/submit',
//                     type: 'POST',
//                     dataType: 'json',
//                     data: {
//                         'contact': $('#phoneText').val(),
//                         'type': $('.type-list').find('dt').attr('data-type'),
//                         'content': $('#textArea').val(),
//                         'captcha': $('#captchaText').val()
//                     }
//                 })
//                     .done(function(data) {
//                         if(data.success==true){
//                             $('#feedbackConatiner').hide();
//                             $('#feedForm').find('.int-text').val('');
//                             $('#captcha').trigger('click');
//                             $('#feedbackModel').show();
//                         }else{
//                             $('#captchaError').text('验证码错误！').show();
//                             $('#captcha').trigger('click');
//                         }
//                     })
//                     .fail(function() {
//                         layer.msg('请求失败，请重试！');
//                     });
//             },
//             errorPlacement: function(error, element) {
//                 element.parent().append(error);
//             }
//         });
//         //support placeholder
//         function placeholder(nodes, pcolor) {
//             if (nodes.length && !("placeholder" in document_createElement_x("input"))) {
//                 for (i = 0; i<nodes.length;i++){
//                     var self = nodes[i],
//                         placeholder = self.getAttribute('placeholder') || ''; self.onfocus = function() {
//                         if (self.value == placeholder) {
//                             self.value = '';
//                             self.style.color = "";
//                         }
//                     }
//                     self.onblur = function() {
//                         if (self.value == '') {
//                             self.value = placeholder;
//                             self.style.color = pcolor;
//                         }
//                     }
//                     self.value = placeholder; self.style.color = pcolor;
//                 }
//             }
//         }
//     })();
//
//
// });

