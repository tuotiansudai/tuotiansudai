require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf'], function ($) {
    $(function () {

        $('.bannerAD').click(function(){
            window.location.href = '/banner-manage/create';
        });

    });
})