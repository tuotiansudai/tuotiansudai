require("activityStyle/super_scholar_2018.scss");
require('publicJs/plugins/jquery.qrcode.min');
require('publicJs/plugins/jQuery.md5');


let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');
//require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);
let url = location.href;

let $getMore = $('#getMoreData'),
    $reappearanceList = $('#reappearanceList'),
    $reappearanceContent = $('.reappearance-content');
commonFun.calculationRem(document, window)

let len = $reappearanceList.find('tr').length;
getMoreData(3);
$getMore.on('click', function () {
    let _this = $(this);
    _this.hide();
    getMoreData(len);
    $reappearanceContent.css('overflow', 'visible')
})


function getMoreData(num) {
    let liHeihgt = $reappearanceList.find('tr').outerHeight();
    let thHeight = $reappearanceContent.find('thead').outerHeight();
    let totalHeight = num * liHeihgt + thHeight + 2;
    $reappearanceContent.height(totalHeight);
}


let $questionContainer = $('.question-container'),
    $questionList = $('#questionList'),
    $questionBtn = $('.question-btn');

if ($questionContainer.length) {
    commonFun.useAjax({
        dataType: 'json',
        url: '/activity/super-scholar/questions',
        type: 'get'

    }, function (response) {
        $questionList.html(tpl('questionTpl', {questions:response}));
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
                    layer.msg('请选择答案！');
                    _self.addClass('error')
                    return false;
                } else {
                    questions.push(answer);
                }

                if (_self.parents('.question-inner').index() !== 4) {
                    _self.parents('.question-inner').hide()
                }
            }
        )

        $('.inner5').find('.question-btn').on('click', function () {
            if ($(this).hasClass('error')) {
                location.reload();
            } else {
                console.log(questions.join(''))
                commonFun.useAjax({
                    dataType: 'json',
                    url: '/activity/super-scholar/submit/answer',
                    type: 'post',
                    data:{
                        answer:questions.join('')
                    }

                }, function (response) {
                    

                })

            }


        })

    })







}



