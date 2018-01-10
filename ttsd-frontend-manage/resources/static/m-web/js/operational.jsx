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

let mySwiper = new Swiper('.swiper-container', {
    direction : 'vertical',
    width: window.screen.width,
    height: document.body.clientHeight - 48,
});

$('#goBack_experienceAmount').on('click',() => {
    history.go(-1);
});

$('.side_to_page').click(function(e){
    let index = Number(e.currentTarget.dataset.index);
    index = index == 5 ? index - 2 : index;
    mySwiper.slideTo(index, 500, false);//切换到第一个slide，速度为1秒
});

let myChart1 = echarts.init(document.getElementById('main_part1'));
let myChart2 = echarts.init(document.getElementById('main_part2'));
let myChart3 = echarts.init(document.getElementById('main_part3'));
let myChart4 = echarts.init(document.getElementById('main_part4'));
let myChart5 = echarts.init(document.getElementById('main_part5'));
let myChart6 = echarts.init(document.getElementById('main_part6'));

let drawBarChart = (data) => {  // 柱状图
    return {
        xAxis: {
            type: 'category',
            name: '(标的)',
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
            barWidth: '30%',
            data: data,
            itemStyle: {
                normal: {
                    color: function (params){
                        let colorList = ['#1691e8','#74bbf3','#7cd8ef','#28c37c'];
                        return colorList[params.dataIndex];
                    }
                }
            },
        }],
        label: {
            normal: {
                formatter:(a) => {
                    var result = [ ], counter = 0;
                    var num_array = a.data.toFixed(2).split('.');
                    var num = num_array[0];
                    var newStr = '';
                    for (var i = num.length - 1; i >= 0; i--) {
                        counter++;
                        result.unshift(num[i]);
                        if (!(counter % 3) && i != 0) { result.unshift(','); }
                    }
                    return result.join('');
                    if(num_array.length>1){
                        newStr = result.join('');
                        newStr += '.'+num_array[1];
                        return newStr;
                    }else{
                        return result.join('');
                    }
                },
                show: true,
                position: 'top'
            }
        }
    }
};

let drawLineChart = (data,monthArr) => {  // 折线图
    return {
        xAxis: {
            type: 'category',
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
                formatter:(a) => {
                    var result = [ ], counter = 0;
                    var num_array = a.data.toFixed(2).split('.');
                    var num = num_array[0];
                    var newStr = '';
                    for (var i = num.length - 1; i >= 0; i--) {
                        counter++;
                        result.unshift(num[i]);
                        if (!(counter % 3) && i != 0) { result.unshift(','); }
                    }
                    return result.join('');
                    if(num_array.length>1){
                        newStr = result.join('');
                        newStr += '.'+num_array[1];
                        return newStr;
                    }else{
                        return result.join('');
                    }
                },
                show: true,
                position: 'top'
            }
        }
    }
};

let circularChart = (data,legendData,color,rightPosition) => { // 环形图
    return {
        legend: {
            orient: 'vertical',
            selectedMode:false,
            right: rightPosition,
            y: 'middle',
            data:legendData
        },
        series: [
            {
                type:'pie',
                radius: ['40%', '70%'],
                center: ['30%','50%'],
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

let drawBarTransverse = (cityName,cityData,colorArr) => { // 横向柱状图
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
            itemStyle: {
                normal: {
                    color: function (params){
                        let colorList = colorArr;
                        return colorList[params.dataIndex];
                    }
                }
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
},(data) => {
    let dataStr = data.operationDays.toString();
    getPartOnePage(data,dataStr);
    getPartTwoPage(data);
    getPartThreePage(data);
    getPartFourPage(data);

}, () => {
    let data = {"operationDays":922,"usersCount":11522,"totalInterest":"108992411","maleScale":"0.7","femaleScale":"0.3","month":["2015.7","2015.8","2015.9","2015.10","2015.11","2015.12","2016.1","2016.2","2016.3","2016.4","2016.5","2016.6","2016.7","2016.8","2016.9","2016.10","2016.11","2016.12","2017.1","2017.2","2017.3","2017.4","2017.5","2017.6","2017.7","2017.8","2017.9","2017.10","2017.11","2017.12"],"money":["0.00","0.00","0.00","0.00","246.00","828.00","718.00","354.00","139.00","156.00","398.00","513.68","56.11","1066.97","1056.37","2000012.51","48.34","133.50","620411.32","21.00","172.44","100033.00","301018.00","200.00","888017.00","9640025.00","4491098.00","20204064.00","17561894.00","0.00"],"ageDistribution":[{"name":"20岁以下","scale":"2.3"},{"name":"20~30岁","scale":"37.5"},{"name":"30~40岁","scale":"42.0"},{"name":"40~50岁","scale":"15.9"},{"name":"50岁以上","scale":"2.3"}],"investCityScaleTop3":[{"city":"北京","scale":"52.2"},{"city":"南宁","scale":"31.1"},{"city":"成都","scale":"5.6"}],"investAmountScaleTop3":[{"city":"北京","scale":"0"},{"city":"南宁","scale":"0"},{"city":"成都","scale":"100"}],"tradeAmount":"55812680.24"}
    let dataStr = data.operationDays.toString();
    getPartOnePage(data,dataStr);
    getPartTwoPage(data);
    getPartThreePage(data);
    getPartFourPage(data);
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
    if (num) { result = num + result; }
    return result;
}

let getPartOnePage = (data,dataStr) => {
    let dom = '';
    for (let i = 0;i < dataStr.length;i++) {
        dom += `<div class="safe_day">${dataStr.charAt(i)}</div>`
    }
    $('.safe_day_wrapper').prepend(dom);
    $('#grand_total_amount').html(formatNumber(data.tradeAmount,2));
    $('#earn_total_amount').html(formatNumber(data.totalInterest,2));

    let myDate = new Date();
    let currentMonth = myDate.getMonth() + 1;
    let currentDay = myDate.getDate();
    currentMonth = currentMonth < 10 ? '0' + currentMonth : currentMonth;
    currentDay = currentDay < 10 ? '0' + currentDay : currentDay;
    $('.currentMonth').html(currentMonth);
    $('.currentDay').html(currentDay);
};

let getPartTwoPage = (data) => {
    let barChartArr = [];
    let num = 0;
    for (let i = 0;i < 4;i++) {
        let $item = $('#investItem' + i);
        let amount = parseInt($item.data('amount') / 10000);
        barChartArr.push(amount);
        let count = Number($item.data('count')) || 0;
        num += count;
    }
    $('#total_trade_count').html(toThousands(num));
    myChart1.setOption(drawBarChart(barChartArr));
    let monthArr = data.month.slice(-6).map(item => {
        return item.split('.')[1] + '月';
    });
    let amountArr = data.money.slice(-6).map(item => {
        return parseInt(item/10000);
    });
    myChart2.setOption(drawLineChart(amountArr,monthArr));
};

let getPartThreePage = (data) => {
    let ageArr = [];
    let ageLegendArr = [];
    let maleScale = data.maleScale;
    let femaleScale = data.femaleScale;
    let ageDistribution = data.ageDistribution;
    $('#total_trade_person').html(toThousands(data.usersCount));
    for (let i = 0;i < ageDistribution.length;i++) {
        let item = ageDistribution[i];
        ageArr[i] = {};
        ageArr[i].value = item.scale;
        ageArr[i].name = item.name + ' ' + item.scale + '%';
        ageLegendArr[i] = item.name + ' ' + item.scale + '%';
    }
    myChart3.setOption(circularChart([
        {value: `${femaleScale}`, name:`女性 ${femaleScale * 100}%`},
        {value: `${maleScale}`, name:`男性 ${maleScale * 100}%`},
    ],[`男性 ${maleScale * 100}%`,`女性 ${femaleScale * 100}%`],['#fdb560','#74bbf3'],'18%'));

    myChart4.setOption(circularChart(ageArr,ageLegendArr,['#a47cf3','#fdb560','#fcee74','#87e376','#69e2ab'],'10%'));
};

let getPartFourPage = (data) => {
    let investCityScaleTop3 = data.investCityScaleTop3; // 投资人数top3
    let investAmountScaleTop3 = data.investAmountScaleTop3; // 投资金额top3
    let cityName_count = [];
    let cityData_count = [];
    let cityName_amount = [];
    let cityData_amount = [];
    investCityScaleTop3.forEach((item,index) => {
        cityName_count[index] = item.city;
        cityData_count[index] = item.scale;
    });
    investAmountScaleTop3.forEach((item,index) => {
        cityName_amount[index] = item.city;
        cityData_amount[index] = item.scale;
    });
    myChart5.setOption(drawBarTransverse(cityName_count,cityData_count,['#c2eef2','#81e9f2','#00def2']));
    myChart6.setOption(drawBarTransverse(cityName_amount,cityData_amount, ['#ffecac','#ffd74f','#ffc601']));
};
