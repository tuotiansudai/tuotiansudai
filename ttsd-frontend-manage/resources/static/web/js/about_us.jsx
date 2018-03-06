require('webStyle/about_us.scss');
require('publicJs/pagination');
require('webJsModule/touch_menu');
let echarts = require('echarts');
let paginationElement = $('.pagination');
let leftMenuBox = globalFun.$('#leftMenuBox');
//手机端菜单滑动


(function(){
    let browser = $(window).width();
    if(browser<1000) {
        let menuLen = $(leftMenuBox).find('li:visible').length;
        let screenW = $(window).width(),
            showMenuNum = 3, //希望一屏展示3个菜单
            someLiW = screenW/showMenuNum,
            totalWidth = someLiW * menuLen;
        $(leftMenuBox).find('ul').width(totalWidth);
        $(leftMenuBox).find('li').css({"width":someLiW});

        //判断当前激活的菜单在可视区域
        let slipAway = (function() {
            let currentLi = $(leftMenuBox).find('li').filter(function(key,option) {
                return $(option).find('a').hasClass('active');
            });
            let hideLi = $(leftMenuBox).find('li:hidden').index();

            let currentOrder = currentLi.index();
            if(currentOrder>hideLi) {
                currentOrder = currentOrder -1;
            }
            let curOrder = parseInt(currentOrder/showMenuNum);
            let moveInit = -curOrder*screenW + 'px';
            $(leftMenuBox).find('ul').css({
                '-webkit-transform':"translate("+moveInit+")",
                '-webkit-transition':'10ms linear'
            });
            return curOrder;
        })();

        let touchSlide = require('publicJs/touch_slide');
        let num=slipAway * showMenuNum;
        touchSlide.options.sliderDom = leftMenuBox;
        touchSlide.finish = function() {

            let direction = touchSlide.options.moveDirection,
                moveDistance;
            //如果没有任何滑动迹象，不左处理
            if(!this.options.moveDirection.horizontal) {
                return;
            }
            if(direction.rtl && num<menuLen-showMenuNum) {
                //从右到左
                num++;

            } else if(direction.ltr && num>0) {
                //从左到右
                num--;
            }

            moveDistance = - someLiW*num + 'px';
            $(leftMenuBox).find('ul').css({
                '-webkit-transform':"translate("+moveDistance+")",
                '-webkit-transition':'100ms linear'
            });
        }
        touchSlide.init();
    }
}());

//拓天公告
let $noticeList=$('#noticeList'),
    $noticeDetail=$('#noticeDetail');
let $leftMenuBox = $('#leftMenuBox');

let redirect = globalFun.browserRedirect();
if(redirect=='mobile') {
    $leftMenuBox.find('li').eq(3).hide();
}
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

//营业执照
let $organizationalImg = $('#organizationalImg');

let organizationalImg={
    '1': {
        small:require('../images/sign/aboutus/aptitude_one_small.png'),
        big:require('../images/sign/aboutus/aptitude_one_big.png')
    },
    '2':{
        small:require('../images/sign/aboutus/aptitude_two_small.png'),
        big:require('../images/sign/aboutus/aptitude_two_big.png')
    }
};
$organizationalImg.find('li').each(function(key,option) {
    let num = key+1;
    $(option).find('a').attr('href',organizationalImg[num].big);
    $(option).find('a').append(`<img src="${organizationalImg[num].small}">`);
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
fancybox(function() {
    $("#organizationalImg li a").fancybox({
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
let drawBarTransverse = (cityName, cityData, colorArr) => { // 横向柱状图
    return {
        xAxis: {type: 'value', show: false},
        yAxis: {
            type: 'category',
            data: cityName,
            axisLine: {show: false},
            axisTick: {show: false},
            axisLabel: {show: false}
        },
        series: [{
            type: 'bar',
            barWidth: '80%',
            data: cityData,
            silent: true,
            itemStyle: {
                normal: {
                    color: function (params) {
                        let colorList = colorArr;
                        return colorList[params.dataIndex];
                    }
                },
            }
        }],
        label: {
            normal: {
                show: true,
                position: 'right',
                color: '#333',
                formatter: '{b}\n{c}%'
            }
        }
    }
};
let investRegion = echarts.init(document.getElementById('investRegionRecord'));
let loanRegion = echarts.init(document.getElementById('loanRegionRecord'));
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
        console.log(data);
        $('#operationDays').text(data.operationDays);
        $('#usersCount').text(data.usersCount);
        $('#tradeAmount').text(data.tradeAmount);
        var dataJson = {

                sub:'金额',
                name:'运营数据',
                month:data.month,
                money:data.money
            },
            option = loadEcharts.ChartOptionTemplates.BarOption(dataJson);
          var  opt = loadEcharts.ChartConfig('dataRecord', option);
        loadEcharts.RenderChart(opt);
        //投资人基本信息 环形图
         var optionUser = loadEcharts.ChartOptionTemplates.AnnularOption(data.ageDistribution,'投资用户(人)');
        var  optUser = loadEcharts.ChartConfig('investRecord', optionUser);
        loadEcharts.RenderChart(optUser);
        //投资人男女比例 饼状图
        var sexOptions = [{name:'男性投资人',scale:data.femaleScale},{name:'女性投资人',scale:data.maleScale}];
        var optionSex = loadEcharts.ChartOptionTemplates.PieOptionBaseInfo(sexOptions,'投资人基本信息');
        var  optSex = loadEcharts.ChartConfig('investSexRecord', optionSex);
        loadEcharts.RenderChart(optSex);
        //投资人地域分布
        // var optionRegion = loadEcharts.ChartOptionTemplates.BarOptionXAxis(data.investCityScaleTop3,'投资人基本信息');
        // var  optRegion = loadEcharts.ChartConfig('investRegionRecord', optionRegion);
        // loadEcharts.RenderChart(optRegion);

        let investCityScaleTop3 = data.investCityScaleTop3; // 投资人数top3
        let investAmountScaleTop3 = data.investAmountScaleTop3; // 投资金额top3
        let cityName_count = [];
        let cityData_count = [];
        let cityName_amount = [];
        let cityData_amount = [];
        investCityScaleTop3.forEach((item, index) => {
            cityName_count[index] = item.city;
            cityData_count[index] = item.scale;
        });
        investAmountScaleTop3.forEach((item, index) => {
            cityName_amount[index] = item.city;
            cityData_amount[index] = item.scale;
        });

        investRegion.setOption(drawBarTransverse(cityName_count, cityData_count, ['#ff9b1b', '#ff9b1b', '#ff9b1b']));
        //借款人基本信息环形图
        var optionLoan = loadEcharts.ChartOptionTemplates.AnnularOption(data.ageDistribution,'投资用户(人)');
        var  optLoan = loadEcharts.ChartConfig('loanBaseRecord', optionLoan);
        loadEcharts.RenderChart(optLoan);
        //投资人男女比例 饼状图
        var sexLoanOptions = [{name:'男性投资人',scale:data.femaleScale},{name:'女性投资人',scale:data.maleScale}];
        var optionSexLoan = loadEcharts.ChartOptionTemplates.PieOptionBaseInfo(sexLoanOptions,'投资人基本信息');
        var  optSexLoan = loadEcharts.ChartConfig('loanBaseSexRecord', optionSexLoan);
        loadEcharts.RenderChart(optSexLoan);
        //借款人地域分布
        loanRegion.setOption(drawBarTransverse(cityName_amount, cityData_amount, ['#ff9b1b', '#ff9b1b', '#ff9b1b']));
    });

},'operationEcharts');

var img = new Image;
var imgUr = require('../images/sign/aboutus/organizational_structure.png');
   img.src= imgUr
$('.organizational-structure').append(img);
let getPartFourPage = (data) => {
    let investCityScaleTop3 = data.investCityScaleTop3; // 投资人数top3
    let investAmountScaleTop3 = data.investAmountScaleTop3; // 投资金额top3
    let cityName_count = [];
    let cityData_count = [];
    let cityName_amount = [];
    let cityData_amount = [];
    investCityScaleTop3.forEach((item, index) => {
        cityName_count[index] = item.city;
        cityData_count[index] = item.scale;
    });
    investAmountScaleTop3.forEach((item, index) => {
        cityName_amount[index] = item.city;
        cityData_amount[index] = item.scale;
    });
    myChart5.setOption(drawBarTransverse(cityName_count, cityData_count, ['#c2eef2', '#81e9f2', '#00def2']));
    myChart6.setOption(drawBarTransverse(cityName_amount, cityData_amount, ['#ffecac', '#ffd74f', '#ffc601']));
};
