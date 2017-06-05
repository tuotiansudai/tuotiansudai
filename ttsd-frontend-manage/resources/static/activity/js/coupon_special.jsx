require("activityStyle/coupon_special.scss");
let browser = globalFun.browserRedirect();

let redBagUrl = require('../images/2017/coupon-special/red-all.png');
let topHeaderUrl = require('../images/2017/coupon-special/head-coupon-wap.png');
document.getElementById('redBag').src = redBagUrl;

if(browser =='mobile') {

    let  topImg= new Image();
    topImg.src = topHeaderUrl;
    topImg.onload = function() {
        $('#topHeader').append(topImg);
    }
}