/**
 * Created by belen on 15/8/19.
 */
require(['jquery', 'csrf'], function ($) {
    $(function () {
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


        // page
        var btn_next = $('.pagination').find('.next');
        var btn_prev = $('.pagination').find('.prev');
        var loanId = $('.jq-loan-user').val();
        var pagesize = 2;
        var API_LOAN_INVEST = '';
        var AUTO ='auto';
        btn_next.click(function () {
            var index = $('.index-page').text();
            index++;
            API_LOAN_INVEST = '/loan/' + loanId + '/index/' + index + '/pagesize/' + pagesize;
            pageAjax(API_LOAN_INVEST);
        });
        function pageAjax(url) {
            $.get(url, function (data) {
                if (data.status) {
                    var _result = data.recordDtoList;
                    var str = '';
                    for (var i = 0; i < _result.length; i++) {
                        str += '<tr><td>' + (i + 1) + '</td><td>' + _result[i]['loginName'] + '</td><td>' + _result[i]['amount'] + '</td><td>' + AUTO + '</td><td>' + _result[i]['expectedRate'] + '</td><td>' + _result[i]['createdTime'] + '</td></tr>';
                    }
                    $('.table-list tbody').find('tr').remove();
                    $('.table-list tbody').append(str);
                }
            });

        }


    });
})