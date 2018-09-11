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

function heroRank(date) {
    commonFun.useAjax({
        type: 'GET',
        url: '/activity/spring-breeze/ranking/' + date
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


