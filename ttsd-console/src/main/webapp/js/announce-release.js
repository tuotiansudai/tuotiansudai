/**
 * Created by CBJ on 2015/11/2.
 */
window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'bootstrap','Validform_v5.3.2','Validform_Datatype', 'ueditor', 'ueditor-all','ueditor-lang', 'ueditor-config'], function ($) {
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
        //提交表单
        $('.jq-btn-form').click(function () {
            if(formFlag) {
                var showOnHomeInputVal = $('.jq-index').val(),
                    showOnHome=(showOnHomeInputVal=='0')?false:true;

                var dataForm = JSON.stringify({
                    "projectName": $('.jq-title').val(),
                    "descriptionText": getContentTxt(),
                    "descriptionHtml": getContent(),
                });
                $.ajax({
                    url: url,
                    type: 'POST',
                    dataType: 'json',
                    data: dataForm
                }).done(function (res) {

                }).fail(function () {
                    console.log("error");
                    $('.jq-btn-form').removeAttr('disabled');
                })

            }
        });
    });
})