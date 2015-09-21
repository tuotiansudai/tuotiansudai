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
    var allData = function(data){
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
                str += '<tr><td><span class="index">' + (i + 1) + '</span><span class="total">' +data[i]["interest"] + '</span> 元</td><td><span class="gain">' +  data[i]["corpus"] + '</span> 元</td><td><span class="user">' + data[i]["phone"] + '</span></td> </tr>';
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
})
