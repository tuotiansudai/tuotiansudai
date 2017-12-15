require('mWebStyle/investment/loan_list.scss');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');

let $content = $('.loan-list-content');
let data = {
    "count": 1,
    "has_next": false,
    "has_previous": false,
    "page": 1,
    "page_count": 1,
    "results": [
        {
            "id": 1,
            "financing_deadline": "2018-01-01",
            "created_time": "2018-01-01T00:00:00Z",
            "updated_time": "2018-01-01T00:00:00Z",
            "upstream_firm_name": "sfasf",
            "upstream_firm_legal_person": "asf",
            "upstream_firm_mobile": "123123",
            "credit_limit": 123123,
            "approve_limit": 123,
            "check_taker": "12313",
            "guarantor": "123",
            "ticket_number": "123",
            "ticket_image": "123",
            "invoice": 1
        },
        {
            "id": 1,
            "financing_deadline": "2018-01-01",
            "created_time": "2018-01-01T00:00:00Z",
            "updated_time": "2018-01-01T00:00:00Z",
            "upstream_firm_name": "sfasf",
            "upstream_firm_legal_person": "asf",
            "upstream_firm_mobile": "123123",
            "credit_limit": 123123,
            "approve_limit": 123,
            "check_taker": "12313",
            "guarantor": "123",
            "ticket_number": "123",
            "ticket_image": "123",
            "invoice": 1
        },
        {
            "id": 1,
            "financing_deadline": "2018-01-01",
            "created_time": "2018-01-01T00:00:00Z",
            "updated_time": "2018-01-01T00:00:00Z",
            "upstream_firm_name": "sfasf",
            "upstream_firm_legal_person": "asf",
            "upstream_firm_mobile": "123123",
            "credit_limit": 123123,
            "approve_limit": 123,
            "check_taker": "12313",
            "guarantor": "123",
            "ticket_number": "123",
            "ticket_image": "123",
            "invoice": 1
        }
    ]
} ;

let $data = JSON.parse(JSON.stringify(data));


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
getMore();//获取更多数据

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
