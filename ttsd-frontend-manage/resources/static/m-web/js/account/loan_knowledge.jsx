require('mWebStyle/account/loan_knowledge.scss');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');


$(function () {

    let $knowledgeList = $('#knowledgeList');
    let bannerHeight = $('.knowledge-banner').height();
    let footerHeight = $('.knowledge-list').height();
    let contentHeight = $('body').height() - bannerHeight - footerHeight;

$('.knowledge-list-frame').height(contentHeight);

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true,
    click: true
});
myScroll.on('scrollEnd', function () {
    //如果滑动到底部，则加载更多数据（距离最底部10px高度）
    if ((this.y - this.maxScrollY) <= 10) {
        getMore();
        console.log(9)
    }
});

    let pagenum = 1;//当前页数
//获取更多数据
    function getMore() {

        pagenum++;

        commonFun.useAjax(
            {
                url: window.location + "?index=" + pagenum,
                dataType: 'html',
                type: 'get',
            },
            function (data) {
                if($(data).find("#wrapperOut #knowledgeList").html().trim().length > 0){
                    $knowledgeList.append($(data).find("#wrapperOut #knowledgeList").html());
                    myScroll.refresh();

                }else {
                    $('#noData').show();
                }

            }
        )
    }


    $knowledgeList.on('click','.goToTranDetail',function (e) {
        e.preventDefault();
        e.stopPropagation();

        var url = $(this).data('url')
        $.when(commonFun.isUserLogin())
            .done(function () {
                location.href = url+'#transferDetail';
            }).fail(function () {
            location.href = '/m/login'
        })

    })
function isPassive() {
    var supportsPassiveOption = false;
    try {
        addEventListener("test", null, Object.defineProperty({}, 'passive', {
            get: function () {
                supportsPassiveOption = true;
            }
        }));
    } catch(e) {}
    return supportsPassiveOption;
}
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, isPassive() ? {
    capture: false,
    passive: false
} : false);
})