require(['jquery', 'csrf', 'jquery-ui', 'bootstrapSelect', 'bootstrap', 'layer','layer-extend', 'layerWrapper'], function ($) {
    $('#status').selectpicker();
    $('#channel').selectpicker();
    $('.file-btn').on('change', function () {
        var file = $(this).find('input').get(0).files[0];
        var formData = new FormData();
        formData.append('file', file);
        $.ajax({
            url: '/point-manage/import',
            type: 'POST',
            data: formData,
            dataType: 'JSON',
            contentType: false,
            processData: false
        })
            .done(function (data) {
                if (data.status) {
                    window.location.href = "/point-manage/channel-point-detail/" + data.id
                }
                else {
                    layer.open({
                        title: '提示',
                        content: data.message,
                        btn: ['确定']

                    });
                    return false;
                }

            });
    });

});