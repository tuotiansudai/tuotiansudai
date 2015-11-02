require(['jquery', 'pagination', 'mustache', 'text!/tpl/loan-invest-list.mustache', 'csrf', 'autoNumeric'], function ($, pagination, Mustache, investListTemplate) {

    $(function () {
        var amountInputElement = $(".text-input-amount");
        amountInputElement.autoNumeric("init");

        var paginationElement = $('.pagination');

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
        //var java_point = 15; //后台传递数据
        if (java_point <= 50) {
            $('.chart-box .rount').css('webkitTransform', "rotate(" + 3.6 * java_point + "deg)");
            $('.chart-box .rount2').hide();
        } else {
            $('.chart-box .rount').css('webkitTransform', "rotate(180deg)");
            $('.chart-box .rount2').show();
            $('.chart-box .rount2').css('webkitTransform', "rotate(" + 3.6 * (java_point - 50) + "deg)");
        }

        // tab select
        var tabs = $('.nav li');
        tabs.click(function () {
            var self = $(this);
            tabs.removeClass('active');
            self.addClass('active');
            var index = self.index();
            if (index === 1) {
                loadLoanData();
            }
            $('.loan-list .loan-list-con').removeClass('active').eq(index).addClass('active');
        });

        $('.img-list li').click(function () {
            var _imgSrc = $(this).find('img').attr('src');
            $('.content img').attr('src', _imgSrc);
            $('.layer-box').show();
            return false;
        });

        $('.layer-wrapper').click(function () {
            $('.layer-box').hide();
        });

        function timer(intDiff) {
            window.setInterval(function () {
                var day = 0,
                    hour = 0,
                    minute = 0,
                    second = 0;//时间默认值
                if(intDiff <= 1800){

                    if (intDiff > 0) {
                        // day = Math.floor(intDiff / (60 * 60 * 24));
                        // hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
                        minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
                        second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
                    }else{
                        $('.btn-pay').removeClass('grey').removeAttr('disabled').html('马上投资');
                    }
                    if (minute <= 9) minute = '0' + minute;
                    if (second <= 9) second = '0' + second;
                    // $('#day_show').html(day+"天");
                    // $('#hour_show').html('<s id="h"></s>'+hour+'时');
                    $('#minute_show').html('<s></s>' + minute + '分');
                    $('#second_show').html('<s></s>' + second + '秒');
                    intDiff--;
                }
            }, 1000);
        }

        $(function () {
            if($('#loanStatus').val() == 'PREHEAT' ){
                timer(intDiff);
            }
        });

        $('form .btn-pay[type="submit"]').click(function(){
            var investAmount = Number($('form input[name="amount"]').val());
            var accountAmount = Number($('form i.account-amount').text());
            if(investAmount > accountAmount){
                location.href = '/recharge';
                return false;
            }
            return true;
        });

        amountInputElement.blur(function(){
            var loanId = $('.hid-loan').val();
            var amount = $(this).val();
            var amountNeedRaised = Number($('.amountNeedRaised-i').text());

            if(amountNeedRaised < parseFloat(amount)){
                $('.loan-detail-error-msg').html("<i class='loan-detail-error-msg-li'>x</i>输入金额不能大于可投金额!").removeAttr("style");
                $('.btn-pay').attr('disabled', 'disabled').addClass('grey');
                return;
            }
            $.ajax({
                url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + amount,
                type: 'get',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(amount){
                $('.loan-detail-error-msg').hide();
                $('.expected-interest').html(amount);
                $('.btn-pay').removeClass('grey').removeAttr('disabled');
            });
        });
    });
});