require(['jquery', 'layerWrapper', 'jquery-ui', 'bootstrap', 'csrf'], function ($, layer, _) {

    function showUserLotteriesList(userName, lotteries) {
        var dataGroup = [];
        dataGroup.push('<ul class="pop-user-list-box">');

        for (var i = 0; i < lotteries.length; i++) {
            dataGroup.push('<li>' + lotteries[i] + '</li>');
        }
        dataGroup.push('</ul>');
        layer.open({
            type: 1,
            title: userName + '的投资码',
            area: ['560px', '390px'],
            shadeClose: false,
            content: dataGroup.join('')
        });
    }

    $('.user-lotteries-count').click(function (e) {
        var lotteries = $(this).data('lotteries').split(',');
        var userName = $(this).data('username');
        showUserLotteriesList(userName, lotteries);
        return false;
    });

    function addConfig(investAmount, lotteryNumber) {
        console.log(arguments);
        $.ajax({
            url: '/activity-console/activity-manage/iphone7-lottery/config',
            type: 'POST',
            data: {'investAmount': investAmount, 'lotteryNumber': lotteryNumber},
            dataType: 'json',
            success: function (result, status, xhr) {
                location.reload();
            },
            error: function (xhr, status, message) {
            }
        });
    }

    function _audit(id, passed) {
        $.ajax({
            url: '/activity-console/activity-manage/iphone7-lottery/audit',
            type: 'POST',
            data: {'passed': passed, 'id': id},
            dataType: 'json',
            success: function (result, status, xhr) {
                if (!!result.data.status) {
                    layer.msg('处理成功', {}, function () {
                        location.reload();
                    });
                } else {
                    layer.msg('处理失败：' + result.data.message);
                }
            },
            error: function (xhr, status, data) {
                console.error(arguments);
            }
        });
    }

    function approve(id) {
        _audit(id, true);
    }

    function refuse(id) {
        _audit(id, false);
    }

    function clearErrorMessage() {
        $('.error-message').html("");
    }

    function showErrorMessage(message) {
        $('.error-message').html(message);
    }

    $('.config-item-submit-btn').click(function (e) {
        var investAmount = parseInt($('#investAmount').val());
        var lotteryNumber = $('#lotteryNumber').val();
        if (isNaN(investAmount) || investAmount % 50 != 0) {
            showErrorMessage("抽奖阶段必须为50万的整数倍");
            return;
        }
        if (lotteryNumber.length != 6 || isNaN(parseInt(lotteryNumber))) {
            showErrorMessage("中奖号码必须是6位数字");
            return;
        }
        addConfig(investAmount, lotteryNumber);
    });

    $('.config-item-approve-btn').click(function (e) {
        approve($(this).data('id'))
    });

    $('.config-item-refuse-btn').click(function (e) {
        refuse($(this).data('id'))
    });

});
