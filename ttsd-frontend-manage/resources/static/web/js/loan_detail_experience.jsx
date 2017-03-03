require('webStyle/investment/loan_detail.scss');
let commonFun= require('publicJs/commonFun');
//新手体验项目
let $experienceLoan=$('#experienceLoanDetailContent');

commonFun.useAjax({
    url: '/calculate-expected-coupon-interest/loan/1/amount/0',
    data: {
        "couponIds":$("input[name='userCouponIds']",$experienceLoan).data("coupon-id")
    },
    type: 'GET'
},function(amount) {
    $(".principal-income",$experienceLoan).text(amount);
});

let investForm=globalFun.$('#investForm');

investForm.onsubmit=function(event) {
    event.preventDefault();
    commonFun.useAjax({
        type:'POST',
        url: '/experience-invest',
        data:$(investForm).serialize()
    },function(response) {
        var data = response.data;
        if (data.status) {
            layer.open({
                type: 1,
                title: '&nbsp',
                area: ['400px'],
                content: $('#freeSuccess')
            });
        }
    });
}
$('.close-free',$experienceLoan).on('click', function (event) {
    event.preventDefault();
    layer.closeAll();
    location.reload();
});
