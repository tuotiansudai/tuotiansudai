/**
 * Created by CBJ on 2015/10/21.
 */
require(['jquery','echarts','commonFun'], function ($) {
    $(function () {
    var $tMonthBox=$('#tMonthBox'),
        $switchMenu=$('ul',$tMonthBox);
        $switchMenu.find('li').first().addClass('current');
        $('table',$tMonthBox).eq(0).show().siblings('table').hide();
        $switchMenu.find('li').click(function(index) {
            var $this=$(this),
                num=$switchMenu.find('li').index(this);
            $this.addClass('current').siblings('li').removeClass('current');
            $('table',$tMonthBox).eq(num).show().siblings('table').hide();
        });

        var data = [{ name: '可用金额', value: pydata.balance },
                { name: '待收本金', value: pydata.collectingPrincipal },
                { name: '待收收益', value: pydata.collectingInterest}],
         option = MyChartsObject.ChartOptionTemplates.Pie(data,'YTTTTT'),
         container = $("#ReportShow")[0],
         opt = MyChartsObject.ChartConfig(container, option);
        MyChartsObject.Charts.RenderChart(opt);

    });
});