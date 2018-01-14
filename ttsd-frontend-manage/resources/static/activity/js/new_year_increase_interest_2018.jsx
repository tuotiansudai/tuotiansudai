require("activityStyle/new_year_increase_interest_2018.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');

let $increase_interest = $('#increase_interest');
let $productList = $('#productList',$increase_interest);

let descObj = {
    0:'蒙顶山春茶云雾茶礼盒装125g',
    1:'江小白45度清香型白酒125ml*12瓶',
    2:'小米 红米5A 全网通版 2GB+16GB',
    3:'周生生黄金羽毛吊坠计价2.31克',
    4:'AOSHIMSIT 按摩椅家用太空舱',
    5:'立久佳家用静音折叠跑步机',
    6:'海尔模卡55英寸 4K曲面液晶电视',
    7:'Apple iPhone8（64GB'
}
$productList.find('li').each(function (index,item) {
    let  _self = $(this);
    let imgUrl = require('../images/2018/new-year-increase-interest/product'+(index+1)+'.png');
    let img = new Image();
    img.src = imgUrl;
    img.alt=descObj[index];
    img.title = descObj[index];
    _self.append(img);
})

//鼠标悬停效果
let $descTip = $('.desc-tips'),
    $lookBtn = $('.look-btn');
$lookBtn.hover(function () {
   $(this).parents('.activity-desc').next().css('visibility','visible');
},function () {
    $(this).parents('.activity-desc').next().css('visibility','hidden');
})
