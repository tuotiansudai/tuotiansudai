require("activityStyle/super_scholar_2018.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/plugins/jquery.qrcode.min');
require('publicJs/plugins/jQuery.md5');
//require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);
let url = location.href;

let $getMore = $('#getMore'),
    $getLess = $('#getLess'),
    $reappearanceList = $('#reappearanceList'),
    $reappearanceContent = $('.reappearance-content');
commonFun.calculationRem(document, window)

//heroRank();
$getMore.on('click',function () {
    let _this = $(this);
    _this.hide();
    $getLess.show();
     getMoreData();
})

$getLess.on('click',function () {
    let _this = $(this);
    _this.hide();
    $getMore.show();
    getLess();
})
function getLess() {
    let liHeihgt = $reappearanceList.find('tr').outerHeight();
    let thHeight =  $reappearanceContent.find('thead').outerHeight();
    let totalHeight = 3*liHeihgt+thHeight+2;
    $reappearanceContent.height(totalHeight);
}
function getMoreData(num) {
    // let liHeihgt = $reappearanceList.find('tr').outerHeight();
    // let thHeight =  $reappearanceContent.find('thead').outerHeight();
    // let totalHeight = num*liHeihgt+thHeight+2;
    // $reappearanceContent.height(totalHeight);
}
 function heroRank(records,callback) {
//     commonFun.useAjax({
//         type: 'GET',
//         url: '/activity/spring-breeze/ranking/2018-3-22'
//     }, function (data) {
//         let list = [1,2,3,4,5];
//         callback(list.length)
//         //获取模版内容
//         let ListTpl = $('#tplTable').html();
//         // 解析模板, 返回解析后的内容
//         let render = _.template(ListTpl);
//         let html = render(data);
//         $reappearanceList.html(html);
//     })
 }
var md5String=commonFun.decrypt.compile('c44e7bfb1f8beae43b1c888844014dce','18710164899');
 alert(md5String)
//c44e7bfb1f8beae43b1c888844014dce   c41e8b7b1f0b1a644b8c989844014dce
