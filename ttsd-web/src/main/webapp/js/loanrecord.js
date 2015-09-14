require(['jquery', 'daterangepicker', 'moment', 'csrf'], function ($) {
    //初始化页面
    var oDate = new Date();
    var oYear = oDate.getFullYear();
    var oMonth = oDate.getMonth();
    var oToday = oDate.getDate();
    var _page,_hasNextPage,_hasPreviousPage;  //define pages
    // 页面初始化日期 条件筛选1个月
    $('#daterangepicker')
        .dateRangePicker({separator: ' ~ '})
        .val((oYear + '-' + parseInt(oMonth+1) + '-' + oToday) + '~' + (oYear + '-' + parseInt(oMonth+1) + '-' + oToday));
    //ajax require
    function getAjax(page) {
        var dates = $('#daterangepicker').val().split('~');
        var startDay = $.trim(dates[0]);
        var endDay = $.trim(dates[1]);
        var selectedType = $('.query-type').find(".current").attr('data-value');
        //$(".query_type strong").css("opacity", '1');
        var rec_typestr = '';
        if (selectedType) {
            rec_typestr = "&type=" + selectedType;
        }
        if (startDay == '' || startDay == 'undefined') {
            var url = "/tuotian/ttsd-web/design/static/jsons/table.json?page=" + page + rec_typestr;
        } else {
            var url = "/tuotian/ttsd-web/design/static/jsons/table.json?startday=" + startDay + "&endday=" + endDay + "&page=" + page + rec_typestr;
        }
        $.get(url, function (res) {
            if (res.status === 'success') {
                //$(".query-type strong").css("display", 'none');
                //var ret = Mustache.render(dealtableTpl, res.data);
                //$('.result').html(ret);
                //_page = res.data['currentPage'];
                //_hasNextPage = res.data['hasNextPage'];
                //_hasPreviousPage = res.data['hasPreviousPage'];
                //console.log('分页'+ _page);
            }
        });
    }
    getAjax(1);

    //select calendar
    $(".start-end .jq-n").click(function () {
        $(this).addClass("current").siblings(".jq-n").removeClass("current");
        filterChanged();

    });

    // options
    $(".query-type .jq-n").click(function () {
        $(this).addClass("current").siblings(".jq-n").removeClass("current");
        getAjax(1);

    });

    //define calendar
    $('.apply-btn').click(function () {
        getAjax(1);
    })

    function filterChanged() {
        var _year = oYear,
            _month = oMonth,
            _today = oToday,
            _days = $(".start-end span.current").attr('day');
        //console.log(_days);
        if(_days){
            if(_days < oToday){
                if(parseInt(_days) == 1){
                    _month = oMonth+1;
                    _today = oToday;

                }else{
                    _month = oMonth+1;
                    _today = oToday - _days;

                }
            }else if(_days == oToday) {

                _today = 1;

            }else{
                if(_days == 180){ //  six month
                    if(oMonth+1>6){
                        _month = parseInt(oMonth)-5;

                    }else{
                        _month = 12+oMonth-5;
                        _year = oYear -1;
                    }

                }else if (_days == 30){ // one month
                    if(oMonth+1 == 2){
                        _month = 12;
                        _year = oYear -1;
                    }else{
                        _month = oMonth;
                    }
                }else{ //一周
                    if(oMonth+1 == 2){
                        _month = 12;
                        _year = oYear -1;

                    }else{
                        _month = oMonth;
                        _year = oYear ;

                    }
                    _days = _days || 0;
                    oToday = oToday || 0;
                    _today = 30+ oToday - _days;
                }
            }
            $('#daterangepicker').val((_year + '-' + _month + '-' + _today) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday));

        }else{
            $('#daterangepicker').val('');
        }
        getAjax(1);
    }

    //还款计划
    $('.plan').click(function () {
        var dataLoan = $(this).attr('data-loan');
        var str = '';
        $.get(dataLoan,function(res){
            if(res.status){
                var _res = res.data;
                for(var i = 0; i< _res.length;i++){
                    if(_res[i]['isEnabled']){
                        var txt = '<a href = "">待还款</a>';
                    }else{
                        var txt = '';
                    }
                   var total =  _res[i]['corpus'] +_res[i]['actualInterest']+_res[i]['expectedInterest']+_res[i]['defaultInterest'];
                    str+="<tr><td>"+
                        _res[i]['period']
                        +"</td><td>"+
                        _res[i]['corpus']
                        +"</td><td>"+
                        _res[i]['expectedInterest']
                        +"</td><td>"+
                        _res[i]['actualInterest']
                        + "</td> <td>"+
                        _res[i]['defaultInterest']
                        +"</td> <td>"+
                        total
                        +"</td> <td>"+
                        _res[i]['repayDate']
                        +"</td> <td>"+
                        _res[i]['actualRepayDate']
                        +"</td><td>"+
                        _res[i]['status']
                        +"</td><td>"+ txt+"</td></tr>";

                }
                $('.table-list tbody').find('tr').remove();
                $('.table-list tbody').append(str);
            }

        });

        $('.layer-box').show();
        return false;
    });
    $('.layer-box .close').click(function () {
        $('.layer-box').hide();
        return false;
    });
    $('.layer-fix').click(function () {
        $('.layer-box').hide();
    });


});