require("activityStyle/midsummer.scss");
let $topHeader=$('#topHeader');

let browser = globalFun.browserRedirect();

if(browser =='mobile') {
    var topImage = new Image();
    var topWapUrl = require('../images/midsummer/zl-head-mobile.jpg');
    topImage.src = topWapUrl;
    topImage.onload=function() {
        $topHeader.append(topImage);
    };
}