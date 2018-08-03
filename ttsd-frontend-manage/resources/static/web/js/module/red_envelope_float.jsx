require("webStyle/module/red_envelop_float.scss");
require.ensure([],function() {
    require('webJsModule/drag');

    let commonFun= require('publicJs/commonFun');
    let ValidatorObj=require('publicJs/validator');
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
        let countValidator = new ValidatorObj.ValidatorForm();

        countValidator.add(countForm.money, [{
            strategy: 'isNonEmpty',
            errorMsg: '请输入投资金额！'
        }, {
            strategy: 'isNumber',
            errorMsg: '请输入有效的数字！'
        }]);

        countValidator.add(countForm.day, [{
            strategy: 'isNonEmpty',
            errorMsg: '请输入投资期限！'
        }, {
            strategy: 'isNumber',
            errorMsg: '请输入有效的数字！'
        }]);

        countValidator.add(countForm.rate, [{
            strategy: 'isNonEmpty',
            errorMsg: '请输入年化利率！'
        }, {
            strategy: 'isNumber',
            errorMsg: '请输入有效的数字！'
        }]);

        let reInputs=$(countForm).find('input:text');
        for(let i=0,len=reInputs.length; i<len;i++) {
            reInputs[i].addEventListener("blur", function() {
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
                var moneyNum = Math.floor(countForm.money.value * 100),
                    dayNum = Math.floor(countForm.day.value),
                    rateNum = Math.floor(countForm.rate.value * 10),
                    $resultNum = $('#resultNum');

                var period = dayNum % 30 == 0 ? 30 : dayNum % 30,
                    resultNum = parseFloat((moneyNum / 100).toFixed(2)),
                    interest, fee;

                while (dayNum > 0) {
                    interest = parseFloat((Math.floor(moneyNum * rateNum * period / 365000) / 100).toFixed(2));
                    fee = parseFloat((Math.floor(interest * 10) / 100).toFixed(2));
                    resultNum += (interest - fee);
                    dayNum -= period;
                    period = 30;
                }
                $resultNum.text(resultNum.toFixed(2));
            }
        }

    })();


//意见反馈
    (function() {
        let $feedbackConatiner=$('#feedbackConatiner');
        let feedForm=globalFun.$('#feedForm');
        let $typeList=$('.type-list',$feedbackConatiner);
        let imageCaptchaFeed=globalFun.$('#imageCaptchaFeed');
        let errorFeedDom=$(feedForm).find('.error-box');
        //刷新验证码
        $(imageCaptchaFeed).on('click', function(event) {
            event.preventDefault();
            commonFun.refreshCaptcha(this,'/feedback/captcha');
        });

        //弹出意见反馈层
        $('.fix-nav-list .show-feed',$redEnvelopFrame).on('click', function(event) {
            event.preventDefault();
            //刷新验证码
            commonFun.refreshCaptcha(imageCaptchaFeed,'/feedback/captcha');
            var $self=$(this);
            $self.addClass('active');
            $feedbackConatiner.show();
        });
        //关闭意见反馈层
        $('.feed-close',$redEnvelopFrame).on('click', function(event) {
            event.preventDefault();
            var $self=$(this),
                $showFeed=$('.fix-nav-list .show-feed',$redEnvelopFrame),
                $tipDom=$self.closest('.feedback-model');
            $tipDom.hide();
            $showFeed.removeClass('active');
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

        //验证表单
        let feedbackValidator = new ValidatorObj.ValidatorForm();

        feedbackValidator.add(feedForm.content, [{
            strategy: 'isNonEmpty',
            errorMsg: '内容不能为空！'
        }, {
            strategy: 'minLength:14',
            errorMsg: '文字限制最小为14！'
        },{
            strategy: 'maxLength:200',
            errorMsg: '文字限制最大为200！'
        }],true);

        feedbackValidator.add(feedForm.contact, [{
            strategy: 'isMobile',
            errorMsg: '请输入正确的手机号！'
        }],true);


        feedbackValidator.add(feedForm.captcha, [{
            strategy: 'isNonEmpty',
            errorMsg: '验证码不能为空！'
        }, {
            strategy: 'isNumber:5',
            errorMsg: '请输入5位验证码！'
        }],true);

        let feedInputs=$(feedForm).find('input:text,textarea');
        feedInputs=Array.from(feedInputs);

        for (var el of feedInputs) {
            el.addEventListener("keyup", function() {
                errorFeedDom.text('');
                feedbackValidator.start(this);
            })
        }

        feedForm.onsubmit = function(event) {
            event.preventDefault();

            let errorMsg;
            for(let i=0,len=feedInputs.length;i<len;i++) {
                errorMsg = feedbackValidator.start(feedInputs[i]);
                if(errorMsg) {
                    break;
                }
            }
            if (!errorMsg) {
                //提交表单
                let type=$feedbackConatiner.find('.type-list dt').attr('data-type');
                let feedSubmit=$(feedForm).find(':submit');
                feedForm.type.value = type;
                commonFun.useAjax({
                    url:"/feedback/submit",
                    type:'POST',
                    data:$(feedForm).serialize(),
                    beforeSend:function() {
                        feedSubmit.prop('disabled',true);
                    }
                },function(data) {
                    commonFun.refreshCaptcha(imageCaptchaFeed,'/feedback/captcha');
                    feedSubmit.prop('disabled',false);
                    if(data.success) {
                        $feedbackConatiner.hide();
                        $(feedForm).find(':text,textarea,input[name="type"]').val('');
                        $('#feedbackModel').show();
                        errorFeedDom.text('');
                    }
                    else {
                        errorFeedDom.text(data.data.message);
                    }
                });
            }
        }

    })();
},'redEnvelope');



