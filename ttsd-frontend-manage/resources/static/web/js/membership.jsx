require('webStyle/membership.scss');

let moment = require('moment');
let commonFun= require('publicJs/commonFun');
require('webJsModule/daterangepicker');

let $instructions = $('#instructions'),
    $dateTimePicker=$('#date-time-picker');

    $instructions.find('.item').on('mouseover', function () {
        var $t = $(this);
        $t.addClass('active').siblings().removeClass('active');
    });

 function renderTable(sendData,fn){
        var template = $('#tpl').html();
        var $tbody = $('#tbody');
        commonFun.useAjax({
            type:'GET',
            url: '/membership/structure-list-data',
            data: sendData || ''
        },function(response) {
            $tbody.html(_.template(template)({
                data: response
            }));
            fn && fn();
        });
    };

    $dateTimePicker.dateRangePicker({
        separator: ' ~ '
    }).on('datepicker-change', function (event, obj) {
        renderTable({
            startTime: moment(obj.date1).format('YYYY-MM-DD'),
            endTime: moment(obj.date2).format('YYYY-MM-DD')
        })
    });

renderTable();
$('#filter-btns').find('span').on('click', function () {
    var $t = $(this);
    var type = $t.data('type');
    $('#date-time-picker').data('dateRangePicker').clear();
    if (type == 'all') {
        renderTable(null, function () {
            $t.addClass('active').siblings().removeClass('active');
        });
    } else {
        var time = moment();
        renderTable({
            endTime: time.format('YYYY-MM-DD'),
            startTime: time.subtract(parseInt(type), 'days').format('YYYY-MM-DD')
        }, function () {
            $t.addClass('active').siblings().removeClass('active');
        });
    }
})
