/**
 * Created by belen on 15/8/19.
 */
require(['jquery', 'jqueryPage', 'csrf','autoNumeric'], function ($) {
    $(function () {
        var amountInputElement = $(".text-input-amount");
        amountInputElement.autoNumeric("init");

        var pagesize = 10; //每页显示条数
        var loanId = $('.jq-loan-user').val();
        $(".pagination").createPage({
            pageCount: pageTotal,
            current: 1,
            backFn: function (pageCurrent) {
                //单击回调方法，p是当前页码
              var  API_LOAN_INVEST = '/loan/' + loanId + '/index/' + pageCurrent  + '/pagesize/' + pagesize;
                pageAjax(API_LOAN_INVEST);

            }
        });

        function pageAjax(url) {
            $.get(url, function (data) {
                if (data.status) {
                    var _result = data.recordDtoList;
                    var str = '';
                    for (var i = 0; i < _result.length; i++) {
                        var txtStatus ='';
                        if(_result[i]['autoInvest']){
                            txtStatus+='自动';
                            if(_result[i]['source']=='WEB'){
                                txtStatus +='<span class="icon-loan-ie"></span>';
                            }else if(_result[i]['source']=='IOS'){
                                txtStatus +='<span class="icon-loan-ios"></span>';
                            }else{
                                txtStatus +='<span class="icon-loan-Android"></span>';
                            }
                        }else{
                            txtStatus+='手动';
                            if(_result[i]['source']=='WEB'){
                                txtStatus +='<span class="icon-loan-ie"></span>';
                            }else if(_result[i]['source']=='IOS'){
                                txtStatus +='<span class="icon-loan-ios"></span>';
                            }else{
                                txtStatus +='<span class="icon-loan-Android"></span>';
                            }
                        }

                        str += '<tr><td>' + _result[i]['serialNo'] + '</td><td>' + _result[i]['loginName'] + '</td><td>' + _result[i]['amount'] + '</td><td>' + txtStatus + '</td><td>' + _result[i]['expectedRate'] + '</td><td>' + _result[i]['createdTime'] + '</td></tr>';
                    }
                    $('.table-list tbody').find('tr').remove();
                    $('.table-list tbody').append(str);
                }
            });

        }

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
        var tab_li = $('.nav li');
        tab_li.click(function () {
            var _index = $(this).index();
            $(this).addClass('current').siblings().removeClass('current');
            $('.loan-list .loan-list-con').eq(_index).show().siblings().hide();
        });
        $('.img-list li').click(function () {
            var _imgSrc = $(this).find('img').attr('src');
            $('.content img').attr('src', _imgSrc);
            $('.layer-box').show();
            return false;
        });
        $('.layer-wrapper').click(function () {
            $('.layer-box').hide();
        })

        function timer(intDiff) {
            window.setInterval(function () {
                var day = 0,
                    hour = 0,
                    minute = 0,
                    second = 0;//时间默认值
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

        $('.text-input-amount').blur(function(){
            var loanId = $('.hid-loan').val();
            var amount = $(this).val();
            var amountNeedRaised = Number($('.amountNeedRaised-i').text());

            if(amountNeedRaised < parseFloat(amount)){
                $('.loan-detail-error-msg').html("<i class='loan-detail-error-msg-li'>x</i>输入金额不能大于可投金额!");
                $('.loan-detail-error-msg').removeAttr("style");
                $('.btn-pay').attr('disabled', 'disabled').addClass('grey')

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
})