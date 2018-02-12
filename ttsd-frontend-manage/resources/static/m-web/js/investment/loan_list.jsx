require('mWebStyle/investment/loan_list.scss');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');
require('webJs/plugins/autoNumeric');

let $amontDom = $('.money');
$amontDom.autoNumeric('init');

let $content = $('.loan-list-content .category-box-main');


let $loanList = $('#loanList'),
    $targetCategoryBox = $('.target-category-box', $loanList),
    $categoryBoxMain = $('.category-box-main',$loanList);


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

//点击btn跳转到购买loanList
$loanList.on('click','.goToDetail',function (e) {
    e.preventDefault();
    e.stopPropagation();
    var url = $(this).data('url')
    $.when(commonFun.isUserLogin())
        .done(function () {
            if(!$(this).hasClass('preheat-btn')){
                location.href = url+'#buyDetail';
            }

        }).fail(function () {
        location.href = '/m/login'
    })

})
$loanList.on('click','[data-url]',function(event) {
    event.preventDefault();
    event.stopPropagation();
    if(!$(this).hasClass('btn-invest')&&!$(this).hasClass('btn-normal')||$(this).hasClass('preheat-btn')){
        let $this=$(this),
            url=$this.data('url');
        location.href=url;
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
            console.log(window.location)

            if($(data).find("#wrapperOut .loan-list-content .category-box-main").html().trim().length > 0){
                $content.append($(data).find("#wrapperOut .loan-list-content .category-box-main").html());
                $('.money').autoNumeric('init');

                myScroll.refresh();

            }else {
                $('#pullUp').find('.pullUpLabel').html('没有更多数据了')
            }

        }
    )
}


$('.goToExDetail').on('click',function (e) {
    e.preventDefault();
    e.stopPropagation();
    console.log($(this).parent('.target-category-box'))
    var url = $(this).data('url')
    $.when(commonFun.isUserLogin())
        .done(function () {
            location.href = url+'#applyTransfer';
        }).fail(function () {
        location.href = '/m/login'
    })

})

$loanList.on('click','.goToTranDetail',function (e) {
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
//开标倒计时

    let $preheat=$('.preheat');

    function countDownLoan(domElement) {
        return $(domElement).each(function () {
            let $this = $(this);
            let countdown=$this.data('time');
            console.log(countdown)
            if(countdown > 0) {
                let timer= setInterval(function () {
                    let $minuteShow=$this.find('.minute_show'),
                        $secondShow=$this.find('.second_show'),
                        minute=Math.floor(countdown/60),
                        second=countdown%60;
                    if (countdown == 0) {
                        //结束倒计时
                        clearInterval(timer);
                        $('.preheat-status').removeClass('preheat-btn').text('立即投资');
                        $this.remove();
                    }
                    minute=(minute <= 9)?('0' + minute):minute;
                    second=(second <= 9)?('0' + second):second;
                    $minuteShow.text(minute);
                    $secondShow.text(second);
                    countdown--;
                },1000);
            }

        });
    };
    countDownLoan('.preheat');
let $myMenu = $('.menu-my');
if($myMenu.length){
    $myMenu.on('click',function (e) {
        e.preventDefault();
        $.when(commonFun.isUserLogin())
            .done(function () {
                location.href = '/m/account'
            }).fail(function () {
            location.href = '/m/account-anonymous'
        })

    })
}
