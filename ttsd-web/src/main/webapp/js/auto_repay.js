require(['jquery','layerWrapper','csrf',  'jquery.validate'], function ($,layer) {
    $(function () {
        var $autoRepayFrame=$('#autoRepayFrame');
        var $btnAuthority = $('#btnAuthority');
        if ($btnAuthority.length) {
            $btnAuthority.click(function () {
                layer.open({
                    type: 1,
                    title: '自动还款授权',
                    area: ['400px', '140px'],
                    content: $autoRepayFrame.find('.auto-repay-tip')
                });

                $('#finishAuthor').on('click',function() {
                    location.href = '/loaner/loan-list';
                });
            });
        }
    });
});