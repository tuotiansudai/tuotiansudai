require(['jquery', 'csrf','bootstrap', 'bootstrapSelect','bootstrapDatetimepicker','jquery-ui'], function($) {
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

        $('.check-apply').on('click', function () {
            var id = $(this).attr('data-id');
            var url = '/announce-manage/article/'+id+'/check';
            $.ajax({
                url: url,
                type: 'POST',
                dataType: 'json'
            }).done(function (date) {
                if(date.data.status){
                    window.location.href = '/announce-manage/article/'+id+'/check-view';
                }else{
                    alert(date.data.message);
                }
            });

        });
    });
})



