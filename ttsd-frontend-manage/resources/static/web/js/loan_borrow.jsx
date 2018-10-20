require('webStyle/investment/loan_borrow.scss');
let commonFun= require('publicJs/commonFun');

$(function () {
    let married, haveCreditReport, amount, period, homeIncome, loanUsage, pledgeInfo, elsePledge = '';
    let workPosition = '';
    let sesameCredit = 0;
    let isFillInSesame = false;

    $('input:radio[name="isMarried"]').on('click',function () {
        married = $('input:radio[name="isMarried"]:checked').val();
        btnLightUp();
    });

    $('input:radio[name="haveCreditReport"]').on('click',function () {
        haveCreditReport = $('input:radio[name="haveCreditReport"]:checked').val();
    });

    $('.workPosition').on('input',function (e) {
        workPosition = e.currentTarget.value;
    });

    $('.elsePledge').on('input',function (e) {
        elsePledge = e.currentTarget.value;
    });

    $('.amount').on('input',function (e) {
        formatVal(e);
        amount = e.currentTarget.value;
        btnLightUp();
    });

    $('.period').on('input',function (e) {
        formatVal(e);
        period = e.currentTarget.value;
        btnLightUp();
    });

    $('.homeIncome').on('input',function (e) {
        formatVal(e);
        homeIncome = e.currentTarget.value;
        btnLightUp();
    });

    $('.loanUsage').on('input',function (e) {
        loanUsage = e.currentTarget.value;
        btnLightUp();
    });

    $('.pledgeInfo').on('input',function (e) {
        pledgeInfo = e.currentTarget.value;
        btnLightUp();
    });

    $('.sesameCredit').on('input',function (e) {
        let obj = e.currentTarget;
        obj.value = obj.value.replace(/[^\d]/g,"");
        if (obj.value != '') {
            isFillInSesame = true;
        }
        else {
            sesameCredit = 0;
            isFillInSesame = false;
        }
        sesameCredit =  obj.value;
        if (sesameCredit > 1000) {
            layer.msg('请输入0-1000之间的分数');
        }
        btnLightUp();
    });

    $('.confirm_btn').on('click',function () {
        if($(this).hasClass('disabled')) return;
        submitFormData();
    });

    function btnLightUp() {
        if (married && haveCreditReport && amount && period && homeIncome && loanUsage && pledgeInfo) {
            if (isFillInSesame && sesameCredit > 1000) {
                $('.confirm_btn').addClass('disabled');
            }
            else {
                $('.confirm_btn').removeClass('disabled');
            }
        }
        else {
            $('.confirm_btn').addClass('disabled');
        }
    }
    function formatVal(e) {
        let obj = e.currentTarget;
        obj.value = obj.value.replace(/[^\d]/g,"");
        obj.value = obj.value.replace(/^0*$/g,"");
    }

    function submitFormData() {
        let data = { married, haveCreditReport, amount, period, homeIncome, loanUsage, pledgeInfo, workPosition, elsePledge, sesameCredit};
        console.log(data);
        commonFun.useAjax({
            type: 'POST',
            url: '/loan-application/create',
            data: data
        }, function (data) {
            if (data.status) {
                location.href = '/loan-application/success';
            }
            else {
                layer.msg(data.message);
            }
        });
    }


});