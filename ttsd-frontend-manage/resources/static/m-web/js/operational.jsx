require('mWebStyle/operational.scss');

import Swiper from 'swiper';

let echarts = require('echarts');

let commonFun=require('publicJs/commonFun');

(function (doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 20 * (clientWidth /375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

let myDate = new Date();

let currentMonth = myDate.getMonth() + 1;
let currentDay = myDate.getDate();

currentMonth = currentMonth < 10 ? '0' + currentMonth : currentMonth;
currentDay = currentDay < 10 ? '0' + currentDay : currentDay;

$('.currentMonth').html(currentMonth);
$('.currentDay').html(currentDay);

console.log(currentMonth)
console.log(currentDay)

let mySwiper = new Swiper('.swiper-container', {
    direction : 'vertical',
    width: window.screen.width,
    height: document.body.clientHeight - 44,
});


$('#goBack_experienceAmount').on('click',() => {
    history.go(-1);
});


$('.side_to_page').click(function(e){
    let index = Number(e.currentTarget.dataset.index);
    index = index == 4 ? 1 : index;
    mySwiper.slideTo(index, 500, false);//切换到第一个slide，速度为1秒
});

let myChart1 = echarts.init(document.getElementById('main_part1'));
// 绘制图表
myChart1.setOption({
    title: {
        text: '',
    },
    tooltip: {
        show: false
    },
    xAxis: {
        type: 'category',
        name: '(标的)',
        nameGap: 5,
        nameTextStyle: {
          color: '#333'
        },
        legendHoverLink: false,
        data: ['30天', '60天', '90天', '360天'],
        axisTick: {
            show: false,
        },
        axisLine: {
            lineStyle: {
                color: '#ccc'
            }
        },
        axisLabel: {
            color: '#333'
        }
    },
    yAxis: {
        show: false
    },
    series: [{
        name: '销量',
        type: 'bar',
        barWidth: '30%',
        data: [3859, 13014, 4275, 6176],
        itemStyle: {
            normal: {
                color: function (params){
                    let colorList = ['#1691e8','#74bbf3','#7cd8ef','#28c37c'];
                    return colorList[params.dataIndex];
                }
            }
        }
    }],
    label: {
        normal: {
            show: true,
            position: 'top',

        }
    }
});

let myChart2 = echarts.init(document.getElementById('main_part2'));
// 绘制图表

function getChartData(data) {
    return {
        title: {
            text: '',
        },
        tooltip: {
            show: false
        },
        xAxis: {
            type: 'category',
            nameGap: 5,
            data: ['7月', '8月', '9月', '10月','11月','12月'],
            axisTick: {
                show: false,
            },
            axisLine: {
                lineStyle: {
                    color: '#ccc'
                }
            },
            axisLabel: {
                color: '#333'
            }
        },
        yAxis: {
            show: false
        },
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
            data: [3859, 13014, 4275, 6176, 5000, 3000],
        }],
    }
}


commonFun.useAjax({
    url: '/about/operation-data/chart',
    type: 'GET'
},(data) => {
    myChart2.setOption(getChartData(data));
}, () => {
    let data = {"operationDays":922,"usersCount":522,"totalInterest":"108992411","maleScale":null,"femaleScale":null,"month":["2015.7","2015.8","2015.9","2015.10","2015.11","2015.12","2016.1","2016.2","2016.3","2016.4","2016.5","2016.6","2016.7","2016.8","2016.9","2016.10","2016.11","2016.12","2017.1","2017.2","2017.3","2017.4","2017.5","2017.6","2017.7","2017.8","2017.9","2017.10","2017.11","2017.12"],"money":["0.00","0.00","0.00","0.00","246.00","828.00","718.00","354.00","139.00","156.00","398.00","513.68","56.11","1066.97","1056.37","2000012.51","48.34","133.50","620411.32","21.00","172.44","100033.00","301018.00","200.00","888017.00","9640025.00","4491098.00","20204064.00","17561894.00","0.00"],"ageDistribution":[{"name":"20岁以下","scale":"2.3"},{"name":"20~30岁","scale":"37.5"},{"name":"30~40岁","scale":"42.0"},{"name":"40~50岁","scale":"15.9"},{"name":"50岁以上","scale":"2.3"}],"investCityScaleTop3":[{"city":"北京","scale":"52.2"},{"city":"南宁","scale":"31.1"},{"city":"成都","scale":"5.6"}],"investAmountScaleTop3":[{"city":"北京","scale":"65.0"},{"city":"南宁","scale":"12.7"},{"city":"成都","scale":"8.6"}],"tradeAmount":"55812680.24"}
    myChart2.setOption(getChartData(data));
    let dataStr = data.operationDays.toString();
    let dom = '';
    for (let i = 0;i < dataStr.length;i++) {
        dom += `<div class="safe_day">${dataStr.charAt(i)}</div>`
    }
    $('.safe_day_wrapper').prepend(dom);
    $('#grand_total_amount').html(fmoney(data.tradeAmount,2));
    $('#earn_total_amount').html(fmoney(data.totalInterest,2));
});


function fmoney(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
    let t = "";
    for (let i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}