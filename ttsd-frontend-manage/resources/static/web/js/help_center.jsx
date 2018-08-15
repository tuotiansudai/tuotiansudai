require('webStyle/help_center.scss');
require('webJsModule/touch_menu');
var helper = require('webJs/help_center_data.jsx')
let helpCenterImgBarUrl = require('webImages/helpcenter/help-center.png');
let $helpCenter = $("#helpCenter");
var tpl = require('art-template/dist/template');


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

var registerStr = '';

var registerList = helper.helperCenterData.registerBar.register;
for(var i = 0;i<registerList.length;i++){
    registerStr+='<li class="problem-single-item">\n' +
        '\n' +
        '    <p class="single-title">'+(i+1)+'、'+registerList[i].title+'</p>\n' +
        '\n' +
        '    <p class="single-answer">答：registerList[i].answer</p>\n' +
        '    </li>';
}

$('#registerList').html(registerStr);