require('mWebStyle/operational.scss');
import Swiper from 'swiper';
let echarts = require('echarts');
let commonFun = require('publicJs/commonFun');
let ifReflow = false;

(function (doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 20 * (clientWidth / 375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

let mySwiper = new Swiper('.swiper-container', {
    direction: 'vertical',
    nextButton: '.button_under_arrow',
    width: window.screen.width,
    height: document.body.clientHeight - 44,
    onTouchMove: function () {
        if (!ifReflow) {
            reflow();
        }
    }
});

document.getElementsByClassName('swiper-container')[0].style.height = document.body.clientHeight - 44 + 'PX';

$('#goBack_experienceAmount').on('click', () => {
    location.href='/m';
});

$('.side_to_page').click(function (e) {
    if (!ifReflow) {
        reflow();
    }
    let index = Number(e.currentTarget.dataset.index);
    index = index == 8 ? index-2 : index;
    mySwiper.slideTo(index, 500, false);//切换到第一个slide，速度为1秒
});

let myChart2 = echarts.init(document.getElementById('main_part2'));
let myChart3 = echarts.init(document.getElementById('main_part3'));
let myChart4 = echarts.init(document.getElementById('main_part4'));
let myChart6 = echarts.init(document.getElementById('main_part6'));
let myChart7 = echarts.init(document.getElementById('main_part7'));

let drawBarChart = (data) => {  // 柱状图
    return {
        xAxis: {
            type: 'category',
            name: '标的',
            nameGap: 5,
            nameTextStyle: {color: '#333'},
            legendHoverLink: false,
            data: ['30天', '60天', '90天', '360天'],
            axisTick: {show: false,},
            axisLine: {
                lineStyle: {color: '#ccc'}
            },
            axisLabel: {color: '#333'}
        },
        yAxis: {show: false},
        series: [{
            type: 'bar',
            silent: true,
            barWidth: '30%',
            data: data,
            itemStyle: {
                normal: {
                    color: function (params) {
                        let colorList = ['#1691e8', '#74bbf3', '#7cd8ef', '#28c37c'];
                        return colorList[params.dataIndex];
                    }
                }
            },
        }],
        label: {
            normal: {
                formatter: (a) => {
                    var result = [], counter = 0;
                    var num_array = a.data.toFixed(2).split('.');
                    var num = num_array[0];
                    var newStr = '';
                    for (var i = num.length - 1; i >= 0; i--) {
                        counter++;
                        result.unshift(num[i]);
                        if (!(counter % 3) && i != 0) {
                            result.unshift(',');
                        }
                    }
                    return result.join('');
                    if (num_array.length > 1) {
                        newStr = result.join('');
                        newStr += '.' + num_array[1];
                        return newStr;
                    } else {
                        return result.join('');
                    }
                },
                show: true,
                position: 'top'
            }
        }
    }
};

let drawLineChart = (data, monthArr) => {  // 折线图
    return {
        xAxis: {
            type: 'category',
            silent: true,
            nameGap: 5,
            data: monthArr,
            axisTick: {show: false},
            axisLine: {
                lineStyle: {color: '#ccc'}
            },
            axisLabel: {color: '#333'}
        },
        yAxis: {show: false},
        series: [{
            type: 'line',
            hoverAnimation: false,
            itemStyle: {
                normal: {
                    label: {
                        show: true
                    },
                    color: '#019bd9',
                }
            },
            data: data,
        }],
        label: {
            normal: {
                formatter: (a) => {
                    var result = [], counter = 0;
                    var num_array = a.data.toFixed(2).split('.');
                    var num = num_array[0];
                    var newStr = '';
                    for (var i = num.length - 1; i >= 0; i--) {
                        counter++;
                        result.unshift(num[i]);
                        if (!(counter % 3) && i != 0) {
                            result.unshift(',');
                        }
                    }
                    return result.join('');
                    if (num_array.length > 1) {
                        newStr = result.join('');
                        newStr += '.' + num_array[1];
                        return newStr;
                    } else {
                        return result.join('');
                    }
                },
                show: true,
                position: 'top'
            }
        }
    }
};

let circularChart = (data,legendData, color) => { // 环形图
    return {
        legend: {
            orient: 'vertical',
            selectedMode: false,
            left: '10%',
            y: 'middle',
            data: legendData
        },
        series: [
            {
                type: 'pie',
                silent: true,
                radius: ['40%', '70%'],
                center: ['70%', '50%'],
                legendHoverLink: false,
                hoverAnimation: false,
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    }
                },
                color: color,
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data: data
            }
        ]
    }
};
let pieChart = (data,legendData, color) => { // 饼形图
    return {
        legend: {
            orient: 'vertical',
            selectedMode: false,
            left: '10%',
            y: 'middle',
            data: legendData
        },
        series: [
            {
                type: 'pie',
                silent: true,
                radius : '65%',
                center: ['70%', '50%'],
                legendHoverLink: false,
                hoverAnimation: false,
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    }
                },
                color: color,
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data: data
            }
        ]
    }
};
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

commonFun.useAjax({  // 拉取页面数据
    url: '/about/operation-data/chart',
    type: 'GET'
}, (data) => {
    console.log(data);
    let dataStr = data.operationDays.toString();
    getPartOnePage(data, dataStr);
    getPartTwoPage(data);
    getPartThreePage(data);
    getPartFourPage(data);
    getPartFivePage(data);

}, () => {
});

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

let getPartOnePage = (data, dataStr) => {

    var days = parseInt(dataStr/365);
    var daysStr = days.toString();
    let domDays = '';
    for (let i = 0; i < daysStr.length; i++) {
        domDays += `<div class="safe_day">${daysStr.charAt(i)}</div>`
    }
    domDays+=`<div class="safe_day_unit">年</div>>`;
    $('.safe_day_wrapper').append(domDays);
    dataStr = (dataStr-days*365).toString();
    let dom = '';
    for (let i = 0; i < dataStr.length; i++) {
        dom += `<div class="safe_day">${dataStr.charAt(i)}</div>`
    }
    dom+=`<span class="safe_day_unit">天</span>`;
    $('.safe_day_wrapper').append(dom);
    $('#grand_total_amount').html(formatNumber(data.tradeAmount, 2));
    $('#earn_total_amount').html(formatNumber(data.totalInterest / 100, 2));
};

let getPartTwoPage = (data) => {
    let barChartArr = [];
    let num = 0;
    for (let i = 0; i < 4; i++) {
        let $item = $('#investItem' + i);
        let amount = parseInt(Math.ceil($item.data('amount') / 10000));
        barChartArr.push(amount);
        let count = Number($item.data('count')) || 0;
        num += count;
    }
    $('#total_trade_count').html(toThousands(num));

    let monthArr = data.month.slice(-6).map(item => {
        return item.split('.')[1] + '月';
    });
    let amountArr = data.money.slice(-6).map(item => {
        return parseInt(Math.ceil(item / 10000));
    });
    myChart2.setOption(drawLineChart(amountArr, monthArr));
};

let getPartThreePage = (data) => {
    let ageArr = [];
    let ageLegendArr = [];
    let ageLoanArr = [];
    let ageLoanLegendArr = [];
    let maleScale = data.maleScale;//投资人男性
    let femaleScale = data.femaleScale;//投资人女性
    let loanerMaleScale = data.loanerMaleScale.toFixed(1);//借款人男性
    let loanerFemaleScale = data.loanerFemaleScale;//借款人女性
    let ageDistribution = data.ageDistribution;
    var loanerAgeDistribution = data.loanerAgeDistribution;
    $('#total_trade_person').html(toThousands(data.usersCount));
    for (let i = 0; i < ageDistribution.length; i++) {
        let item = ageDistribution[i];
        ageArr[i] = {};
        ageArr[i].value = item.scale;
        ageArr[i].name = item.name + ' ' + item.scale + '%';
        ageLegendArr[i] = item.name + ' ' + item.scale + '%';
    }
    for (let i = 0; i < loanerAgeDistribution.length; i++) {
        let item = loanerAgeDistribution[i];
        ageLoanArr[i] = {};
        ageLoanArr[i].value = item.scale;
        ageLoanArr[i].name = item.name + ' ' + item.scale + '%';
        ageLoanLegendArr[i] = item.name + ' ' + item.scale + '%';
    }
    myChart3.setOption(pieChart([
        {value: `${maleScale}`, name: `男性出借人 ${maleScale }%`},
        {value: `${femaleScale}`, name: `女性出借人 ${femaleScale }%`}
    ], [`男性出借人 ${maleScale }%`, `女性出借人 ${femaleScale }%`], ['#84a2ff', '#ff6ecb']));


    myChart4.setOption(circularChart(ageArr, ageLegendArr, ['#ff7e50', '#86cffa', '#da70d6', '#32cd32']));
    myChart6.setOption(pieChart([
        {value: `${loanerMaleScale}`, name: `男性借款人 ${loanerMaleScale }%`},
        {value: `${loanerFemaleScale}`, name: `女性借款人 ${loanerFemaleScale }%`}
    ], [`男性借款人 ${loanerMaleScale }%`, `女性借款人 ${loanerFemaleScale }%`], ['#84a2ff', '#ff6ecb']));


    myChart7.setOption(circularChart(ageLoanArr, ageLoanLegendArr, ['#ff7e50', '#86cffa', '#da70d6', '#32cd32']));
};

let getPartFourPage = (data) => {
     let investCityScaleTop5 = data.investCityScaleTop5; // 投资人数top5
    investCityScaleTop5.forEach((item, index) => {
        $('#geographicalWrap').append(`<li class="clearfix"><div class="fl">${item.city}</div> <div class="fr">${item.scale}%</div><div class="percent"><span style="width: ${item.scale}%;"></span></div></li>`);
    });
};
let getPartFivePage = (data) => {
    let loanerCityScaleTop5 = data.loanerCityScaleTop5; // 投资人数top5
    loanerCityScaleTop5.forEach((item, index) => {
        $('#geographicalWrapLoan').append(`<li class="clearfix"><div class="fl">${item.city}</div> <div class="fr">${item.scale}%</div><div class="percent"><span style="width: ${item.scale}%;"></span></div></li>`);
    });
};

function reflow() {
    document.getElementById('main_part2').style.visibility = "visible";
    document.getElementById('main_part3').style.visibility = "visible";
    document.getElementById('main_part4').style.visibility = "visible";
    document.getElementById('main_part5').style.visibility = "visible";
    document.getElementById('main_part6').style.visibility = "visible";
    document.getElementById('main_part7').style.visibility = "visible";
    ifReflow = true;
}
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