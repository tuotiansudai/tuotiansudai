require("activityStyle/tt_guarantee.scss");
let $topHeader=$('#topHeader'),
    $guaranteeContainer = $('#guaranteeContainer');

let browser = globalFun.browserRedirect();
let  relationshipMap;
if(browser =='mobile') {
    var topImage = new Image();
    var topWapUrl = require('../images/guarantee/db-top-wap.jpg');
    topImage.src = topWapUrl;
    topImage.onload=function() {
        $topHeader.append(topImage);
    };
}

let imageMap = new Image();
if(browser =='mobile') {
    relationshipMap = require('../images/guarantee/map-wap.png')
} else {
    relationshipMap = require('../images/guarantee/map-pc.png');
}

imageMap.src = relationshipMap;
$('#relationshipMap').append(imageMap);


