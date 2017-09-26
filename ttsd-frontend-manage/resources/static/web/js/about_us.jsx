require('webStyle/about_us.scss');
require('publicJs/pagination');
require('webJsModule/touch_menu');
let paginationElement = $('.pagination');

//拓天公告
var $noticeList=$('#noticeList'),
    $noticeDetail=$('#noticeDetail');
if($noticeList.length) {
    let noticeTpl=$('#noticeListTemplate').html();
    let ListRender = _.template(noticeTpl);
    let requestData={"index":1,"pageSize":10};
    paginationElement.loadPagination(requestData, function (data) {

        let html = ListRender(data);
        $noticeList.html(html);
        $noticeList.find('span').each(function(key,option) {
            var getTime=$(option).text();
            $(option).text(getTime.substr(0,10));
        });
        if(/app/gi.test(location.search)) {
            $noticeList.find('li a').each(function(key,option) {
                var thisURL= $(option).attr('href')+'?source=app';
                $(option).attr('href',thisURL);
            });
        }
    });
}

let $companyPhoto = $('#companyPhoto');

let photoGroup={
    '1': {
        small:require('../images/sign/aboutus/photo_01.jpg'),
        big:require('../images/sign/aboutus/photo_big_01.jpg')
    },
    '2':{
        small:require('../images/sign/aboutus/photo_02.jpg'),
        big:require('../images/sign/aboutus/photo_big_02.jpg')
    },
    '3':{
        small:require('../images/sign/aboutus/photo_03.jpg'),
        big:require('../images/sign/aboutus/photo_big_03.jpg')
    },
    '4':{
        small:require('../images/sign/aboutus/photo_04.jpg'),
        big:require('../images/sign/aboutus/photo_big_04.jpg')
    },
    '5':{
        small:require('../images/sign/aboutus/photo_05.jpg'),
        big:require('../images/sign/aboutus/photo_big_05.jpg')
    }
};
$companyPhoto.find('li').each(function(key,option) {
    let num = key+1;
    $(option).find('a').attr('href',photoGroup[num].big);
    $(option).find('a').append(`<img src="${photoGroup[num].small}">`);
});
//团队介绍
let fancybox = require('publicJs/fancybox');
fancybox(function() {
    $("#companyPhoto li a").fancybox({
        'titlePosition' : 'over',
        'cyclic'        : false,
        'showCloseButton':true,
        'showNavArrows' : true,
        'titleFormat'   : function(title, currentArray, currentIndex, currentOpts) {
            return '<span id="fancybox-title-over">' + (currentIndex + 1) + ' / ' + currentArray.length + (title.length ? ' &nbsp; ' + title : '') + '</span>';
        }
    });
});

//问题列表
require.ensure([],function() {
    let $problemListFrame=$('#problemListFrame');
    if(!$problemListFrame.length) {
        return;
    }
    let $problemList=$('.problem-list dt span',$problemListFrame);
    $problemList.on('click', function(e) {
        e.preventDefault();
        var $self=$(this),
            $dtDom=$self.parent('dt'),
            $parents=$dtDom.parent();

        if($dtDom.next().hasClass('active')){
            $dtDom.next().removeClass('active');
            $dtDom.find('i').removeClass('fa-toggle-down').addClass('fa-toggle-up');
        }else{
            $parents.find('dd').removeClass('active');
            $parents.find('i').removeClass('fa-toggle-down').addClass('fa-toggle-up');
            $dtDom.next().addClass('active');
            $dtDom.find('i').removeClass('fa-toggle-up').addClass('fa-toggle-down');
        }
    });

},'qaList');

//运营数据
require.ensure(['publicJs/load_echarts','publicJs/commonFun'],function() {
    let loadEcharts = require('publicJs/load_echarts');
    let commonFun=require('publicJs/commonFun');
    if (!$("#dataRecord").length) {
        return;
    }
    commonFun.useAjax({
        url: '/about/operation-data/chart',
        type: 'GET'
    },function(data) {
        $('#operationDays').text(data.operationDays+'天');
        $('#usersCount').text(data.usersCount+'人');
        $('#tradeAmount').text(data.tradeAmount+'元');
        var dataJson = {
                title:'拓天速贷',
                sub:'金额',
                name:'运营数据',
                month:data.month,
                money:data.money
            },
            option = loadEcharts.ChartOptionTemplates.BarOption(dataJson);
          var  opt = loadEcharts.ChartConfig('dataRecord', option);
        loadEcharts.RenderChart(opt);
    });

},'operationEcharts');
