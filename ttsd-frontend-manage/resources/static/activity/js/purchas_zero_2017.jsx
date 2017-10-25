require("activityStyle/purchas_zero_2017.scss");
require("activityStyle/media.scss");
  let commonFun= require('publicJs/commonFun');
require("publicJs/login_tip");
let $purchasZero = $('#purchas_zero');
let $goodsBetter = $('#goodsBetter');//选择好货
let $goodsInvest = $('#goodsInvest');//投资一定金额
let $goodsGive = $('#goodsGive');//白拿商品
let $goodsInterest = $('#goodsInterest');//到期还本付息

//活动流程图片加载
let $processImg = $('.process-img',$purchasZero);
let bagsUrl = require('../images/2017/purchas_zero/bags.png');
let giftsUrl = require('../images/2017/purchas_zero/gifts.png');
let investUrl = require('../images/2017/purchas_zero/investing.png');
let moneyUrl = require('../images/2017/purchas_zero/money.png');
let $bagsImg = $('<img src = "'+bagsUrl+'">');
let $giftsImg = $('<img src = "'+giftsUrl+'">');
let $investImg = $('<img src = "'+investUrl+'">');
let $moneyImg = $('<img src = "'+moneyUrl+'">');

$goodsBetter.append($bagsImg);
$goodsInvest.append($moneyImg);
$goodsGive.append($giftsImg);
$goodsInterest.append($investImg);

//商品列表图片加载
let $humidifier = $('#humidifier'),//加湿器
    $barBox = $('#barBox'),//拉杆箱
    $razor = $('#razor'),//剃须刀
    $cosmetics = $('#cosmetics'),//化妆品
    $phone = $('#phone'),//小米手机
    $video = $('#video'),//夏普电视
    $purifier = $('#purifier'),//飞利浦净化器
    $camara = $('#camara'),//索尼相机
    $macAir = $('#macAir'),//小米Air
    $iphonex = $('#iphonex');//iphonex
let barBoxUrl = require('../images/2017/purchas_zero/product/bar_box.png');
let $barBoxImg = $('<img src = "'+barBoxUrl+'">');
$('.img').append($barBoxImg);
//商品详情页图片
let articleUrl = require('../images/2017/purchas_zero/phone.png');
let imgDom = $('<img src = "'+articleUrl+'">');
$('#previewImg').append(imgDom);
//弹窗提示已售完
let $soldTipDOM = $('#soldTipDOM');
// layer.open({
//     type: 1,
//     title: false,
//     closeBtn: 1,
//     btn: ['我知道啦'],
//     shadeClose: true,
//     // area: ['385px', '290px'],
//     content: $soldTipDOM
// });
let sourceKind = globalFun.parseURL(location.href);
console.log(sourceKind)





