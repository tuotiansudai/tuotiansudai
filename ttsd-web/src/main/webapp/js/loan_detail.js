require(['jquery', 'pagination', 'mustache', 'text!/tpl/loan-invest-list.mustache', 'layerWrapper','csrf', 'autoNumeric'], function ($, pagination, Mustache, investListTemplate, layer) {

        var $loanDetail = $('.loan-detail-content'),
            amountInputElement = $(".text-input-amount"),
            $accountInfo = $('.account-info'),
            $btnLookOther = $('.btn-pay', $accountInfo),
            $errorDom = $('.error', $accountInfo),
            tabs = $('.loan-nav li'),
            $loanlist = $('.loan-list', $loanDetail),
            $imageList=$('#picListBox'),
            paginationElement = $('.pagination');
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

        if($('#loanStatus').val() == 'PREHEAT' ){
            timer(intDiff);
        }

        if(amountInputElement.length) {
            var calExpectedInterest = function(isFirstLoad){
                var loanId = $('.hid-loan').val(),
                    amount = parseFloat(amountInputElement.autoNumeric("get"));
                if(isNaN(amount)) {
                    amount='0.00';
                }
                var amountNeedRaised = $('form .amountNeedRaised-i').text();
                if(Number(amountNeedRaised) < Number(amount)){
                    $errorDom.html("<i class='fa fa-times-circle'></i>输入金额不能大于可投金额!").removeAttr("style");
                    $btnLookOther.prop('disabled', true);
                    return;
                }
                $.ajax({
                    url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + amount,
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function(amount){
                    if(!isFirstLoad){
                        $errorDom.hide();
                    }
                    $('.expected-interest').html(amount);
                    $btnLookOther.prop('disabled', false);
                });
            };
            calExpectedInterest(true);
            amountInputElement.blur(function(){calExpectedInterest(false);});

            $('form').submit(function(){
                var frm = $(this);
                if(frm.attr('action') === '/invest'){
                    if(typeof user_can_invest === 'undefined'){
                        location.href = '/login?redirect='+encodeURIComponent(location.href);
                        return false;
                    }
                    var amount=parseFloat(amountInputElement.autoNumeric("get"));
                    if(isNaN(parseFloat(amount))) {
                        $errorDom.html("<i class='fa fa-times-circle'></i>请正确输入投资金额").removeAttr("style");
                        return false;
                    }
                    var investAmount = parseFloat(amount);
                    var accountAmount = parseFloat($('form .account-amount').text());
                    if(investAmount > accountAmount){
                        location.href = '/recharge';
                        return false;
                    }
                    return true;
                }
                return true;
            });
        }


});