require('mWebStyle/operational.scss');

import Swiper from 'swiper';

let echarts = require('echarts');

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
    height: document.body.clientHeight - 44,
});


$('#goBack_experienceAmount').on('click',() => {
    history.go(-1);
});


$('.side_to_page').click(function(e){
    let index = e.currentTarget.dataset.index;
    index = index == 4 ? 0 : index;
    mySwiper.slideTo(index, 500, false);//切换到第一个slide，速度为1秒
});

// 基于准备好的dom，初始化echarts实例
let myChart1 = echarts.init(document.getElementById('main'));
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
        lineHeight: '1rem',
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
            interval: 0,

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

// let myChart2 = echarts.init(document.getElementById('main_part2'));
// // 绘制图表
// myChart2.setOption({
//     title: {
//         text: '',
//     },
//     tooltip: {
//         show: false
//     },
//     xAxis: {
//         type: 'category',
//         name: '(标的)',
//         nameGap: 5,
//         lineHeight: '1rem',
//         data: ['30天', '60天', '90天', '360天'],
//         axisTick: {
//             show: false,
//         },
//         axisLine: {
//             lineStyle: {
//                 color: '#ccc'
//             }
//         },
//         axisLabel: {
//             interval: 0,
//
//         }
//     },
//     yAxis: {
//         show: false
//     },
//     series: [{
//         name: '销量',
//         type: 'bar',
//         barWidth: '30%',
//         data: [3859, 13014, 4275, 6176],
//         itemStyle: {
//             normal: {
//                 color: function (params){
//                     let colorList = ['#1691e8','#74bbf3','#7cd8ef','#28c37c'];
//                     return colorList[params.dataIndex];
//                 }
//             }
//         }
//     }],
//     label: {
//         normal: {
//             show: true,
//             position: 'top',
//
//         }
//     }
// });


