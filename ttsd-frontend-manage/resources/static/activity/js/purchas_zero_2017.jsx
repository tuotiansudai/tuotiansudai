require("activityStyle/purchas_zero_2017.scss");
  let commonFun= require('publicJs/commonFun');
require("publicJs/login_tip");
let $purchasZero = $('#purchas_zero');
let $goodsBetter = $('#goodsBetter');//选择好货
let $goodsInvest = $('#goodsInvest');//选择好货
let $goodsGive = $('#goodsGive');//选择好货
let $goodsInterest = $('#goodsInterest');//选择好货

//活动流程图片加载
let $processImg = $('.process-img',$purchasZero);
let purchasImgUrl = require('../images/2017/purchas_zero/222.png');
let purchasImg = $('<img src = "'+purchasImgUrl+'">');
let purchasImg2 = $('<img src = "'+purchasImgUrl+'">');
let purchasImg3 = $('<img src = "'+purchasImgUrl+'">');
let purchasImg4 = $('<img src = "'+purchasImgUrl+'">');
$goodsBetter.append(purchasImg);
$goodsInvest.append(purchasImg2);
$goodsGive.append(purchasImg3);
$goodsInterest.append(purchasImg4);

//商品列表图片加载
let imgUrl = require('../images/2017/purchas_zero/video.png');
let conImg = $('<img src = "'+imgUrl+'">');
$('.img').append(conImg);
//商品详情页图片
let articleUrl = require('../images/2017/purchas_zero/phone.png');
let imgDom = $('<img src = "'+articleUrl+'">');
$('#previewImg').append(imgDom);



