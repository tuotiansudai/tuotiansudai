require('mWebStyle/investment/loan_list.scss');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');

let $content = $('.loan-list-content .category-box-main');


let $loanList = $('#loanList'),
    $targetCategoryBox = $('.target-category-box', $loanList);


$targetCategoryBox.on('click', function () {
    let $this = $(this);
    location.href = $this.data('url');
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});


myScroll.on('scrollEnd', function () {
    console.log(this.y + "|||" + this.maxScrollY);
    //如果滑动到底部，则加载更多数据（距离最底部10px高度）
    if ((this.y - this.maxScrollY) <= 10) {
        getMore();
    }
});

let pagenum = 1;//当前页数
//获取更多数据
function getMore() {
    let that = $(".pullUpLabel");//更多按钮
    pagenum++;

    commonFun.useAjax(
        {
            url: window.location + "?index=" + pagenum,
            dataType: 'html',
            type: 'get',
        },
        function (data) {
            $content.append($(data).find("#wrapperOut .loan-list-content .category-box-main").html());
            myScroll.refresh();
        }
    )
}
