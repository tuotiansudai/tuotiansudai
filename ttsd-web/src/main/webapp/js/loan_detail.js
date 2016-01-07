require(['jquery', 'pagination', 'mustache', 'text!/tpl/loan-invest-list.mustache', 'layerWrapper','csrf', 'autoNumeric'], function ($, pagination, Mustache, investListTemplate, layer) {

        var $loanDetail = $('.loan-detail-content'),
            amountInputElement = $(".text-input-amount",$loanDetail),
            $accountInfo = $('.account-info',$loanDetail),
            $btnLookOther = $('.btn-pay', $accountInfo),
            tabs = $('.loan-nav li'),
            $loanlist = $('.loan-list', $loanDetail),
            $imageList=$('#picListBox'),
            $experienceTicket=$('.experience-ticket',$loanDetail),
            $couponInterest=$('.experience-interest',$loanDetail),
            paginationElement = $('.pagination',$loanDetail),
            $error=$('.errorTip');
        amountInputElement.autoNumeric("init");
        layer.ready(function(){
            layer.photos({
                photos: '#picListBox'
            });
        });

        var loadLoanData = function (currentPage) {
            var requestData = {index: currentPage || 1};
            paginationElement.loadPagination(requestData, function (data) {
                if (data.status) {
                    var html = Mustache.render(investListTemplate, data);
                    $('.loan-list-con table').html(html);
                }
            });
        };

        //pageCount：总页数
        //current：当前页
        //初始化标的比例（进度条）
       // var java_point = 15; //后台传递数据
        if (java_point <= 50) {
            $('.chart-box .rount').css('webkitTransform', "rotate(" + 3.6 * java_point + "deg)");
            $('.chart-box .rount2').hide();
        } else {
            $('.chart-box .rount').css('webkitTransform', "rotate(180deg)");
            $('.chart-box .rount2').show();
            $('.chart-box .rount2').css('webkitTransform', "rotate(" + 3.6 * (java_point - 50) + "deg)");
        }

        // tab select
        $loanlist.find('.loan-list-con').eq(0).show();
        tabs.click(function () {
            var self = $(this);
            self.addClass('active').siblings('li').removeClass('active');
            var index = self.index();
            if (index === 1) {
                loadLoanData();
            }
            $loanlist.find('.loan-list-con').eq(index).show().siblings('.loan-list-con').hide();
        });

        function timer(intDiff) {
            window.setInterval(function () {
                var day = 0,
                    hour = 0,
                    minute = 0,
                    second = 0;
                if(intDiff <= 1800){

                    if (intDiff > 0) {
                        minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
                        second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
                    }else{
                        $btnLookOther.prop('disabled', false);
                        $btnLookOther.html('马上投资');
                        $accountInfo.find('.time-item').remove();
                        $accountInfo.find('.expected-interest').parents('dd').removeClass('hide');
                    }
                    if (minute <= 9) minute = '0' + minute;
                    if (second <= 9) second = '0' + second;
                    $('#minute_show').html(minute + '分');
                    $('#second_show').html(second + '秒');
                    intDiff--;
                }
            }, 1000);
        }

        if($('.title').data("loan-status") == 'PREHEAT' ){
            timer(intDiff);
        }

        function showInterest(interest){
            var $checkBox = $experienceTicket.find(':checkbox');
            if($checkBox.is(':checked')){
                plusCouponInterest();
            }else{

            }
        }

        function plusCouponInterest(){
            var couponInterest = parseFloat($couponInterest.html());
            var interest = Math.round((couponInterest + amount)*100);

        }

        if(amountInputElement.length) {
            var $ticketList=$('.ticket-list'),
                $useExperienceTicket=$('#use-experience-ticket'),
                initInvestAmount=0;
            if($experienceTicket.is(':hidden')) {
                $('.account-list').find('dd').last().css({'margin-top':'20px'});
            }
            else {
                $experienceTicket.find(':checkbox').on('click',function() {
                    var $thischeckBox=$(this),isChecked;
                    isChecked=$thischeckBox.is(':checked');
                    if(isChecked) {
                        $experienceTicket.next('dd.experience-revenue').show();
                        plusCouponInterest();
                    } else {
                        $experienceTicket.next('dd.experience-revenue').hide();

                    }
                });
            }
            var calExpectedInterest = function(isFirstLoad){
                var loanId = $('.hid-loan').val(),
                    amount = parseFloat(amountInputElement.autoNumeric("get"));
                if(isNaN(amount)) {
                    amount='0.00';
                }
                var amountNeedRaised = $('form .amountNeedRaised-i').text();

                if(Number(amountNeedRaised) < Number(amount) || amount<=0){
                    var tipmsg;
                    if(amount<=0) {
                        tipmsg='输入金额不能等于0!';
                    }
                    else {
                        tipmsg='输入金额不能大于可投金额!';
                    }
                    layer.tips('<i class="fa fa-times-circle"></i>'+tipmsg, '.text-input-amount', {
                        tips: [1,'#ff7200'] ,
                        time:0
                    });
                    $btnLookOther.prop('disabled', true);
                    return;
                }
                else {
                    layer.closeAll('tips');
                    $btnLookOther.prop('disabled', false);
                }
                $ticketList.find('.ticket-title').filter(function(key,option) {
                    return $(option).attr('data-income')>amount;
                }).parents('li').addClass('disabled');
                $.ajax({
                    url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + amount,
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function(amount){
                    if(!isFirstLoad){
                        layer.closeAll('tips');
                    }
                    showInterest(amount);
                    $btnLookOther.prop('disabled', false);
                });
            };
            if(typeof user_can_invest !== 'undefined') {
                calExpectedInterest(true);
            }
            amountInputElement.blur(function(){
                calExpectedInterest(false);
            });
            amountInputElement.keypress(function(event) {
                    $ticketList.find('input').prop('checked',false);
                    $useExperienceTicket.find('span').text('请点击选择优惠券');


            });
            $('form').submit(function(){
                var frm = $(this);
                if(frm.attr('action') === '/invest'){
                    if(typeof user_can_invest === 'undefined'){
                        location.href = '/login?redirect='+encodeURIComponent(location.href);
                        return false;
                    }
                    var amount=parseFloat(amountInputElement.autoNumeric("get"));
                    if(isNaN(parseFloat(amount))) {
                        return false;
                    }

                    var accountAmount = parseFloat($('form .account-amount').text());
                    if(amount > accountAmount){
                        location.href = '/recharge';
                        return false;
                    }
                    amountInputElement.val(amount);
                    return true;
                }
                return true;
            });
            $useExperienceTicket.click(function(event) {
                var $this=$(this);
                if($this.hasClass('disabled')) {
                    return false;
                }
                $ticketList.toggleClass('hide');
                $this.find('.fa-sort-down').toggleClass('hide').siblings('.fa-sort-up').toggleClass('hide');
                if (event.stopPropagation) {
                    event.stopPropagation();
                }
                else if (window.event) {
                    window.event.cancelBubble = true;
                }

            });
            $ticketList.find('input').click(function(event) {
                var text= $.trim($(event.target).next().find('.ticket-title').text());
                $useExperienceTicket.find('span').text(text);
            });

            $('body').click(function(e) {
                var event = e || window.event,
                    target = event.srcElement || event.target;

                if(!$ticketList.hasClass('hide') && $(target).parents('.ticket-list').length==0) {
                    $ticketList.addClass('hide');
                }
            });
            $('#use-experience-ticket').click(function(event) {
                var $this=$(this),
                    userCouponId=this.value,
                    boolSelect=$this.prop('checked'),
                    amount = parseFloat(amountInputElement.autoNumeric("get"));

                if(boolSelect) {
                    $.ajax({
                        url: '/coupon-is-available/coupon/' + userCouponId + '/amount/' + amount,
                        type: 'get',
                        dataType: 'json',
                        contentType: 'application/json; charset=UTF-8'
                    }).done(function(dataBool){
                        if(!dataBool) {
                            $this.prop('checked',false);
                            layer.tips('<i class="fa fa-times-circle"></i>投资金额不少于'+$this.attr('data-amount')+'才可使用此劵', '.experience-ticket', {
                                tips: [1,'#ff7200'] ,
                                time:0
                            });
                            $('.experience-revenue').hide();
                            minusCouponInterest();
                        }
                    });

                }
            });
        }

        if($error.length) {
            layer.tips('<i class="fa fa-times-circle"></i>'+$error.text(), '.text-input-amount', {
                tips: [1,'#ff7200'] ,
                time:0
            });
        }
});