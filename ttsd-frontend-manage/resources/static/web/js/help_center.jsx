require('webStyle/help_center.scss');
require('webJsModule/touch_menu');
let commonFun= require('publicJs/commonFun');
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

if($helpContainer.length){
    var type = sourceKind.params.type;
    var question = sourceKind.params.question;
    var indexs = sourceKind.params.index;
    var typeStr = '';
    var typeHref = '';
    var questionHref = '';
    var questionStr = '';
    $('.left-nav').find('li a').removeClass('active');
    switch (type) {
        case 'registerBar':
            $('.left-nav').find('li').eq(0).find('a').addClass('active');
            typeStr='注册认证';
            typeHref= '/help/account';
            if(question == 'register'){
                questionHref = '/help/account#11';
                questionStr = '注册';
            }else if(question == 'account'){
                questionHref = '/help/account#21';
                questionStr = '认证';
            }
            break;
        case 'moneyBar':
            $('.left-nav').find('li').eq(2).find('a').addClass('active');
            typeStr='资金相关';
            typeHref= '/help/money';
            if(question == 'recharge'){
                questionHref = '/help/money#11';
                questionStr = '充值';
            }else if(question == 'invest'){
                questionHref = '/help/money#21';
                questionStr = '投资';
            }else if(question == 'payments'){
                questionHref = '/help/money#31';
                questionStr = '回款';
            }else if(question == 'cash'){
                questionHref = '/help/money#41';
                questionStr = '提现';
            }else if(question == 'found'){
                questionHref = '/help/money#51';
                questionStr = '资金存管';
            }
            break;
        case 'productBar':
            $('.left-nav').find('li').eq(3).find('a').addClass('active');
            typeStr='产品类型';
            typeHref= '/help/product';
            if(question == 'mortgage'){
                questionHref = '/help/product#11';
                questionStr = '抵押贷';
            }else if(question == 'transferRights'){
                questionHref = '/help/product#21';
                questionStr = '债权转让';
            }
            break;
        case 'otherBar':
            $('.left-nav').find('li').eq(4).find('a').addClass('active');
            typeStr='其他问题';
            typeHref= '/help/other';
            if(question == 'app'){
                questionHref = '/help/other#11';
                questionStr = '手机客户端';
            }
            break;
        case 'accountBar':
            $('.left-nav').find('li').eq(1).find('a').addClass('active');
            typeStr='账户管理';
            typeHref= '/help/user';
            if(question == 'password'){
                questionHref = '/help/user#11';
                questionStr = '密码设置';
            }else if(question == 'vip'){
                questionHref = '/help/user#21';
                questionStr = 'VIP会员';
            }else if(question == 'bankAccount'){
                questionHref = '/help/user#31';
                questionStr = '银行卡认证及更换';
            }
            break;

    }

    var navStr = '<a href="/help/help-center">帮助中心></a><a href="'+typeHref+'">'+typeStr+'></a><a href="'+questionHref+'">'+questionStr+'></a>'+helper.helperCenterData[type][question][indexs].title;
    $('#questionTitle').html(helper.helperCenterData[type][question][indexs].title);
    $('#helpContent').html(helper.helperCenterData[type][question][indexs].answer);
   $('#nav').html(navStr);
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