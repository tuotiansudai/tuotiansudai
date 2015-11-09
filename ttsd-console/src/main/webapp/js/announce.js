/**
 * Created by CBJ on 2015/11/2.
 */
window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'bootstrap','Validform','Validform_Datatype', 'ueditor','jquery-ui','csrf'], function ($) {
    $(function () {
        var formFlag =false;
        $(".jq-form").Validform({
            btnSubmit:'.jq-btn-form',
            tipSweep: true,
            focusOnError: false,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            //beforeSubmit
            beforeCheck: function(curform){

            },
            callback:function(form){
                formFlag = true;
                return false;
            }
        });

        $('.search').click(function(){
            var id = $('.jq-id').val();
            var title = $('.jq-title').val();
            window.location.href = '/announceManage?id='+id+'&title='+title+'&currentPageNo=1&pageSize=10';
        });

        $('.publishAD').click(function(){
            window.location.href = '/announce';
        });

        $('.jq-delete').click(function(event) {
            event.preventDefault();
            var dataForm = JSON.stringify({
                "id":$(this).data('id')
            });
            $.ajax({
                url: '/announce/delete',
                type: 'POST',
                dataType: 'json',
                data: dataForm,
                contentType: 'application/json; charset=UTF-8'
            })
            .done(function (res) {
                if(res.data.status){
                   location.href='/announceManage';
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
                "showOnHome":showOnHome
            });
            $.ajax({
                url: '/announce/'+operate,
                type: 'POST',
                dataType: 'json',
                data: dataForm,
                contentType: 'application/json; charset=UTF-8'
            })
            .done(function (res) {
                if(res.data.status){
                    location.href='/announceManage';
                }else{
                    showErrorMessage("保存失败");
                }
            })
            .fail(function() {
                console.log("error");
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
        $('.jq-checkbox label').click(function () {
            if ($('.jq-index').prop('checked')) {
                $('.jq-index').val('1');
            } else {
                $('.jq-index').val('0');
            }
        });

    });
})