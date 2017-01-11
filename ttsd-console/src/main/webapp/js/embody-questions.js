require(['jquery', 'layerWrapper', 'bootstrap', 'csrf'], function ($, _) {
    $(function () {

        $('.file-btn').on('change',function(){
            $('.name-tr').remove();
            var file = $(this).find('input').get(0).files[0];
            var formData = new FormData();
            formData.append('file',file);
            $.ajax({
                    url:'/ask-manage/import-excel',
                    type:'POST',
                    data:formData,
                    dataType:'JSON',
                    contentType: false,
                    processData: false
                })
                .done(function(data){
                    if (data.status) {
                        $('#import-file').val(data.fileUuid);
                    } else {
                    }
                    layer.msg(data.message, function(){
                        location.reload();
                    });
                });
        });

    });
});
