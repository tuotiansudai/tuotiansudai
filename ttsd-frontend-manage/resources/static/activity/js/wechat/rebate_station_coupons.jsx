require('activityStyle/wechat/rebate_station_coupons.scss');
let commonFun = require('publicJs/commonFun');
let sourceKind = globalFun.parseURL(location.href);
commonFun.calculationFun(document,window);
let Cycle = commonFun.Cycle;

setTimeout(function () {
    let fontSize = $('html').css('fontSize');
    let opts2 = {
        id: "cycle",
        width: 24 * parseInt(fontSize),
        height: 24 * parseInt(fontSize),
        percent: "66.4",
        border: parseInt(fontSize) / 2,
        bgColor: "rgba(187,185,176,.7)",
        barColor: "#e7051c",
        fillColor: "rgba(251,233,197,.47)"
    };
    new Cycle(opts2).init();
    $('canvas').css('width',12 * parseInt(fontSize) + 'px');
    $('canvas').css('height',12 * parseInt(fontSize) + 'px');
    function getPercentLight() {
        let currentPer = 1000;
        let percentArr= [100,200,300,400,500,900];
        for (let i = 0;i < percentArr.length;i++) {
            let item = percentArr[i];
            if (currentPer < item) {
                let index = percentArr.indexOf(item) - 1;
                // $('.light_line').css('width',16.6 * index + '%');
                for(let j = 1; j < index + 2;j++) {
                    let lightCircle = 'percent' + j;
                    $('.' + lightCircle).addClass('light');
                }
                return;
            }
            else {
                if (currentPer > percentArr[percentArr.length - 1]) {
                    // $('.light_line').css('width','100%');
                    for(let j = 1; j < 7;j++) {
                        let lightCircle = 'percent' + j;
                        $('.' + lightCircle).addClass('light');
                    }
                }
            }
        }
    }

    getPercentLight();
},0);






