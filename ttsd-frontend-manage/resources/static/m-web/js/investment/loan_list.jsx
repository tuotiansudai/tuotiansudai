require('mWebStyle/investment/loan_list.scss');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');
require('webJs/plugins/autoNumeric');

let $amontDom = $('.amontDom');
$amontDom.autoNumeric('init');
let $content = $('.loan-list-content .category-box-main');


let $loanList = $('#loanList'),
    $targetCategoryBox = $('.target-category-box', $loanList),
    $categoryBoxMain = $('.category-box-main',$loanList);


$('.abc').on('click',function () {
    location.href='/m/loan/1';
})

let myScroll = new IScroll('#wrapperOut', {
    probeType: 2,
    mouseWheel: true,
    click: true
});

$('[data-url]',$categoryBoxMain).on('click',function(event) {
    event.preventDefault();
    let $this=$(this),
        url=$this.data('url');
   // location.href=url;
    location.href='/m/loan/1';
});

myScroll.on('scrollEnd', function () {
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
            if($(data).find("#wrapperOut .loan-list-content .category-box-main").html().trim().length > 0){
                $content.append($(data).find("#wrapperOut .loan-list-content .category-box-main").html());

                myScroll.refresh();
            }else {
                $('#pullUp').find('.pullUpLabel').html('没有更多数据了')
            }

        }
    )
}

