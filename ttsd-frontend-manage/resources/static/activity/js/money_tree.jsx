require("activityStyle/money_tree.scss");
let commonFun= require('publicJs/commonFun');
let $appVersion = $('#appVersion');
var sourceKind = globalFun.parseURL(location.href);
$('#btn-shake').on('click', function () {
    layer.msg("活动已结束！")
});
