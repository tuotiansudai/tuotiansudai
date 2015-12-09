require(['jquery','echarts','commonFun', 'csrf','layerWrapper'], function ($) {
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
                { name: '待收利息', value: pydata.collectingInterest}],
         option = MyChartsObject.ChartOptionTemplates.Pie(data,'YTTTTT'),
         container = $("#ReportShow")[0],
         opt = MyChartsObject.ChartConfig(container, option);
        MyChartsObject.Charts.RenderChart(opt);


        tipshow('#tMonthBox','.month-title',6);
        tipshow('.newProjects','.trade-detail',15);

        /**
         * @dom  {[string]}		当前列表的DOM节点
         * @active  {[string]}	触发事件的DOM节点
         * @length  {[number]}	限制文字长度触发提示框
         * @return {[function]}
         */
        function tipshow(dom,active,length){
            $(dom).on('mouseenter',active,function() {
                layer.closeAll('tips');
                if($(this).text().length>length){
                    layer.tips($(this).text(), $(this), {
                        tips: [1, '#efbf5c'],
                        time: 2000,
                        tipsMore: true,
                        area: 'auto',
                        maxWidth: '500'
                    });
                }
            });
        }
    });
});