require('webStyle/investment/loan_borrow.scss');

$(function () {
    let married, haveCreditReport, amount, period, homeIncome, loanUsage, pledgeInfo, sesameCredit = '';
    let isFillInSesame = false;

    $('input:radio[name="isMarried"]').on('click',function () {
        married = $('input:radio[name="isMarried"]:checked').val();
        btnLightUp();
    });

    $('input:radio[name="haveCreditReport"]').on('click',function () {
        haveCreditReport = $('input:radio[name="haveCreditReport"]:checked').val();
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
            isFillInSesame = false;
        }
        sesameCredit =  obj.value;
    });

    $('.sesameCredit').on('blur',function (e) {
        let obj = e.currentTarget;
        if (obj.value > 1000) {
            layer.msg('请输入0-1000之间的分数');
        }
        btnLightUp();
    });

    $('.confirm_btn').on('click',function () {
        if($(this).hasClass('disabled')) return;
        alert(1)
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


});