/**
 * Created by belen on 15/8/27.
 */
$(function () {
    //初始化渲染
    document.getElementById("jq-selected").selectedIndex = 0;
    var jqDate = $('#jq-date');
    $.post(API_URL, {period: 'week'}, function (data) {
        /*optional stuff to do after success */
        allData(data);
    });
    var allData = function (data) {
        //渲染全国数据
        var _chinaArr = data.areaInvestments.CHINA;
        var _china = _chinaArr.length;

        //华北区域
        var CHINA_NORTH_Arr = data.areaInvestments.CHINA_NORTH;
        var _CHINA_NORTH = CHINA_NORTH_Arr.length;

        var CHINA_EAST_Arr = data.areaInvestments.CHINA_EAST;
        var _CHINA_EAST = CHINA_EAST_Arr.length;

        var CHINA_NORTHWEST_Arr = data.areaInvestments.CHINA_NORTHWEST;
        var _CHINA_NORTHWEST = CHINA_NORTHWEST_Arr.length;

        var CHINA_NORTHEAST_Arr = data.areaInvestments.CHINA_NORTHEAST;
        var _CHINA_NORTHEAST = CHINA_NORTHEAST_Arr.length;

        var CHINA_SOUTH_Arr = data.areaInvestments.CHINA_SOUTH;
        var _CHINA_SOUTH = CHINA_SOUTH_Arr.length;

        tmpChina(_china, 9, _chinaArr, '.china-table');
        tmpTable(_CHINA_NORTH, 9, CHINA_NORTH_Arr, '.area-table', 1);
        tmpTable(_CHINA_NORTHEAST, 9, CHINA_NORTHEAST_Arr, '.area-table', 0);
        tmpTable(_CHINA_EAST, 9, CHINA_EAST_Arr, '.area-table', 0);
        tmpTable(_CHINA_NORTHWEST, 9, CHINA_NORTHWEST_Arr, '.area-table', 0);
        tmpTable(_CHINA_SOUTH, 9, CHINA_SOUTH_Arr, '.area-table', 0);
        jqDate.find('.dataStar').text(data.beginTime);
        jqDate.find('.dataEnd').text(data.endTime);
        jqDate.find('.newsdata i').text(data.updateTime);
    };
    var tmpChina = function (length, n, data, tableName) {
        var str = '';
        for (var i = 0; i < length; i++) {
            if (i > n) {
                str += '<tr class="hidden"><td><span class="index">' + (i + 1) + '</span><span class="total">' + data[i]["interest"] + '</span> 元</td><td><span class="gain">' + data[i]["corpus"] + '</span> 元</td><td><span class="user">' + data[i]["phone"] + '</span></td> </tr>';
            } else {
                str += '<tr><td><span class="index">' + (i + 1) + '</span><span class="total">' + data[i]["interest"] + '</span> 元</td><td><span class="gain">' + data[i]["corpus"] + '</span> 元</td><td><span class="user">' + data[i]["phone"] + '</span></td> </tr>';
            }
        }
        if (i > n) {
            str += '<tr><td colspan="3" style="text-align: center; padding: 0;"><button type="button" class="btn-more yellow">更多</button></td></tr>';
        }
        $(tableName).find('tbody tr').remove();
        $(tableName).find('tbody').append(str);
    }

    var tmpTable = function (length, n, data, tableName, bool) {
        if (bool) {
            var str = '<tbody>';
        } else {
            var str = '<tbody style="display:none">';
        }

        for (var i = 0; i < length; i++) {
            if (i > n) {
                str += '<tr class="hidden"><td><span class="index">' + (i + 1) + '</span><span class="total">' + data[i]["interest"] + '</span> 元</td><td><span class="gain">' + data[i]["corpus"] + '</span> 元</td><td><span class="user">' + data[i]["phone"] + '</span></td> </tr>';
            } else {
                str += '<tr><td><span class="index">' + (i + 1) + '</span><span class="total">' + data[i]["interest"] + '</span> 元</td><td><span class="gain">' + data[i]["corpus"] + '</span> 元</td><td><span class="user">' + data[i]["phone"] + '</span></td> </tr>';
            }
        }
        if (i > n) {
            str += '<tr><td colspan="3" style="text-align: center; padding: 0;"><button type="button" class="btn-more orange">更多</button></td></tr>';
        }
        str += '</tbody' >
            $(tableName).append(str);
    }

    //活动排行按 周 月 季 年
    jqDate.find('select').change(function () {
        var _thisValue = $(this).val();
        $('.nav li').removeClass('active').eq(0).addClass('active');
        $('.area-table').find('tbody').remove();
        $.post(API_URL, {period: _thisValue}, function (data) {
            allData(data);
        });
    });

    $('.nav li').click(function () {
        var _index = $(this).index();
        $(this).addClass('active').siblings().removeClass('active');
        $('.area-table tbody').hide();
        $('.area-table tbody').eq(_index).show();
    })


    $('body').on('click', '.btn-more', function () {
        $(this).closest('tr').siblings('.hidden').removeClass('hidden');
        $(this).closest('tr').remove();
    });


    //big picture
    var stage = '' ; //默认
    $('.house-list li,.cars-list li').click(function () {
        if($(this).closest('.pic-list').hasClass('house-list')){
            stage = '.house-list';
        }else{
            stage = '.cars-list';
        }
        var _img = $(this).find('img').attr('src');
        $(this).addClass('on').siblings().removeClass('on');
        $('.layer-box .content img').attr('src', _img);
        $('.layer-box').show();
        return false;
    });
    $('.btn-next').click(function (ele) {
        var index = $(stage+' li.on').next().index();
        if (index > 0) {
            var next_li = $(stage+' li.on').next();
            var _next_src = next_li.find('img').attr('src');
            $(stage+' li').removeClass('on');
            next_li.addClass('on');
            $('.layer-box .content img').attr('src', _next_src);
        }else{
        }
        return false;
    });
    $('.btn-prev').click(function () {
        var index = $(stage+' li.on').prev().index();
        if (index >=0) {
            var prev_li = $(stage+ ' li.on').prev();
            var _prev_src = prev_li.find('img').attr('src');
            $(stage +' li').removeClass('on');
            prev_li.addClass('on');
            $('.layer-box .content img').attr('src', _prev_src);
        }else{

        }
        return false;
    });




    $('body').click(function () {
        $('.layer-box').hide();
    })
})
