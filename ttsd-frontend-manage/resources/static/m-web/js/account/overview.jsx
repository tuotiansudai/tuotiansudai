require('mWebStyle/account/overview.scss');
let menuClick = require('mWebJsModule/menuClick');
let $accountOverview = $('#accountOverview');

menuClick({
    pageDom:$accountOverview
});

//累计收益
require.ensure(['publicJs/load_echarts','publicJs/commonFun'],function() {
    let loadEcharts = require('publicJs/load_echarts');
    let commonFun=require('publicJs/commonFun');
    if (!$("#dataRecord").length) {
        return;
    }
    var data = [{ name: '可用金额', value: '100' },
            { name: '待收投资本金', value: '200' },
            { name: '待收预期收益', value: '300'}],
        option = loadEcharts.ChartOptionTemplates.PieOption(data,{
            tooltip:false,
            legend:false
        }),
        opt = loadEcharts.ChartConfig('dataRecord', option);
    loadEcharts.RenderChart(opt);

    let ReceivedOpt = loadEcharts.ChartConfig('receivedRecord', option);
    loadEcharts.RenderChart(ReceivedOpt);

},'accumulateIncome');


