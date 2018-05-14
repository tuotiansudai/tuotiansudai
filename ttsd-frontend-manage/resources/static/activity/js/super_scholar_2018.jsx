require("activityStyle/super_scholar_2018.scss");
require('publicJs/plugins/jquery.qrcode.min');
require('publicJs/plugins/jQuery.md5');


let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');
//require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);
let url = location.href;

let $getMore = $('#getMore'),
    $getLess = $('#getLess'),
    $reappearanceList = $('#reappearanceList'),
    $reappearanceContent = $('.reappearance-content');
commonFun.calculationRem(document, window)

//heroRank();
$getMore.on('click', function () {
    let _this = $(this);
    _this.hide();
    $getLess.show();
    getMoreData();
})

$getLess.on('click', function () {
    let _this = $(this);
    _this.hide();
    $getMore.show();
    getLess();
})

function getLess() {
    let liHeihgt = $reappearanceList.find('tr').outerHeight();
    let thHeight = $reappearanceContent.find('thead').outerHeight();
    let totalHeight = 3 * liHeihgt + thHeight + 2;
    $reappearanceContent.height(totalHeight);
}

function getMoreData(num) {
    // let liHeihgt = $reappearanceList.find('tr').outerHeight();
    // let thHeight =  $reappearanceContent.find('thead').outerHeight();
    // let totalHeight = num*liHeihgt+thHeight+2;
    // $reappearanceContent.height(totalHeight);
}

function heroRank(records, callback) {
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

let $questionContainer = $('.question-container'),
    $questionList = $('#questionList'),
    $questionBtn = $('.question-btn');

if ($questionContainer.length) {
    var data = {
        questions: [
            {
                "question": "ICP备案ICP经营许可证，是一回事吗？( )",
                "options": [
                    "A、是一回事",
                    "B、不是一回事，ICP经营许可证要求更加严格"
                ]
            },
            {
                "question": "以下哪个名字不是小编的惯用马甲( )",
                "options": [
                    "A、小明",
                    "B、拓小天",
                    "C、小拓拓"
                ]
            },
            {
                "question": "在线上没有活动的时候，拓天速贷平台180天项目预期年化收益率是多少？( )",
                "options": [
                    "A、9%",
                    "B、10%",
                    "C、11%"
                ]
            },
            {
                "question": "在线上没有活动的时候，拓天速贷平台180天项目预期年化收益率是多少？( )",
                "options": [
                    "A、9%",
                    "B、10%",
                    "C、11%"
                ]
            },
            {
                "question": "购买银行理财产品，错误的做法是( )",
                "options": [
                    "A、信赖银行，把钱交给理财经理就好",
                    "B、要在银行柜台完成申购手续"
                ]
            }
        ]
    }

    $questionList.html(tpl('questionTpl', data));
    $('.question-inner').find('li').on('click', function () {
        let _this = $(this);
        _this.addClass('active').siblings().removeClass('active');
    })
    let questions = [];
    let questionFlag = true;

    $questionList.find('.question-btn').on('click', function () {
            let _self = $(this);
            let count = 0;
            let error = false;
            let answer;
            _self.parents('.question-inner').find('li').each(function (index, item) {

                if ($(item).hasClass('active')) {

                    if (index == 0) {
                        answer = 'A';
                    } else if (index == 1) {
                        answer = 'B';
                    } else if (index == 2) {
                        answer = 'C';
                    } else if (index == 3) {
                        answer = 'D';
                    }


                    count++;
                    return false;

                }


            })

            if (count < 1) {
                layer.msg('未选择题目');
                _self.addClass('error')
                return false;
            }else {
                questions.push(answer);
            }

            if (_self.parents('.question-inner').index() !== 4) {
                _self.parents('.question-inner').hide()
            }
        }
    )

    $('.inner5').find('.question-btn').on('click', function () {
        if($(this).hasClass('error')){
            location.reload();
        }else {
            console.log(questions)
        }



    })


}



