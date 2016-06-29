/**
 * created by xuyang
 * 2016-06-08
 */
require(['jquery', 'underscore', 'moment', 'daterangepicker'], function ($, _, moment) {
    $(function () {
        if (!$('#instructions').length) {
            return false;
        }

        $('#instructions').find('.item').on('mouseover', function () {
            var $t = $(this);
            $t.addClass('active').siblings().removeClass('active');
        });

        var $dateTimePicker = $('#date-time-picker').dateRangePicker({
            separator: ' ~ '
        }).on('datepicker-change', function (event, obj) {
            renderTable({
                startTime: moment(obj.date1).format('YYYY-MM-DD'),
                endTime: moment(obj.date2).format('YYYY-MM-DD')
            })
        })

        var template = $('#tpl').html();
        var $tbody = $('#tbody');
        var renderTable = function (sendData, fn) {
            $.ajax({
                url: '/membership/structure-list-data',
                data: sendData || ''
            }).done(function (response) {

                $tbody.html(_.template(template)({
                    data: response
                }));
                fn && fn();

            }).fail(function (xhr) {
                console.error(xhr, '获取数据失败!');
            });
        };

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


    }); // document ready end bracket
}); // require end bracket