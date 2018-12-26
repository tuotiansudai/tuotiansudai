require('webStyle/investment/loan_consume_apply.scss');
let commonFun= require('publicJs/commonFun');
let uploadPic = require('publicJs/uploadPic');
let fancybox = require('publicJs/fancybox');
initFancyBox();

function initFancyBox() {
    for (let i = 0;i < $('.evidence_item').length;i++) {
        $('.evidence_item').eq(i).find('.fancybox').addClass('fancybox' + i);
        fancybox(function() {
            $('.fancybox' + i).fancybox({
                fixed: false,
            });
        });
    }
}

let marriage, haveCreditReport, amount, period, homeIncome, loanUsage, elsePledge, hasTogetherLoaner, togetherLoaner, togetherLoanerIdentity = '';
let maticalSets = {
    identityProveUrls: [],
    incomeProveUrls: [],
    creditProveUrls: [],
    marriageProveUrls: [],
    propertyProveUrls: [],
    togetherProveUrls: [],
    driversLicense: []
};
let workPosition = '';
let pledgeType = $('#pledgeType').val();
let sesameCredit = '';

$('input:radio[name="marriage"]').on('click',function () {
    marriage = $('input:radio[name="marriage"]:checked').val();
    if (marriage !== 'MARRIED') {
        $('#marriageProveUrls').parent('.info_wrapper').hide();
        $('#marriageProveUrls').find('.remove-img-btn').trigger('click');

    }
    else $('#marriageProveUrls').parent('.info_wrapper').show();
    btnLightUp();
});

$('input:radio[name="period"]').on('click',function () {
    period = $('input:radio[name="period"]:checked').val();
    if (period === 'others') {
        period = $('#othersSelect').find('option:selected').val();
    }
    btnLightUp();
});

$('input:radio[name="togetherLoaner"]').on('click',function () {
    hasTogetherLoaner = $('input:radio[name="togetherLoaner"]:checked').val();
    if (hasTogetherLoaner === 'loaner') $('#togetherLoaner').show();
    else {
        let $togetherLoaner = $('#togetherLoaner');
        $togetherLoaner.hide();
        $togetherLoaner.find('.togetherLoaner').val('');
        $togetherLoaner.find('.togetherLoanerIdentity').val('');
        $togetherLoaner.find('.remove-img-btn').trigger('click');
        togetherLoaner = '';
        togetherLoanerIdentity = '';

    }
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

$('.togetherLoaner').on('input',function (e) {
    togetherLoaner = e.currentTarget.value;
    btnLightUp();
});

$('.togetherLoanerIdentity').on('input',function (e) {
    formatVal(e);
    togetherLoanerIdentity = e.currentTarget.value;
    btnLightUp();
});

$('#othersSelect').on('change',function () {
    if ($('input:radio[name="period"]:checked').val() !== 'others') return;
    period = $(this).find('option:selected').val();
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

function formatVal(e) {
    let obj = e.currentTarget;
    obj.value = obj.value.replace(/[^\d]/g,"");
    obj.value = obj.value.replace(/^0*$/g,"");
}

$('.imgWrapper').on('change','.file-input',function (event) {
    stopDefault(event);
    new uploadPic($(this),handlePic).init();
});

function handlePic($input,url) {
    let materialType = $input.parents('.evidence_item').attr('id');
    let type = $input.data('type') || '';
    let dom = `
        <a class="fancybox ${type}" href="${url}" rel="example_group">
            <img class="img" src="${url}" />
            <span class="remove-img-btn" data-type="${type}"></span>
        </a>
     `;
    if (type) {
        $input.parent('.' + type).replaceWith(() => {
            return dom;
        });
    }
    else {
        $input.parent().before(dom);
    }
    initFancyBox();
    updatePicArr(materialType);
    btnLightUp();
}

$('.imgWrapper').on('click','.remove-img-btn',function (event) {
    stopPropagation(event);
    stopDefault(event);
    let materialType = $(this).parents('.evidence_item').attr('id');
    let type = $(this).data('type');
    if (type) {  // one upload
        let text = '';
        switch (type) {
            case 'positive':
                text = '点击上传人像面';
                break;
            case 'negative':
                text = '点击上传国徽面';
                break;
            case 'positive_hand':
                text = '点击上传手持身份证人像面';
                break;
            case 'negative_hand':
                text = '点击上传手持身份证国徽面';
                break;
            default:
                text = '点击上传';
                break;
        }
        $(this).parent('.' + type).replaceWith(() => {
            return `
                <div class="img-item ${type}">
                    <div class="upload-desc">
                        <div class="icon-upload"></div>
                        ${text}
                    </div>
                    <input type="file" class="file-input" data-type="${type}">
                </div>
                `
        })
    }
    else { // multiple upload
        $(this).parent().remove();
    }
    initFancyBox();
    updatePicArr(materialType);
    btnLightUp();
});

function updatePicArr(type) {
    maticalSets[type] = [];
    let $fancybox =   $('#' + type).find('.fancybox');
    $fancybox.find('.img').each(function () {
        maticalSets[type].push($(this).attr('src'))
    });
    if ($fancybox.find('.img').length >= 8) {
        $('#' + type).find('.img-item').hide();
    }
    else {
        $('#' + type).find('.img-item').show();
    }
}

function stopDefault(e) {
    e = e || window.event;
    e && e.preventDefault ? e.preventDefault() :  e.returnValue = false;
    return false;
}

function stopPropagation(e) {
    e = e || window.event;
    e && e.stopPropagation ?  e.stopPropagation() :  e.cancelBubble = true;
}


function validateSupplementInfo() {  // 补充信息
    if (marriage) {
        if ( marriage === 'MARRIED') return maticalSets.marriageProveUrls.length;
        else return true;
    }
    else return false;
}

function validateLoanInfo() { // 借款申请信息
    if (period) return amount && homeIncome && loanUsage && elsePledge;
    else return false;
}

function validateProveInfo() { // 证明材料（除共同借款人）
    let { identityProveUrls, incomeProveUrls, creditProveUrls, propertyProveUrls }= maticalSets;
    return identityProveUrls.length == 4 && incomeProveUrls.length && creditProveUrls.length && propertyProveUrls.length;
}

function ValidateCommonLoaner() { // 共同借款人
    if (hasTogetherLoaner) {
        if (hasTogetherLoaner === 'loaner') {
            return togetherLoaner && togetherLoaner.length &&  togetherLoanerIdentity && togetherLoanerIdentity.length == 18 && maticalSets.togetherProveUrls.length == 2;
        }
        else return true;
    }
    else return false;
}
function btnLightUp() {
    if ($('.agreeContract').is( ":checked" ) && validateSupplementInfo() && validateLoanInfo() && validateProveInfo() && ValidateCommonLoaner()) {
        $('.confirm_btn').removeClass('disabled');
    }
    else {
        $('.confirm_btn').addClass('disabled');
    }
}

$('.agreeContract').on('click',function () {
    btnLightUp();
});

function submitFormData() {
    let data = { marriage, haveCreditReport, amount, period, homeIncome, loanUsage, elsePledge, workPosition, togetherLoaner, togetherLoanerIdentity, pledgeType, sesameCredit};
    data = Object.assign({},data,maticalSets);
    console.log(data);
    commonFun.useAjax({
        type: 'POST',
        url: '/loan-application/create-consume',
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

