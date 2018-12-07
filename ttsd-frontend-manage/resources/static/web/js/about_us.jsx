require('webStyle/about_us.scss');
require('publicJs/pagination');
require('webJsModule/touch_menu');
require('webJsModule/touch_menu');
let echarts = require('echarts');
let paginationElement = $('.pagination');
let leftMenuBox = globalFun.$('#leftMenuBox');
//手机端菜单滑动
var sourceKind = globalFun.parseURL(location.href);
var formatNum = function (num) {
  return formatNumber(num / 10000 ,2);
};

(function(){
    if (location.pathname !== '/about/operational') {
        let browser = $(window).width();
        if(browser<1000) {
            $('.subMenu_show').hide();
            $('.left-nav').find('.text_icon').hide();
            $('.menuItem.active').addClass('ipad_styles');
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
if($('#basicList').length) {
    let noticeTpl=$('#noticeListTemplate').html();
    let ListRender = _.template(noticeTpl);
    let requestData={"index":1,"pageSize":10,"subSection":"BASIC_KNOWLEDGE"};
    paginationElement.loadPagination(requestData, function (data) {
        let html = ListRender(data);
        $('#basicList').html(html);
        $('#basicList').find('span').each(function(key,option) {
            var getTime=$(option).text();
            $(option).text(getTime.substr(0,10));
        });
        if(/app/gi.test(location.search)) {
            $('#basicList').find('li a').each(function(key,option) {
                var thisURL= $(option).attr('href')+'?source=app';
                $(option).attr('href',thisURL);
            });
        }
    });
}
if($('#knowlegeList').length) {
    let noticeTpl=$('#noticeListTemplate').html();
    let ListRender = _.template(noticeTpl);
    let requestData={"index":1,"pageSize":10,"subSection":"LAW_RULE"};
    paginationElement.loadPagination(requestData, function (data) {
        let html = ListRender(data);
        $('#knowlegeList').html(html);
        $('#knowlegeList').find('span').each(function(key,option) {
            var getTime=$(option).text();
            $(option).text(getTime.substr(0,10));
        });
        if(/app/gi.test(location.search)) {
            $('#knowlegeList').find('li a').each(function(key,option) {
                var thisURL= $(option).attr('href')+'?source=app';
                $(option).attr('href',thisURL);
            });
        }
    });
}
if($('#investorList').length) {
    let noticeTpl=$('#noticeListTemplate').html();
    let ListRender = _.template(noticeTpl);
    let requestData={"index":1,"pageSize":10,"subSection":"INVESTOR_EDUCATION"};
    paginationElement.loadPagination(requestData, function (data) {
        let html = ListRender(data);
        $('#investorList').html(html);
        $('#investorList').find('span').each(function(key,option) {
            var getTime=$(option).text();
            $(option).text(getTime.substr(0,10));
        });
        if(/app/gi.test(location.search)) {
            $('#investorList').find('li a').each(function(key,option) {
                var thisURL= $(option).attr('href')+'?source=app';
                $(option).attr('href',thisURL);
            });
        }
    });
}
if($noticeDetail.length){
    var title = $('#knowledgeTitleH2').text();
    switch (sourceKind.params.subSection) {
        case 'base':
            $('#knowledgeTitle').text('基础知识');
            $('.left-nav').find('li').eq(2).find('a').addClass('active').siblings().removeClass('active');
            break;
        case 'law':
            $('#knowledgeTitle').text('法律法规');
            $('.left-nav').find('li').eq(0).find('a').addClass('active').siblings().removeClass('active');
            break;
        case 'investor':
            $('#knowledgeTitle').text('出借人教育');
            $('.left-nav').find('li').eq(1).find('a').addClass('active').siblings().removeClass('active');
            break;
        default :
            $('#knowledgeTitle').text('法律法规');
            $('.left-nav').find('li').eq(0).find('a').addClass('active').siblings().removeClass('active');



    }
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
    },
    '3':{
        small:require('../images/sign/aboutus/aptitude_three_small.jpg'),
        big:require('../images/sign/aboutus/aptitude_three_big.jpg')
    }
};
$organizationalImg.find('li').each(function(key,option) {
    let num = key+1;
    $(option).find('a').attr('href',organizationalImg[num].big);
    $(option).find('a').append(`<img src="${organizationalImg[num].small}">`);
});
//法人承诺书
let $corporateUndertakingImg = $('#corporateUndertaking');

let corporateUndertakingImg={
    '1': {
        small:require('../images/sign/aboutus/corporate_undertaking_one_big.jpeg'),
        big:require('../images/sign/aboutus/corporate_undertaking_one_big.jpeg')
    }
};

$corporateUndertakingImg.find('a').attr('href',corporateUndertakingImg[1].big);
$corporateUndertakingImg.find('a').append(`<img src="${corporateUndertakingImg[1].small}">`);

//审计报告
let $reportImg = $('.report-con');
let $report2017 = $('.photo2017'),
    $report2016 = $('.photo2016'),
    $report2015 = $('.photo2015');

let reportImg={
    '1': {
        small:require('../images/sign/aboutus/report_2017_1_small.png'),
        big:require('../images/sign/aboutus/report_2017_1_big.png')
    },
    '2':{
        small:require('../images/sign/aboutus/report_2017_2_small.png'),
        big:require('../images/sign/aboutus/report_2017_2_big.png')
    },
    '3': {
        small:require('../images/sign/aboutus/report_2016_1_small.png'),
        big:require('../images/sign/aboutus/report_2016_1_big.png')
    },
    '4':{
        small:require('../images/sign/aboutus/report_2015_1_small.png'),
        big:require('../images/sign/aboutus/report_2015_1_big.png')
    }
};

$reportImg.find('li').each(function(key,option) {
    let num = key+1;
    $(option).find('a').attr('href',reportImg[num].big);
    $(option).find('a').append(`<img src="${reportImg[num].small}">`);
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
fancybox(function() {
    $("#corporateUndertaking li a").fancybox({
        'titlePosition' : 'over',
        'cyclic'        : false,
        'showCloseButton':true,
        'showNavArrows' : true,
        'titleFormat'   : function(title, currentArray, currentIndex, currentOpts) {
            return '<span id="fancybox-title-over">' + (currentIndex + 1) + ' / ' + currentArray.length + (title.length ? ' &nbsp; ' + title : '') + '</span>';
        }
    });
});
//审计报告放大图

fancybox(function() {
    $(".audit-report-item li a").fancybox({
        'titlePosition' : 'over',
        'cyclic'        : false,
        'showCloseButton':true,
        'showNavArrows' : true,
        "loop":false,
        'titleFormat'   : function(title, currentArray, currentIndex, currentOpts) {
            return '<span id="fancybox-title-over">' + (currentIndex + 1) + ' / ' + currentArray.length + (title.length ? ' &nbsp; ' + title : '') + '</span>';
        }
    });
});

//三个报告切换
let $reportContainer = $('#reportContainer');
let $reportTitle = $('#reportTitle');
$reportTitle.find('em').on('click',function () {
    let _self = $(this);
    let index = _self.index();
    _self.siblings().removeClass('active').end().addClass('active');
    $reportContainer.find('.content').hide().eq(index).show();
})

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

let getPartOnePage = (data, dataStr) => {

    var days = parseInt(dataStr/365);
    dataStr = (dataStr-days*365).toString();
    let dom = '';
    for (let i = 0; i < dataStr.length; i++) {
        dom += `<span class="data-bg">${dataStr.charAt(i)}</span>`
    }
    $('#operationDays').prepend(`<span class="assurance"><span class="margin28">安</span><span class="margin28">全</span><span class="margin28">运</span><span class="margin28">营</span></span>`);
    $('#operationDays').append(`<span class="mid_line pre_line"></span><span class="data-bg">${days}</span><span style="margin-top: 20px">年</span><span class="mid_line last_line"></span>`);
    $('#operationDays').append(dom);
    $('#operationDays').append(`<span style="margin-top: 20px">天</span>`);

     $('#earn_total_amount').html(formatNum(data.totalInterest));//累计为用户赚取
};

function toThousands(num) {
    var num = (num || 0).toString(), result = '';
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) {
        result = num + result;
    }
    return result;
}
function formatNumber(s, n) {  // 金额格式化
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
    let t = "";
    for (let i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
};
function dateFomater(datetime) {
    var dateArr = datetime.substr(0,10).split('-');
    var dom = '';
    dom += dateArr[0]+'年'+dateArr[1]+'月'+dateArr[2]+'日';
    return dom;
}

//运营数据
require.ensure(['publicJs/load_echarts','publicJs/commonFun'],function() {
    let loadEcharts = require('publicJs/load_echarts');
    let commonFun=require('publicJs/commonFun');
    //团队介绍环形图

    if($('.team-introduction').length){
        //年龄分布 环形图
        let ageDistributionArr = [{name:'25岁以下  49%',scale:0.49},{name:'30~40岁   32%',scale:0.32},{name:'26~30岁  19%',scale:0.19}];
        var optionAgeDistribution = loadEcharts.ChartOptionTemplates.AnnularOption(ageDistributionArr,
            {
                tooltip: {
                formatter: '员工年龄'+'<br/>{b} ',
                show: true,
                enterable:true
                },
                color: ['#32cd32', '#da70d6', '#86cffa']
        });
        optionAgeDistribution.series[0].center = ['50%', '40%'];
        var  optAgeDistribution = loadEcharts.ChartConfig('ageDistribution', optionAgeDistribution);
        loadEcharts.RenderChart(optAgeDistribution);
        //学历分布
        let educationalDistributionArr = [{name:'大专及本科  88%',scale:0.88},{name:'硕士及以上  5%',scale:0.05},{name:'其他  7%',scale:0.07}];
        var optionEducationalDistribution = loadEcharts.ChartOptionTemplates.AnnularOption(educationalDistributionArr,
            {
                tooltip: {
                    formatter: '员工学历'+'<br/>{b} ',
                    show: true,
                    enterable:true
                },
                color: ['#32cd32', '#da70d6', '#86cffa']
            });
        optionEducationalDistribution.series[0].center = ['50%', '40%'];
        var  optEducatioEnalDistribution = loadEcharts.ChartConfig('educationalDistribution', optionEducationalDistribution);
        loadEcharts.RenderChart(optEducatioEnalDistribution);

        //技术及风控团队员工学历分布
        let technologyEducationalArr = [{name:'本科  80%',scale:0.8},{name:'硕士及以上  10%',scale:0.1},{name:'专科  10%',scale:0.1}];
        var optionTechnologyEducational = loadEcharts.ChartOptionTemplates.AnnularOption(technologyEducationalArr,
            {
                tooltip: {
                    formatter: '技术及风控团队员工学历'+'<br/>{b} ',
                    show: true,
                    enterable:true
                },
                color: ['#32cd32', '#da70d6', '#86cffa']
            });
        optionTechnologyEducational.series[0].center = ['50%', '40%'];
        var  optTechnologyEducational = loadEcharts.ChartConfig('technologyEducational', optionTechnologyEducational);
        loadEcharts.RenderChart(optTechnologyEducational);

        //技术及风控团队员工工作年限分布
        let technologyWorkingLifeArr = [{name:'3年以下  22%',scale:0.22},{name:'3-5年  39%',scale:0.39},{name:'6-10年    31%',scale:0.31},{name:'10年以上  8%',scale:0.08}];
        var optionTechnologyWorkingLife = loadEcharts.ChartOptionTemplates.AnnularOption(technologyWorkingLifeArr,
            {
                tooltip: {
                    formatter: '技术及风控团队员工工作年限'+'<br/>{b} ',
                    show: true,
                    enterable:true
                },
                color: ['#32cd32', '#da70d6', '#86cffa',"#ff7e50"]
            });
        optionTechnologyWorkingLife.series[0].center = ['50%', '40%'];
        var  optTechnologyWorkingLife = loadEcharts.ChartConfig('technologyWorkingLife', optionTechnologyWorkingLife);
        loadEcharts.RenderChart(optTechnologyWorkingLife);
        //点击前后按钮
        var pageIndex = 0;
        var perWidth = 310*2+20*2;
        var $carouselBox = $('#carouselBox');

        $('.prevBtn').click(function(){
            pageIndex--;
            if(pageIndex <= 0) {
                pageIndex = 0;
            }
            $carouselBox.animate({
                'left':-pageIndex*perWidth
            });
        })
        $('.nextBtn').click(function(){
            pageIndex ++;
            if(pageIndex >= 1) {
                pageIndex = 1;
            }
            $carouselBox.animate({
                'left':-pageIndex*perWidth
            });
        })
    }

    if (!$("#dataRecord").length) {
        return;
    }
    commonFun.useAjax({
        url: '/about/operation-data/chart',
        type: 'GET'
    },function(data) {
        var datetime = data.now;
        var dateTimeDOM = '（数据截止到'+dateFomater(datetime)+'）';
        $('#dateTime').text(dateTimeDOM);

        var month = data.month.slice(-6);
        var money = data.money.slice(-6);
        getPartOnePage(data,data.operationDays);

        // 平台数据总览
        $('#tradeAmount').text(formatNum(data.tradeAmount));//累计交易金额
        $('.sumExpectedAmount').text(formatNum(data.sumExpectedAmount));//待偿金额
        $('#sumExpectedInterestAmount').text(formatNum(data.sumExpectedInterestAmount));//待偿利息金额
        $('#usersCount').text(toThousands(data.usersCount));//注册投资用户数
        $('.sumRepayIngInvestCount').html(toThousands(data.sumRepayIngInvestCount)); // 待偿金额笔数

        // 借款标的情况
        $('#sumLoanAmount').text(formatNum(data.sumLoanAmount));//累计借贷金额
        $('#sumLoanCount').text(toThousands(data.sumLoanCount));//累计借贷笔数

        // 出借人信息
        $('.investUsersCount').text(toThousands(data.investUsersCount));//累计出借用户数
        $('#maxSingleInvestAmountRate').text(formatNumber(data.maxSingleInvestAmountRate*100,2));//最大单户出借余额占比
        $('#avgInvestAmount').text(formatNum(data.avgInvestAmount));//人均累计出借金额
        $('#maxTenInvestAmountRate').text(formatNumber(data.maxTenInvestAmountRate*100,2));//最大十户出借余额占比
        $('#sumNotCompleteInvestorCount').text(toThousands(data.sumNotCompleteInvestorCount));//当前出借人数

        // 借款人信息
        $('.sumLoanerCount').text(toThousands(data.sumLoanerCount));//累计借款人数
        $('#maxSingleLoanAmountRate').text(formatNumber(data.maxSingleLoanAmountRate*100,2));//最大单一借款人待还金额占比
        $('#avgLoanAmount').text(formatNum(data.avgLoanAmount));//人均累计借款金额
        $('#maxTenLoanAmountRate').text(formatNumber(data.maxTenLoanAmountRate*100,2));//前十大借款人待还金额占比
        $('#sumNotCompleteLoanerCount').text(toThousands(data.sumNotCompleteLoanerCount));//当前借款人数
        // $('#sumOverDueAmount').text(formatNum(data.sumOverDueAmount));//逾期金额
        $('#sumOverDueAmount').text(formatNum("5297800.00"));//逾期金额

        // 逾期情况
        // $('#loanerOverDueCount').text(toThousands(data.loanerOverDueCount));//借款人平台逾期次数
        $('#loanerOverDueCount').text(toThousands('11'));//借款人平台逾期次数
        // $('#loanOverDueRate').text(formatNumber(data.loanOverDueRate*100,2));//项目逾期率
        $('#loanOverDueRate').text(formatNumber(0.0121*100,2));//项目逾期率
        // $('#loanerOverDueAmount').text(formatNum(data.loanerOverDueAmount));//平台逾期总金额
        $('#loanerOverDueAmount').text(formatNum('5297800.00'));//平台逾期总金额
        // $('#amountOverDueRate').text(formatNumber(data.amountOverDueRate*100,2));//金额逾期率
        $('#amountOverDueRate').text(formatNumber(0.0357*100,2));//金额逾期率
        // $('#amountOverDueLess90Rate').text(formatNumber(data.amountOverDueLess90Rate*100, 2));//金额逾期率（90天及以内）
        // $('#amountOverDue90To180Rate').text(formatNumber(data.amountOverDue90To180Rate*100, 2));//金额逾期率（90天以上至180天）
        // $('#amountOverDueGreater180Rate').text(formatNumber(data.amountOverDueGreater180Rate*100, 2));//金额逾期率（181天及以上）
        // $('#loanOverDueLess90Rate').text(formatNumber(data.loanOverDueLess90Rate*100, 2));//项目逾期率（90天及以内）
        // $('#loanOverDue90To180Rate').text(formatNumber(data.loanOverDue90To180Rate*100, 2));//项目逾期率（90天以上至180天）
        // $('#loanOverDueGreater180Rate').text(formatNumber(data.loanOverDueGreater180Rate*100, 2));//项目逾期率（181天及以上）

        $('#amountOverDueLess90Rate').text(formatNumber(0.0357*100, 2));//金额逾期率（90天及以内）
        $('#amountOverDue90To180Rate').text(formatNumber(0.0000*100, 2));//金额逾期率（90天以上至180天）
        $('#amountOverDueGreater180Rate').text(formatNumber(0.0000*100, 2));//金额逾期率（181天及以上）
        $('#loanOverDueLess90Rate').text(formatNumber(0.0121*100, 2));//项目逾期率（90天及以内）
        $('#loanOverDue90To180Rate').text(formatNumber(0.0000*100, 2));//项目逾期率（90天以上至180天）
        $('#loanOverDueGreater180Rate').text(formatNumber(0.0000*100, 2));//项目逾期率（181天及以上）


        let barChartArr = [];
        let num = 0;
        for (let i = 0; i < 4; i++) {
            let $item = $('#investItem' + i);
            let amount = parseInt(Math.ceil($item.data('amount') / 10000));
            barChartArr.push(amount);
            let count = Number($item.data('count')) || 0;
            num += count;
        }
        $('.total_trade_count').html(toThousands(num));//累计投资笔数
        var dataJson = {

                sub:'金额（元）',
                name:'运营数据',
                month:month,
                money:money
            },
            option = loadEcharts.ChartOptionTemplates.BarOption(dataJson);
        option.series[0].barWidth = 50;

          var  opt = loadEcharts.ChartConfig('dataRecord', option);
        loadEcharts.RenderChart(opt);
        //投资人基本信息 环形图
         var optionUser = loadEcharts.ChartOptionTemplates.AnnularOption(data.ageDistribution,{},'投资用户(人)');
        var  optUser = loadEcharts.ChartConfig('investRecord', optionUser);
        loadEcharts.RenderChart(optUser);
        //投资人男女比例 饼状图
        var sexOptions = [{name:'男性投资人',scale:data.maleScale},{name:'女性投资人',scale:data.femaleScale}];
        var optionSex = loadEcharts.ChartOptionTemplates.PieOptionBaseInfo(sexOptions);
        var  optSex = loadEcharts.ChartConfig('investSexRecord', optionSex);
        loadEcharts.RenderChart(optSex);


        let investCityScaleTop5 = data.investCityScaleTop5; // 投资人地域分布top5
        let loanerCityScaleTop5 = data.loanerCityScaleTop5; // 借款人地域分布top5
        //投资人地域分布
        investCityScaleTop5.forEach((item, index) => {
            $('#geographicalWrap').append(`<li class="clearfix"><div class="fl city-name marginLeft10">${item.city}</div> <div class="percent fl marginLeft10"><span style="width: ${item.scale}%;"></span></div><div class="fl marginLeft10">${item.scale}%</div></li>`);
        });
        //借款人地域分布top5
        loanerCityScaleTop5.forEach((item, index) => {
            $('#geographicalWrapLoan').append(`<li class="clearfix"><div class="fl city-name marginLeft10">${item.city}</div> <div class="percent fl marginLeft10"><span style="width: ${item.scale}%;"></span></div><div class="fl marginLeft10">${item.scale}%</div></li>`);
        });
        calculateWidth($('#geographicalWrap'),'.city-name');
        calculateWidth($('#geographicalWrapLoan'),'.city-name');

        //借款人基本信息环形图
        var optionLoan = loadEcharts.ChartOptionTemplates.AnnularOption(data.loanerAgeDistribution,{},'借款用户(人)');
        var  optLoan = loadEcharts.ChartConfig('loanBaseRecord', optionLoan);
        loadEcharts.RenderChart(optLoan);
        //借款人男女比例 饼状图
        var sexLoanOptions = [{name:'男性借款人',scale:data.loanerMaleScale},{name:'女性借款人',scale:data.loanerFemaleScale}];
        var optionSexLoan = loadEcharts.ChartOptionTemplates.PieOptionBaseInfo(sexLoanOptions,'借款人基本信息');
        var  optSexLoan = loadEcharts.ChartConfig('loanBaseSexRecord', optionSexLoan);
        loadEcharts.RenderChart(optSexLoan);
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

function calculateWidth(dom,className) {
    let widthArr = [];
    dom.find(className).each(function (index,item) {
        widthArr.push($(item).width());
        widthArr.sort(function (a,b) {
            return a-b;
        })

    })
    $(dom).find(className).width(widthArr[widthArr.length-1]).css('marginRight','10px');
}
//控股名单
$('#nameList').click(function () {
    layer.open({
        type: 1,
        title:'实际控制人与持股5%以上的股东名单',
        closeBtn: 1,
        area: ['600px', '280px'],
        shadeClose: false,
        content: $('#nameListPop')
    });
});
//用户注册协议
$('#userAgreement').click(function () {
    layer.open({
        type:1,
        title:'速贷用户注册协议样本',
        area:$(window).width()>700?['800px','520px']:['320px','100%'],
        shadeClose: false,
        scrollbar: true,
        skin:'register-skin',
        content: $('.user-register')
    });
});
