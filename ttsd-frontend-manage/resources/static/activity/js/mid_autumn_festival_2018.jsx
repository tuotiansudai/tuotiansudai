require("activityStyle/mid_autumn_festival_2018.scss");

let commonFun= require('publicJs/commonFun');
// require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);

let imgUrl = require('../images/2018/mid-autumn-festival/phone.png');
$('.phone').find('img').attr('src',imgUrl)


