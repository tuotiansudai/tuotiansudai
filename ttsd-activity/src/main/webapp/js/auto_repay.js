require(['jquery', 'csrf', 'commonFun', 'jquery.validate'], function ($) {
    $(function () {
        if ($('#btnAuthority').length) {
            var $btnAuthority = $('#btnAuthority');
            $btnAuthority.click(function () {
                var content = '<div cass="auto-invest"><button id="finishAuthor" class="btn btn-normal">已完成授权</button></div>';
                commonFun.popWindow('自动还款授权', content, {
                    width: '450px'
                });

                $('body').delegate('#finishAuthor', 'click', function () {
                    location.href = '/loaner/loan-list';
                });
            });
        }
    });
});