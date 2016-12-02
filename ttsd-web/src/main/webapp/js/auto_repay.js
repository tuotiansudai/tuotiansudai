require(['jquery','layerWrapper','csrf',  'jquery.validate'], function ($,layer) {
    $(function () {
        var $btnAuthority = $('#btnAuthority');
        if ($btnAuthority.length) {
            $btnAuthority.click(function () {
                layer.open({
                    type: 1,
                    title: '自动还款授权',
                    area: ['450px', '100px'],
                    content: $btnAuthority.parents('.auto-repay-tip')
                });

                $btnAuthority.on('click',function() {
                    location.href = '/loaner/loan-list';
                });
            });
        }
    });
});