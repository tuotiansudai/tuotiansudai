require("activityStyle/super_scholar_2018.scss");
require('publicJs/plugins/jquery.qrcode.min');
let $qrcodeWap = $('#qrcodeWap');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');
let sourceKind = globalFun.parseURL(location.href);
let equipment = globalFun.equipment();
let url;
if (sourceKind.port) {
    url = sourceKind.protocol + '://' + sourceKind.host + ':' + sourceKind.port;
} else {
    url = sourceKind.protocol + '://' + sourceKind.host;
}
if ($qrcodeWap.length) {
    let imgUrl = require('../images/2018/super-scholar/app_code.png');
    $qrcodeWap.find('img').attr('src', imgUrl);
}
let isWeixin = equipment.wechat;
if (isWeixin) {
    $('#immediateAnswer').on('click', function () {
        $.when(commonFun.isUserLogin())
            .done(function () {
                location.href = '/activity/super-scholar/view/question'
            })
            .fail(function () {
                location.href = '/m/login?redirect=/activity/super-scholar/view/question';
            })
    })
    $qrcodeWap.hide();

} else {
    $('.immediate-answer').hide();
}

let $getMore = $('#getMoreData'),
    $reappearanceList = $('#reappearanceList'),
    $reappearanceContent = $('#reappearanceContent');
commonFun.calculationRem(document, window)

let len = $reappearanceList.find('tr').length;
if (len < 3) {
    getMoreData(len);
    $getMore.hide();
} else {
    getMoreData(3);
    $getMore.show();
}

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
let isDoneQuestion = $('#isDoneQuestion').val();

let $qrcodeBox = $('#qrcodeBox');
let qrcodeWidth = $qrcodeBox.width();
let qrcodeHeight = $qrcodeBox.height();


if ($questionContainer.length) {
    $.when(commonFun.isUserLogin())
        .done(function () {
            if (isDoneQuestion == 'false') {
                commonFun.useAjax({
                    dataType: 'json',
                    url: '/activity/super-scholar/questions',
                    type: 'get'

                }, function (response) {
                    $questionList.html(tpl('questionTpl', {questions: response.data}));
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
                                layer.msg('请选择答案！', {time: 400});
                                _self.addClass('error');
                                return false;
                            } else {
                                questions.push(answer);
                                _self.removeClass('error');
                            }

                            if (_self.parents('.question-inner').index() !== 4) {
                                _self.parents('.question-inner').hide()
                            }
                        }
                    )

                    $('.inner5').find('.question-btn').on('click', function () {

                        if (questionFlag) {


                            if ($(this).hasClass('error')) {
                                layer.msg('请选择答案！');
                                $(this).removeClass('error');
                            } else {
                                console.log(questions.join(''))
                                commonFun.useAjax({
                                    dataType: 'json',
                                    url: '/activity/super-scholar/submit/answer',
                                    type: 'post',
                                    data: {
                                        answer: questions.join(',')
                                    }

                                }, function (response) {
                                    if (response.status == true) {
                                        location.href = '/activity/super-scholar/view/result'
                                    } else {
                                        layer.msg('答题失败，请稍后再试！')
                                    }
                                })

                            }
                        }
                        questionFlag = false;


                    })

                })


            } else {
                location.href = '/activity/super-scholar/view/result';
            }

        })
        .fail(function () {
            if (sourceKind.params.source == 'app') {
                location.href = '/login';
            } else {
                location.href = '/m/login?redirect=/activity/super-scholar/view/question';
            }
        });


} else {
    $('#myReappearanceWapBtn').on('click', function () {
        $('#myReappearanceWap').toggleClass('show');
    })
    $('#qrcodeBox').qrcode({
        text: url + '/we-chat/active/authorize?redirect=/activity/super-scholar/view/question',
        width: qrcodeWidth,
        height: qrcodeHeight,
        colorDark: '#1e272e',
        colorLight: '#ffffff',
    }).find('canvas').hide();
    var canvas = $qrcodeBox.find('canvas').get(0);
    $('#rqcodeImg').attr('src', canvas.toDataURL('image/jpg'))
}


