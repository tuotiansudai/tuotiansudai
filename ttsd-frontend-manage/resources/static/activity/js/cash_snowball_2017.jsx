require("activityStyle/cash_snowball_2017.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);

let $ware_DOM = $('#ware_DOM'),
    $progress = $('#progress'),
    $money_tip = $('#money_tip'),
    $record_list = $('#record_list');

//所有红包置灰
$ware_DOM.find('.red-ware').each((index,item)=>{
    $(item).css({
        'opacity':0.6
    })
})
let myWare = 30;
let wareIndex = Math.floor(myWare/100);
let moneyTipWidth = $money_tip.innerWidth();

//返现红包提示
if(wareIndex >0 && wareIndex < 3){
    $money_tip.css({
        'left':28*(wareIndex-1) + 174*(wareIndex-1) +28 -moneyTipWidth/2,
    })
    if(wareIndex%2 == 0){
        $money_tip.css({
            'top':-40
        })
    }
}else {
    $money_tip.css({
        'left':370,
        'top':54
    })
}



//根据获取的红包大小一次点亮红包
let wareArr = [];
if(myWare < 400 && myWare > 0){
    wareArr = [100,200,300,400,500]
    $ware_DOM.find('.red-ware').each((index,item)=>{
        if(index < wareIndex){
            $(item).css({
                'opacity':1
            })

        }

    })
    //圆点点亮
    $progress.find('.circle').each((index,item)=>{

        if(index < wareIndex){
            $(item).css({
                'borderColor':'#ffd41d'
            })
        }
    })
    //线条点亮
    $progress.find('.thread').each((index,item)=>{
        if(index < wareIndex - 1){
            $(item).css({
                'background':'#ffd41d'
            })
        }
    })

}else if(myWare >= 400) {
    wareArr = [myWare-200,myWare-100,myWare,myWare+100,myWare+200];
    $ware_DOM.find('.red-ware').each((index,item)=>{
        if(index <= 2 ){
            $(item).css({
                'opacity':1
            })
        }

    })
    $progress.find('.circle').each((index,item)=>{

        if(index < 3){
            $(item).css({
                'borderColor':'#ffd41d'
            })
        }
    })
    $progress.find('.thread').each((index,item)=>{
        if(index < 2){
            $(item).css({
                'background':'#ffd41d'
            })
        }
    })
}
//5个红包的值
wareArr.forEach((item,index)=>{
    if(item >= 1000){
        //红包大于等于1000调整样式
        $ware_DOM.find('.red-ware .money').eq(index).css({
            fontSize:'20px'
        })
        $ware_DOM.find('.red-ware em').eq(index).css({
            left:'4px'
        })
    }
    $ware_DOM.find('.red-ware .money').eq(index).text(item);
})
//点击去投资按钮跳转到投资列表
$('.to-invest').on('click',function () {
    window.location.href = '/loan-list';
})
$('.to-login').on('click',function () {
    toLogin();
})

//未登录
function toLogin(){
    if (sourceKind.params.source == 'app') {
        location.href = "app/tuotian/login";
    }else{
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['auto', 'auto'],
            content: $('#loginTip')
        });
    }

}
commonFun.scrollList($record_list,5,1500)





