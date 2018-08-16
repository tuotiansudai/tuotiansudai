require('webStyle/help_center.scss');
require('webJsModule/touch_menu');
var helper = require('webJs/help_center_data.jsx')
let helpCenterImgBarUrl = require('webImages/helpcenter/help-center.png');
let $helpCenter = $("#helpCenter");
let tpl = require('art-template/dist/template');
var sourceKind = globalFun.parseURL(location.href);


$helpCenter.find("img.help-center-bar").attr("src", helpCenterImgBarUrl);

let $changeBtn = $('.problem-title-item span'),
    $contentList = $('.problem-content-item'),
    $problem = $('.problem-single-item'),
    page = location.href.split('#')[1] ? parseInt(location.href.split('#')[1].slice(0, 1)) - 1 : false,
    index = location.href.split('#')[1] ? parseInt(location.href.split('#')[1].slice(1, 2)) - 1 : false;

//切换类型
$changeBtn.on('click', function (event) {
    event.preventDefault();
    let $self = $(this),
        index = $self.index();
    $self.addClass('active').siblings().removeClass('active');
    $contentList.find('.list-group:eq(' + index + ')').addClass('active')
        .siblings('.list-group').removeClass('active');
});

$problem.on('click', '.single-title', function (event) {
    event.preventDefault();
    var $self = $(this);
    $self.siblings('.single-answer').slideToggle('fast', function () {
        $self.parent().hasClass('active') ? $self.parent().removeClass('active').find('.fa').removeClass('fa-angle-up').addClass('fa-angle-down') : $self.parent().addClass('active').find('.fa').removeClass('fa-angle-down').addClass('fa-angle-up');
    });
}).on('click', '.fa', function (event) {
    event.preventDefault();
    $(this).siblings('.single-title').trigger('click')
});

$('.problem-title-item span:eq(' + page + ')').trigger('click');
$('.list-group:eq(' + page + ')').find('.problem-single-item:eq(' + index + ') .single-title').trigger('click');

getData($('#registerList'),'registerBar','register');
getData($('#accountList'),'registerBar','account');
getData($('#passwordList'),'accountBar','password');
getData($('#vipList'),'accountBar','vip');
getData($('#bankAccountList'),'accountBar','bankAccount');

getData($('#rechargeList'),'moneyBar','recharge');
getData($('#investList'),'moneyBar','invest');
getData($('#paymentsList'),'moneyBar','payments');
getData($('#cashList'),'moneyBar','cash');
getData($('#fundList'),'moneyBar','found');

getData($('#mortgageList'),'productBar','mortgage');
getData($('#transferRightsList'),'productBar','transferRights');

getData($('#otherList'),'otherBar','app');

// passwordList
var pageType = location.href.split('/')[location.href.split('/').length-1] ? location.href.split('/')[location.href.split('/').length-1]:'';

function getData(rootDOM,type,question) {

    var str = '';

    var list = helper.helperCenterData[type][question];
    for(var i = 0;i<list.length;i++){
        str+='<li class="problem-single-item">\n' +
            '\n' +
            '    <p class="single-title" data-type="'+type+'" data-question="'+question+'" data-index="'+i+'"><a href="help-content?type='+type+'&question='+question+'&index='+i+'">'+(i+1)+'、'+list[i].title+'</a></p>\n' +
            '\n' +
            '    </li>';
    }
    rootDOM.html(str);
}

var $helpContainer = $('#helpContentContainer');
// var selectedResolvedImg = require('../images/helpcenter/icon_resoved_selected.png');
// var selectedUnsolveImg = require('../images/helpcenter/icon_unresolve_selected.png');

if($helpContainer.length){
    var type = sourceKind.params.type;
    var question = sourceKind.params.question;
    var indexs = sourceKind.params.index;
    $('#questionTitle').html(helper.helperCenterData[type][question][indexs].title);
    $('#helpContent').html(helper.helperCenterData[type][question][indexs].answer);
    $('#resolvedBtn').on('click',function () {
        commonFun.useAjax({
                url: 'XXX',
                type: 'POST'
            }, function (res) {
                if(res.status == true){
                    $(this).addClass('selected').siblings().removeClass('selected');
                }else {
                    layer.msg(res.message)
                }

            }
        )


    })
    $('#unsolvedBtn').on('click',function () {
        commonFun.useAjax({
                url: 'XXX',
                type: 'POST'
            }, function (res) {
                if(res.status == true){
                    $(this).addClass('selected').siblings().removeClass('selected');

                }else {
                    layer.msg(res.message)
                }

            }
        )

    })

}