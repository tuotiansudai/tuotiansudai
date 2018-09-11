require("activityStyle/mid_autumn_festival_2018.scss");

let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);

let imgUrl = require('../images/2018/mid-autumn-festival/phone.png');
$('.phone').find('img').attr('src',imgUrl)
let $contentList = $('#contentList');
let $date = $('#dateTime');
let startTime = $date.data('starttime'),//开始时间
    endTime = $date.data('endtime'),//结束时间
    todayDay = $.trim($date.text());//显示的日期

let currentData = new Date();
console.log('==========================')
console.log(currentData)
console.log('==========================')
heroRank('2018-09-11 12:00:00');


function getDate() {
    let date = new Date();
}
//获取前一天或者后一天的日期
let GetDateStr = function(date,AddDayCount) {
    var dd = new Date(date);
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();

    return y + "-" + (m < 10 ? ('0' + m) : m) + "-" + (d < 10 ? ('0' + d) : d);
}
function heroRank(date) {
    commonFun.useAjax({
        type: 'GET',
        url: '/activity/middleautum-nationalday/records?tradingTime=' + date
    }, function (data) {

        if (data.status) {
            //获取模版内容
            let ListTpl = $('#tplTable').html();
            // 解析模板, 返回解析后的内容
            let render = _.template(ListTpl);
            let html = render(data);
            $contentList.html(html);
        }
    })
}


