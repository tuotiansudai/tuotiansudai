require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'csrf'], function ($) {
    $(function () {
        $('.pass').click(function (event) {
            event.preventDefault();
            if (!confirm("确定同意吗?")) {
                return false;
            }
            $.ajax({
                url: "/app-push-manage/manual-app-push/" + $(this).attr('data-pushId') + "/pass",
                type: 'GET',
                dataType: 'json',
                data: {}
            }).done(function (res) {
                if(res.data.status){
                    location.href='/app-push-manage/manual-app-push-list';
                }else{
                    var msg = res.data.message || '服务端校验失败';
                    showErrorMessage(msg);
                }
            }).fail(function () {
                console.log("error");
            }).always(function () {
                console.log("complete");
            });
        });

        //显示警告提示
        var currentErrorObj = null;
        function showErrorMessage(msg, obj){
            currentErrorObj = obj;
            //var htm = '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <span class="txt">' + msg + '</span></div>';
            //$('.form-error').append(htm);
            alert(msg);
        }

        //关闭警告提示
        $('body').on('click','.form-error',function(){
            //$('.jq-btn-form').removeAttr('disabled');
            if(!!currentErrorObj){currentErrorObj.focus();}
        });
    });

});