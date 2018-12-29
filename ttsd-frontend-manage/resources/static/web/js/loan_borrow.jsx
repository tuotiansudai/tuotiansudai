require('webStyle/investment/loan_borrow.scss');
let commonFun= require('publicJs/commonFun');

$(function () {
    let marriage, haveCreditReport, amount, period, homeIncome, loanUsage, pledgeInfo, elsePledge = '';
    let workPosition = '';
    let pledgeType = $('#pledgeType').val();
    let sesameCredit = '';

    $('input:radio[name="isMarried"]').on('click',function () {
        marriage = $('input:radio[name="isMarried"]:checked').val();
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
        sesameCredit =  obj.value;
    });

    $('.confirm_btn').on('click',function () {
        if($(this).hasClass('disabled')) return;
        submitFormData();
    });

    function btnLightUp() {
        if (marriage && haveCreditReport && amount && period && homeIncome && loanUsage && pledgeInfo) {
            $('.confirm_btn').removeClass('disabled');
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
        let data = { marriage, haveCreditReport, amount, period, homeIncome, loanUsage, pledgeInfo, workPosition, elsePledge, sesameCredit, pledgeType};
        console.log(data);
        commonFun.useAjax({
            type: 'POST',
            url: '/loan-application/create',
            data: JSON.stringify(data),
            contentType: 'application/json; charset=UTF-8'
        }, function (data) {
            if (data.data.status) {
                location.href = '/loan-application/success';
            }
            else {
                layer.msg(data.data.message);
            }
        });
    }


});