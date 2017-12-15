require('mWebStyle/investment/loan_list.scss');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');

let $content = $('.loan-list-content');


let $loanList = $('#loanList'),
    $targetCategoryBox = $('.target-category-box',$loanList);


$targetCategoryBox.on('click',function() {

    let $this = $(this),
        url = $this.data('url');

    location.href = url;
});

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true
});


myScroll.on('scrollEnd',function () {
    console.log(this.y +"|||"+this.maxScrollY);
    //如果滑动到底部，则加载更多数据（距离最底部10px高度）
    if ((this.y - this.maxScrollY) <= 10) {
        getMore();
    }
})
let pagenum = 0;//当前页数
//获取更多数据
function getMore() {
    var that = $(".pullUpLabel");//更多按钮
    pagenum++;


    commonFun.useAjax({
        url:'url',
        type:'get',
        data:{
            pagenum:pagenum
        }
    },
    function (data) {
        that.html('上拉加载更多')
        let investList = tpl('directInvestmentTpl',data);
        $('#scroller').append($(investList));
        myScroll.refresh();
    },function(data){
            that.html('请求数据失败')
    }
)


}
