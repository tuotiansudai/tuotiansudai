require('webStyle/investment/current_deposit.scss');
require('webJs/plugins/autoNumeric');

let currentDepositContent = $('#currentDepositContent');

let amountInputElement = $('.amount-input', currentDepositContent);
let amountElement = $('input[name="amount"]', currentDepositContent);
let depositSubmitElement = $('.deposit-submit', currentDepositContent);
let depositForm = $('form', currentDepositContent);

amountInputElement.autoNumeric("init");

amountInputElement.focus(function () {
    layer.closeAll('tips');
});

let showInputErrorTips = function (message) {
    layer.tips('<i class="fa fa-times-circle"></i>' + message, amountInputElement, {
        tips: [2, '#ff7200'],
        time: 0
    });
};

if (amountInputElement.data('error-message')) {
    amountInputElement.val(amountInputElement.data('deposit-amount'));
    showInputErrorTips(amountInputElement.data('error-message'));
}

let getDepositAmount = function () {
    let amount = 0;
    if (!isNaN(amountInputElement.autoNumeric("get"))) {
        amount = parseInt((amountInputElement.autoNumeric("get") * 100).toFixed(0));
    }
    return amount;
};

let validate = function () {
    if ($('input[name="agreement"]:checked', currentDepositContent).length === 0) {
        layer.tips('<i class="fa fa-times-circle"></i>' + "请阅读并同意", '.agreement-text', {
            tips: [2, '#ff7200'],
            time: 3000
        });
        return false
    }
    let depositAmount = getDepositAmount();
    if (depositAmount === 0) {
        showInputErrorTips('请输入转入金额');
        return false;
    }
    let balance = parseInt(amountInputElement.data('balance'));
    let maxDepositAmount = parseInt(amountInputElement.data('max-deposit-amount'));
    if (depositAmount > maxDepositAmount) {
        showInputErrorTips('今日最多可购买' + (maxDepositAmount / 100).toFixed(2) + '元');
        return false;
    }

    if (depositAmount > balance) {
        location.href = '/recharge';
        return false;
    }

    return true;
};

depositSubmitElement.click(function (event) {
    event.preventDefault();

    if (!validate()) {
        return false;
    }

    amountElement.val(getDepositAmount());

    layer.open({
        type: 1,
        closeBtn: 0,
        skin: 'layer-tip-deposit',
        title: '日息宝转入',
        shadeClose: false,
        btn: ['取消', '确认'],
        area: ['300px'],
        content: '<p class="pad-m-tb tc">确认转入？</p>',
        btn1: function () {
            layer.closeAll();
        },
        btn2: function () {
            depositForm.submit();
            layer.closeAll();
        }
    });

});