require('webStyle/help_center.scss');
require('webJsModule/touch_menu');
let commonFun= require('publicJs/commonFun');
var helper = require('webJs/help_center_data.jsx')
let helpCenterImgBarUrl = require('webImages/helpcenter/help-center.png');
let $helpCenter = $("#helpCenter");
let tpl = require('art-template/dist/template');
var sourceKind = globalFun.parseURL(location.href);

//左侧菜单栏
let $leftBox = $('#helpLeftMenuBox');
let $titleBox = $leftBox.find('.swiper-slide').find('>a');
let $childBox = $leftBox.find('.swiper-slide').find('ul');

$helpCenter.find("img.help-center-bar").attr("src", helpCenterImgBarUrl);

let $changeBtn = $('.problem-title-item span'),
    $contentList = $('.problem-content-item'),
    $problem = $('.problem-single-item'),
    page = location.href.split('#')[1] ? parseInt(location.href.split('#')[1].slice(0, 1)) - 1 : false,
    index = location.href.split('#')[1] ? parseInt(location.href.split('#')[1].slice(1, 2)) - 1 : false;

$(window).on('hashchange',function () {
    whichShow();
})
whichShow();

let $resolvedBtn = $('.resolved-btn');
let $unsolvedBtn = $('.unsolved-btn');
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
    let isSolution = -1;
    let contentId = type+','+question+','+indexs;
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
    $('.solve-btn').on('click',function () {
        let _self = $(this);


        if(_self.hasClass('resolved-btn')){
            isSolution = 1;
        }else if(_self.hasClass('unsolved-btn')){
            isSolution = 0;
        }
        if(_self.hasClass('disabled')||_self.hasClass('selected')){
            layer.msg('您已参与过投票');
            return;
        }

        selectSquare(contentId,isSolution)


    })
    selectSquare(contentId)

}

function selectSquare(contentId,isSolution) {
    let url;
    if(isSolution!==undefined){
        url = '/help/help-content/vote?contentId='+contentId+'&isSolution='+isSolution;
    }else {
        url = '/help/help-content/vote?contentId='+contentId
    }
    commonFun.useAjax({
            url: url,
            type: 'POST'
        }, function (res) {
            if(res.status ===true){
                $('#person').text(res.data.voteNumber);
                if(res.data.isSolution===1){
                    $resolvedBtn.addClass('selected');
                    $unsolvedBtn.addClass('disabled');
                }else if(res.data.isSolution===0){
                    $unsolvedBtn.addClass('selected');
                    $resolvedBtn.addClass('disabled');
                }
            }else {
                layer.msg(res.message)
            }

        }
    )
}



$titleBox.on('click',function (e) {
    e.stopPropagation();
    e.preventDefault();
    let self = $(this);
    if(self.parent().find('ul').hasClass('show')){
        self.parent().find('ul').removeClass('show')
    }else {
        self.parent().find('ul').addClass('show')
    }
    if(self.find('.icon-arrow').hasClass('down')){
        self.find('.icon-arrow').removeClass('down')
    }else {
        self.find('.icon-arrow').addClass('down')
    }

    if(self.find('.icon-arrow').hasClass('whiteDown')){
        self.find('.icon-arrow').removeClass('whiteDown').addClass('whiteRight');
    }else if(self.find('.icon-arrow').hasClass('whiteRight')){
        self.find('.icon-arrow').removeClass('whiteRight').addClass('whiteDown');
    }
})

function whichShow() {
    let title = location.href.split('#')[0].split('/')[location.href.split('#')[0].split('/').length-1],
        page2 = location.href.split('#')[1] ? parseInt(location.href.split('#')[1].slice(0, 1)) - 1 : false

    $contentList.find('.list-group:eq(' + page2 + ')').addClass('active')
        .siblings('.list-group').removeClass('active');
    $changeBtn.eq(page2).addClass('active').siblings().removeClass('active');


    switch (title) {
        case 'account':
            $leftBox.find('.swiper-slide').eq(0).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(0).find('>a').addClass('active');
            $leftBox.find('.swiper-slide').eq(0).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(0).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
        case 'user':
            $leftBox.find('.swiper-slide').eq(1).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(1).find('>a').addClass('active');
            $leftBox.find('.swiper-slide').eq(1).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(1).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
        case 'money':
            $leftBox.find('.swiper-slide').eq(2).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(2).find('>a').addClass('active');
            $leftBox.find('.swiper-slide').eq(2).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(2).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
        case 'product':
            $leftBox.find('.swiper-slide').eq(3).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(3).find('>a').addClass('active');
            $leftBox.find('.swiper-slide').eq(3).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(3).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
        case 'other':
            $leftBox.find('.swiper-slide').eq(4).find('>a').find('.icon-arrow').addClass('whiteDown');
            $leftBox.find('.swiper-slide').eq(4).find('a').addClass('active');
            $leftBox.find('.swiper-slide').eq(4).find('ul').addClass('show');
            $leftBox.find('.ulSection').eq(4).find('li').eq(page2).find('a').addClass('active').end().siblings().find('a').removeClass('active');
            break;
    }
}