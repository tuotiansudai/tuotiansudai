require('mWebStyle/account/loan_knowledge.scss');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');


$(function () {

    let $knowledgeList = $('#knowledgeList');
    let bannerHeight = $('.knowledge-banner').height();
    let footerHeight = $('.knowledge-list').length?$('.knowledge-list').height():0;
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

    }
});

let requestData={"index":1,"pageSize":10};
let isLastPage = false;
getMore()
//获取更多数据
    function getMore() {

if(isLastPage){
        $('#pullUp').hide();
    $('#noData').show();
    return false;

}
        commonFun.useAjax(
            {
                url: '/knowledge/list',
                type: 'get',
                data:requestData
            },
            function (data) {
                if(data.success){
                    if(!data.data.hasNextPage){
                        isLastPage = true;
                    }
                   if(data.data.records.length){
                       if(requestData.index!==1){
                           $('#pullUp').show();
                       }
                        let knowledgeTpl=$('#knowledgeListTemplate').html();
                        let ListRender = _.template(knowledgeTpl);
                        let html = ListRender(data.data);
                        $knowledgeList.append(html);
                        myScroll.refresh();

                    }else {
                        $('#noData').show();
                    }
                }


            }
        )
        requestData.index++;
    }



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