require("activityStyle/cash_snowball_2017.scss");
require("activityStyle/media.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);

let $ware_DOM = $('#ware_DOM'),
    $progress = $('#progress',$ware_DOM),
    $money_tip = $('#money_tip',$ware_DOM),
    $record_list = $('#record_list');
let $mobileWareDOM = $('#mobile_ware_DOM'),
    $mobileMoney_tip = $('#mobile_money_tip',$mobileWareDOM),
    $mobileProgress = $('#progress',$mobileWareDOM);

let $returnMoney = $('#returnMoney');

//所有红包置灰
$ware_DOM.find('.red-ware').each((index,item)=>{
    $(item).css({
        'opacity':0.6
    })
})


$.when(commonFun.isUserLogin())
    .done(function () {
        let myWare = parseInt($returnMoney.text());
        let wareIndex = Math.floor(myWare/100);
        let moneyTipWidth = $money_tip.innerWidth(),
            mobileMoney_tip_width = $mobileMoney_tip.innerWidth();
        //返现红包金额提示
        $money_tip.find('em').text(myWare);
        $mobileMoney_tip.find('em').text(myWare);

//返现红包提示
        if(wareIndex >0 && wareIndex < 3){
            $money_tip.css({
                'left':-12+28*(wareIndex-1) + 174*(wareIndex-1) +28 -moneyTipWidth/2,
            })
            if(wareIndex%2 == 0){
                $money_tip.css({
                    'top':-40
                })
            }else {
                $money_tip.css({
                    'top':54
                })
            }
        }else if(wareIndex >=3){
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
                $ware_DOM.find('.red-ware').eq(index).css({
                    fontSize:'0.2rem'
                })

            }
            $ware_DOM.find('.red-ware').eq(index).text(item);
        })

//wap端点亮红包
        let mobileWareArr = [];
        if(wareIndex > 0 && wareIndex < 2) {
            mobileWareArr = [100,200,300];
            $mobileWareDOM.find('.red-ware').each((index,item)=>{
                if(index < 1){
                    $(item).css({
                        'opacity':1
                    })

                }

            })
            //wap红包提示位置
            $mobileMoney_tip.css({
                left:'-0.35rem'
            })
            //点亮圆点和轴线
            $mobileProgress.find('.circle').each((index,item)=>{

                if(index < wareIndex){
                    $(item).css({
                        'borderColor':'#ffd41d'
                    })
                }
            })
            $mobileProgress.find('.thread').each((index,item)=>{
                if(index < wareIndex - 1){
                    $(item).css({
                        'background':'#ffd41d'
                    })
                }
            })
        }else if(wareIndex >= 2) {
            mobileWareArr = [myWare-100,myWare,myWare+100];
            $mobileWareDOM.find('.red-ware').each((index,item)=>{
                if(index < 2){
                    $(item).css({
                        'opacity':1
                    })

                }

            })

            //wap红包提示位置
            $mobileMoney_tip.css({
                left:'2.5rem'
            })
            //点亮圆点和轴线
            $mobileProgress.find('.circle').each((index,item)=>{

                if(index < 2){
                    $(item).css({
                        'borderColor':'#ffd41d'
                    })
                }
            })
            $mobileProgress.find('.thread').each((index,item)=>{
                if(index < 1){
                    $(item).css({
                        'background':'#ffd41d'
                    })
                }
            })
        }
        //3个红包的值
        mobileWareArr.forEach((item,index)=>{
            if(item >= 1000){
                //红包大于等于1000调整样式
                $mobileWareDOM.find('.red-ware').eq(index).css({
                    fontSize:'0.2rem'
                })
            }
            $mobileWareDOM.find('.red-ware .money').eq(index).text(item);
        })

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
//中奖记录滚动
commonFun.scrollList($record_list,5,1500)

//点击去投资按钮跳转到投资列表
$('.to-invest').on('click',function () {
    window.location.href = '/loan-list';
})
$('.to-login').on('click',function () {
    toLogin();
})




