require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf'], function ($) {
    $(function () {

        $('.search').click(function(){
            var title = $('.jq-title').val();
            window.location.href = '/linkexchange-manage/linkexchange?title='+title+'&index=1&pageSize=10';
        });

        $('.publishAD').click(function(){
            window.location.href = '/linkexchange-manage/linkexchange/add';
        });

        $('.jq-delete').click(function(event) {
            if(!confirm('确定要删除吗?')) {
                return;
            }
            event.preventDefault();
            var dataForm = JSON.stringify({
                "id":$(this).data('id')
            });
            $.ajax({
                url: '/linkexchange-manage/linkexchange/delete',
                type: 'POST',
                dataType: 'json',
                data: dataForm,
                contentType: 'application/json; charset=UTF-8'
            })
            .done(function (res) {
                if(res.data.status){
                   location.href='/linkexchange-manage/linkexchange';
                }else{
                   showErrorMessage("保存失败");
                }
            })
            .fail(function() {
                console.log("error");
                $('.jq-btn-form').removeAttr('disabled');
            })
        });

        $('.jq-save').click(function(){
            $(this).attr('disabled', 'disabled');
            var showOnHomeInputVal = $('.jq-index').val();
            var showOnHome = true;
            if (showOnHomeInputVal == '0') {
                showOnHome = false;
            }
            var operate;
            if($('.jq-id').val() != null && $('.jq-id').val() != '') {
                operate = 'update';
            } else {
                operate = 'create';
            }
            var dataForm = JSON.stringify({
                "id":$('.jq-id').val(),
                "title":$('.jq-title').val(),
                "content":getContent(),
                "contentText":getContentTxt(),
                "showOnHome":showOnHome
            });
            $.ajax({
                url: '/linkexchange-manage/linkexchange/'+operate,
                type: 'POST',
                dataType: 'json',
                data: dataForm,
                contentType: 'application/json; charset=UTF-8'
            })
            .done(function (res) {
                if(res.data.status){
                    location.href='/linkexchange-manage/linkexchange';
                }else{
                    showErrorMessage("保存失败");
                }
            })
            .fail(function() {
                $('.jq-btn-form').removeAttr('disabled');
            })
        });

        //显示警告提示
        var currentErrorObj = null;
        function showErrorMessage(msg, obj){
            currentErrorObj = obj;
            var htm = '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <span class="txt">编辑公告：'+msg+'</span></div>';
            $('.form-error').append(htm);
        }

        //关闭警告提示
        $('body').on('click','.form-error',function(){
            $('.jq-btn-form').removeAttr('disabled');
            if(!!currentErrorObj){currentErrorObj.focus();}
        });

    });
})