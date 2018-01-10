require('mWebStyle/investment/exchange_coupon.scss');
let commonFun = require('publicJs/commonFun');
var tpl = require('art-template/dist/template');

let $exchangeForm  = $('#exchangeForm');
let  $couponSuccess =  $('#couponSuccess');
let  $couponExchange =  $('#couponExchange'),
    $exchangeSuccessConfirm = $('#exchangeSuccessConfirm');

$('#submitCode').on('click', function() {
    var exchangeCode = $('#couponByCode').val(),
        $errorText=$('#errorText');
    $(this).prop('disabled',true);
    if ($.trim(exchangeCode).length != 14) {
        commonFun.CommonLayerTip({
            btn: ['确定'],
            area:['280px', '150px'],
            content: `<div class="record-tip-box"><b class="pop-title">温馨提示</b> <p>兑换码不正确</p></div> `,
        },function() {
            layer.closeAll();
        })
        $('#couponByCode').val('');
        $("#submitCode").removeAttr("disabled");
    } else {
        commonFun.useAjax({
            url: '/m/my-treasure/'+exchangeCode+'/exchange',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8'
        },
            function(data){
                $("#submitCode").removeAttr("disabled");
                var message = data.message;
                if (data.status) {

                    $couponExchange.hide();
                    data.data.exchangeCode = exchangeCode;
                    data.data.minDay = minValue(data.data.productTypeList);
                    let html = tpl('exchangeSuccessData', data);
                    $couponSuccess.html(html);
                    $couponSuccess.show();

                } else {
                    commonFun.CommonLayerTip({
                        btn: ['确定'],
                        area:['280px', '150px'],
                        content: `<div class="record-tip-box"><b class="pop-title">温馨提示</b> <p>${message}</p></div> `,
                    },function() {
                        layer.closeAll();
                    })
                    $('#couponByCode').val('');
                }
            }

            )


    }
});
$('#couponByCode').on('keyup',function () {
    var exchangeCode = $('#couponByCode').val();

    if ($.trim(exchangeCode).length == 14) {
        $("#submitCode").removeAttr("disabled");
    }else {

    }
})
//后退
$('#iconExchangeCoupon').on('click',function () {
    history.go(-1);
})



function minValue(arr) {
    let newArr = [];
    arr.forEach(function (item,index) {
        let newItem = parseInt(item.substr(1));
        if(!isNaN(newItem) ){
            newArr.push(newItem)
        }

    })

    let minItem = newArr[0];
    newArr.forEach(function (item,index) {
        if(item < minItem){
            let temp = item;
            item = minItem;
            minItem = temp;
        }
    })
    return minItem;

}

//确定按钮回到优惠券兑换页面
$couponSuccess.on('click','#exchangeSuccessConfirm',function () {
    $couponSuccess.hide();
    $couponExchange.show();
})
$couponSuccess.on('click','#iconExchangeSuccess',function () {
    $couponSuccess.hide();
    $couponExchange.show();
})
$couponSuccess.on('click','.to-use-btn',function () {
    location.href='/m/loan-list'
})
// let data = {status:true,message:'陈工',data:{amount:'1000'}}
// $couponExchange.hide();
// let html = tpl('exchangeSuccessData', data);
// $couponSuccess.html(html);
// $couponSuccess.show();