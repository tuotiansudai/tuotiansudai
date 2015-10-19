require(['jquery', 'csrf', 'autoNumeric'], function ($) {
    $(function () {
        var amountInputElement = $(".e-bank-amount");
        var amountElement = $(".e-bank-recharge .recharge-form input[name='amount']");
        var submitElement = $('.recharge-submit');

        amountInputElement.autoNumeric("init");

        amountInputElement.keyup(function () {
            var amount = parseFloat(amountInputElement.autoNumeric("get"));
            if (isNaN(amount) || amount === 0) {
                submitElement.addClass('grey').attr('disabled', true);
            } else {
                submitElement.removeClass('grey').attr('disabled', false);
            }
        });

        //select bank
        var bankElement = $('.e-bank-recharge ol li');

        bankElement.click(function () {
            var selectedBankElement = $(this).find('input');
            var bankCode = selectedBankElement.data('name');
            $('.selected-bank').val(bankCode);
        });

        //tab切换
        var _li = $('.banking ul li');
        _li.click(function () {
            var _this = $(this);
            var index = _this.index();
            var boxBanking = $('.box-banking .tab-box');
            if (_this.hasClass('tab-fast-pay')) {
                $.get(API_FAST_PAY, function (data) {
                    /*optional stuff to do after success */
                    if (data.bindCardStatus == "unbindCard") {
                        //未绑卡
                        $('.bind-card-layer').find('p').text("您还未绑定银行卡，请您绑定银行卡！");
                        $('.btn-box-layer').find('.now').attr('href', "/bind-card");
                        $('.bind-card-layer').show();
                        //location.href = 'http://localhost:8080/bind-card';
                    } else if (data.bindCardStatus == "commonBindCard") {
                        $('.bind-card-layer').find('p').text("您绑定的银行卡不支持快捷支付，是否换卡？");
                        $('.btn-box-layer').find('.now').attr('href', "/bind-card");
                        $('.bind-card-layer').show();
                        //  普卡绑
                        //location.href = 'http://localhost:8080/bind-card';

                    } else if (data.bindCardStatus == "specialBindCard") {
                        //  7卡未开
                        _this.addClass('active').siblings().removeClass('active');
                        boxBanking.eq(index).find('.recharge-bank').hide();
                        boxBanking.eq(index).find('.jq-open-fast-pay').show();
                        boxBanking.eq(index).show().siblings().hide();
                    } else if (data.bindCardStatus == "openFastPay") {
                        //  开通快捷支付
                        _this.addClass('active').siblings().removeClass('active');
                        boxBanking.eq(index).find('.recharge-bank').show()
                        boxBanking.eq(index).find('.jq-open-fast-pay').hide()
                        boxBanking.eq(index).show().siblings().hide();

                    }
                });

            } else {
                _this.addClass('active').siblings().removeClass('active');
                boxBanking.eq(index).show().siblings().hide();
            }

        });

        $('.close2,.cancel').click(function () {
            $('.bind-card-layer').hide();
        });

        //充值提交
        submitElement.click(function () {
            var amount = amountInputElement.autoNumeric("get");
            amountElement.val(amount);
            var content=$('#popRecharge');

            popWindow('登录到联动优势支付平台充值',content,{width:'560px'});

        });
        var initRadio=function($radio,$radioLabel) {
            var numRadio=$radio.length;
            if(numRadio) {
                $radio.each(function(key,option) {
                    var $this=$(this);
                    if($this.is(':checked')) {
                        $this.next('label').addClass('checked');
                    }
                    $this.next('label').click(function() {
                        var $thisLab=$(this);
                        if(!/checked/.test(this.className)) {
                            $radioLabel.removeClass('checked');
                            $thisLab.addClass('checked');
                        }
                    });
                });

            }
        };
        var checkBoxInit=function($checkbox,$label) {
            var numCheckbox=$checkbox.length;
            if(numCheckbox) {
                $checkbox.each(function(key,option) {
                    var $this=$(this);
                    if($this.is(':checked')) {
                        $this.next('label').addClass('checked');
                    }
                    $this.next('label').click(function() {
                        var $thisLab=$(this);
                        if(/checked/.test(this.className)) {
                            $thisLab.removeClass('checked');
                        }
                        else {
                            $thisLab.addClass('checked');
                        }
                    });
                });

            }
        };
        var popWindow=function(title,content,size) {
            if(!$('.popWindow').length) {
                var popW=[];
                popW.push('<div class="popWindow">');
                popW.push('<div class="ecope-overlay"></div>');
                popW.push('<div class="ecope-dialog">');
                popW.push('<div class="dg_wrapper">');

                popW.push('<div class="hd"><h3>'+title+' ' +
                    '<em class="close" ></em></h3></div>');
                popW.push('<div class="bd">sss</div>');

                popW.push('</div></div></div>');
                $('body').append(popW.join(''));
                var $popWindow=$('.ecope-dialog'),
                size= $.extend({width:'560px'},size);
                $popWindow.css({
                    width:size.width
                });
                var adjustPOS=function() {
                    var scrollHeight=document.body.scrollTop || document.documentElement.scrollTop,
                        pTop=$(window).height()-$popWindow.height(),
                        pLeft=$(window).width()-$popWindow.width();
                    $popWindow.css({'top':pTop/2+scrollHeight,left:pLeft/2});
                    $popWindow.find('.bd').empty().append(content);
                }
                adjustPOS();
                $(window).resize(function() {
                    adjustPOS();
                });
                var mousewheel = document.all?"mousewheel":"DOMMouseScroll";
                $(window).bind('mousewheel',function() {
                    adjustPOS();
                })
            }
            else {
                $('.ecope-overlay,.popWindow').show();
            }

            $popWindow.delegate('.close','click',function() {
                $('.ecope-overlay,.popWindow').hide();
            })
        }

        if($('#btnAuthority').length) {
            var $btnAuthority=$('#btnAuthority');
            $btnAuthority.click(function() {
                var content='<div cass="auto-invest"><button id="finishAuthor" class="btn btn-normal">已完成授权</button></div>';
                popWindow('自动投标授权',content,{
                    width:'450px'
                });

                $('body').delegate('#finishAuthor','click',function() {
                    location.href='http://localhost:8080/investor/auto-invest/plan';
                });

            });
        }
        //switch button for plan
        if($('#planSwitchDom').length) {
            var $planSwitchDom=$('#planSwitchDom'),
                $btnSwitch=$('.switchBtn',$planSwitchDom),
                $radioLabel=$('label.radio',$btnSwitch),
                $projectLimit=$('.projectLimit',$planSwitchDom),
                $radio=$('input[type="radio"]',$planSwitchDom),
                $checkbox=$('input[type="checkbox"]',$planSwitchDom),
                $checkboxLabel=$('label.checkbox',$planSwitchDom);
            //init radio and checkbox
            initRadio($radio,$radioLabel);
            checkBoxInit($checkbox,$checkboxLabel);

            $radioLabel.click(function(index) {
                var $this=$(this),
                    value=$this.prev('input').val();
                if(value==1) {
                    $planSwitchDom.find('dl').show().siblings().show();
                }
                else {
                    $planSwitchDom.find('dl').first().show().siblings().hide();
                }

            });
            //select project limit
            $projectLimit.find('span').click(function() {
                var $this=$(this);
                $this.toggleClass('active');
            });
        }


    });
});