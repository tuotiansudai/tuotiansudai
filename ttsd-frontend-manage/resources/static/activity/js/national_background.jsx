require("activityStyle/national_background.scss");
let riskUrl = require('../images/national-background/icon-risk.png'),
    moneyUrl = require('../images/national-background/icon-money_save.png'),
    elecUrl = require('../images/national-background/icon-electronic.png'),
    devUrl = require('../images/national-background/icon-development.png');
let riskImg = new Image();
riskImg.src = riskUrl;
let moneyImg = new Image();
moneyImg.src = moneyUrl;
let elecImg = new Image();
elecImg.src = elecUrl;
let devImg = new Image();
devImg.src = devUrl;

$('.risk').append(riskImg);
$('.money-save').append(moneyImg);
$('.electornic').append(elecImg);
$('.dev').append(devImg);