/**
 * Created by zhaoshuai on 2015/6/18.
 */
require.config({
    baseUrl: "../js",//此处存放文件路径
    paths: {
        "jquery": "jquery-1.10.1.min",//此处存放baseURL路径下要引入的JS文件
        "mustache":"mustache.js"
    }
});
require(['jquery'], function ($) {
    var oDate = new Date();
    var oYear = oDate.getFullYear();
    var oMonth = oDate.getMonth();
    var oToday = oDate.getDate();
    var month = oDate.setMonth(oDate.getMonth() - 6);
    var oWeek = new Date();
    var week = oWeek.setDate(oWeek.getDate() - 7);

    $('.rec_today').on('click', function () {
        $('.starttime').val(oYear + '-' + parseInt(oMonth + 1) + '-' + oToday);
        $('.endtime').val(oYear + '-' + parseInt(oMonth + 1) + '-' + oToday);
        $.ajax({
            url: '/static/jsons/table.json',
            type: 'GET',
            dataType: 'json',
            beforeSend: function () {
                $('.query_type strong').css({'opacity':1});
            },
            success: function (response) {
                if (response.status === 'success') {
                    $('.query_type strong').css({'opacity':0});
                    $('.rec_tab tbody').append(Mustache.render(template,{data:rows}));
                    if(response.data.hasNextPage ){
                        $('.page_record').css({'display':'block'});
                    }
                }else{
                    alert('2');
                }
            },
            error: function () {
                if(status==500){
                    alert('服务器错误!');
                }
            }
        })
    });
    $('.rec_month').on('click', function () {
        $('.starttime').val(oYear + '-' + oMonth + '-' + oToday);
        $('.endtime').val(oYear + '-' + parseInt(oMonth + 1) + '-' + oToday);
        $.ajax({
            url: '/static/jsons/mobile.json',
            type: 'GET',
            dataType: 'json',
            beforeSend: function () {
                $('.query_type strong').css({'opacity':1});
            },
            success: function (response) {
                if (response.status === 'success') {
                    $('.query_type strong').css({'opacity':0});
                }else{
                    alert('2');
                }
            },
            error: function () {
                if(status==500){
                    alert('服务器错误!');
                }
            }
        })
    });
    $('.rec_sixmonth').on('click', function () {
        $('.starttime').val(oDate.toLocaleDateString());
        $('.endtime').val(oYear + '-' + parseInt(oMonth + 1) + '-' + oToday);
        $.ajax({
            url: '/static/jsons/table.json',
            type: 'GET',
            dataType: 'json',
            beforeSend: function () {
                $('.query_type strong').css({'opacity':1});
            },
            success: function (response) {
                if (response.status === 'success') {
                    $('.query_type strong').css({'opacity':0});
                }else{
                    alert('2');
                }
            },
            error: function () {
                if(status==500){
                    alert('服务器错误!');
                }
            }
        })
    });
    $('.rec_all').on('click', function () {
        $('.starttime').val('');
        $('.endtime').val('');
        $.ajax({
            url: '/static/jsons/mobile.json',
            type: 'GET',
            dataType: 'json',
            beforeSend: function () {
                $('.query_type strong').css({'opacity':1});
            },
            success: function (response) {
                if (response.status === 'success') {
                    $('.query_type strong').css({'opacity':0});
                }else{
                    alert('2');
                }
            },
            error: function () {
                if(status==500){
                    alert('服务器错误!');
                }
            }
        })
    });
    $('.rec_week').on('click', function () {
        $('.starttime').val(oWeek.toLocaleDateString());
        $('.endtime').val(oYear + '/' + parseInt(oMonth + 1) + '/' + oToday);
        $.ajax({
            url: '/static/jsons/mobile.json',
            type: 'GET',
            dataType: 'json',
            beforeSend: function () {
                $('.query_type strong').css({'opacity':1});
            },
            success: function (response) {
                if (response.status === 'success') {
                    $('.query_type strong').css({'opacity':0});
                }else{
                    alert('2');
                }
            },
            error: function () {
                if(status==500){
                    alert('服务器错误!');
                }
            }
        })
    })
    $('.Wdate').on('focus', function () {
        WdatePicker({maxDate: '%y/%M/%d'});
    });
    var active = $('.start_end span');
    var rec_acative = $('.rec_type li');
    var len = rec_acative.length;
    for (var i = 0; i < active.length; i++) {
        active[i].onclick = function () {
            for (var i = 0; i < active.length; i++) {
                active[i].className = '';
            }
            this.className = 'active';
        }
    }
    for (var i = 0; i < len; i++) {
        rec_acative[i].onclick = function () {
            for (var i = 0; i < len; i++) {
                rec_acative[i].className = '';
            }
            this.className = 'active';
        }
    }

})