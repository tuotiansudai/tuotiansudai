require(['jquery','bootstrap', 'bootstrapSelect','bootstrapDatetimepicker','jquery-ui'], function($) {
    $(function () {
        $('.selectpicker').selectpicker();

        $('.search').click(function(){
            var title = $('.jq-title').val();
            var articleSectionType = $('form select[name="articleSectionType"]').val();
            window.location.href = '/announce-manage/article/list?title='+title+'&articleSectionType='+articleSectionType;
        });

        $('.publishAD').click(function(){
            window.location.href = '/announce-manage/article/create';
        });

    });
})