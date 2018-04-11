require('mWebStyle/account/risk_estimate.scss');
let commonFun= require('publicJs/commonFun');
// 使用rem
function calculationFun(doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 100 * (clientWidth /750);
            fSize > 100 && (fSize = 98.4);
            docEl.style.fontSize = fSize + 'px';
        };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
};
calculationFun(document, window);

$('#goBackIcon').on('click',function () {
    history.go(-1);
})
var $riskBox = $('#riskBox');
var $lastPart = $riskBox.find('.part-last');
//选择评估选项
$riskBox.find('dd').on('click', function(event) {
    let _self = $(this);
    let index = _self.parent().index();
    event.preventDefault();
    _self.addClass('active').siblings('dd').removeClass('active');
    if(index !== 7){
        setTimeout(function () {
            _self.parent().css('zIndex',-index);
        },200)

    }

});

//查询评估结果
$lastPart.find('dd').on('click', function(event) {
    event.preventDefault();

    let $self=$(this),
        $problemList=$('.problem-list',$riskBox),
        scoreArray=[];
    $problemList.find('dl').each(function(index, el) {

            scoreArray.push($(this).find('dd.active').attr('data-score'));

    });
    console.log({answers: scoreArray});

        commonFun.useAjax({
            url: '/risk-estimate',
            data: {answers: scoreArray},
            type: 'POST'
        },function(data) {
            console.log(data)
            if(data.data.status){
                location.href='/m/risk-estimate';
            }
        });

});

