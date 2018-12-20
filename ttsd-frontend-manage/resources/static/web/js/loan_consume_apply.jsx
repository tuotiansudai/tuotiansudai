require('webStyle/investment/loan_consume_apply.scss');
let commonFun= require('publicJs/commonFun');
let uploadPic = require('publicJs/uploadPic');

$(function () {
    let marriageState, haveCreditReport, amount, period, homeIncome, loanUsage, elsePledge = '';
    let workPosition = '';
    let pledgeType = $('#pledgeType').val();
    let sesameCredit = '';

    $('input:radio[name="marriageState"]').on('click',function () {
        marriageState = $('input:radio[name="marriageState"]:checked').val();
        btnLightUp();
    });

    $('input:radio[name="period"]').on('click',function () {
        period = $('input:radio[name="period"]:checked').val();
        if (period === 'others') {
            period = $('#othersSelect').find('option:selected').val();
        }
        console.log('period的值是:' + period);
        btnLightUp();
    });

    haveCreditReport = 0;

    $('.workPosition').on('input',function (e) {
        workPosition = e.currentTarget.value;
    });

    $('.amount').on('input',function (e) {
        formatVal(e);
        amount = e.currentTarget.value;
        btnLightUp();
    });

    $('#othersSelect').on('change',function () {
        if ($('input:radio[name="period"]:checked').val() !== 'others') return;
        period = $(this).find('option:selected').val();
        console.log('period1的值是:' + period);
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

    $('.elsePledge').on('input',function (e) {
        elsePledge = e.currentTarget.value;
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
        if (marriageState && amount && period && homeIncome && loanUsage && elsePledge) {
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
        let data = { marriageState, haveCreditReport, amount, period, homeIncome, loanUsage, elsePledge, workPosition, elsePledge, sesameCredit, pledgeType};
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

//图片效果

$(function () {
    function initFancyBox() {
        let fancybox = require('publicJs/fancybox');
        for (let i = 0;i < $('.evidence_item').length;i++) {
            fancybox(function() {
                $('.evidence_item').eq(i).find('.fancybox').fancybox({
                    fixed: false,
                });
            });
        }
    }
    initFancyBox();

    $('.file-input').on('change',function (event) {
        event.preventDefault();
        new uploadPic($(this)).init();
        // $(this).replaceWith(function () {
        //     return (
        //         `<div>11111</div>`
        //     )
        // })
    })
});

