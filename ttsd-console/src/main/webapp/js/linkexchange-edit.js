require(['jquery', 'bootstrap','Validform','Validform_Datatype', 'jquery-ui','csrf'], function ($) {
    $(function () {
        $('.jq-save').click(function(){
            $(this).attr('disabled', 'disabled');
            var operate;
            if($('.jq-id').val() != null && $('.jq-id').val() != '') {
                operate = 'update';
            } else {
                operate = 'create';
            }

            var getWebSiteTypes = function() {
                var obj=[];
                $('.jq-webSiteType').each(function(key,option) {

                    if(option.checked) {
                        obj.push(option.value);
                    }
                })
                return obj.join(',');
            }


            var dataForm = JSON.stringify({
                "id":$('.jq-id').val(),
                "title":$('.jq-title').val(),
                "linkUrl": $('.jq-linkurl').val(),
                "noFollow": $('.jq-noFollow').val(),
                "webSiteTypes": getWebSiteTypes()
            });
            if($('.jq-title').val() == ''
                || $('.jq-linkurl').val().trim() == ''){
                return false;
            }
            if(!IsURL($('.jq-linkurl').val())){
                showErrorMessage("网址格式不正确,请以http://或 https://开始");
                return false;
            }

            var len= $('input[name="webSiteTypes"]').filter(function(key,option) {
                return $(option).is(':checked');
            }).length;

            if(len <= 0){
                showErrorMessage("请选择显示位置");
                return false;
            }

            $.ajax({
                url: '/link-exchange-manage/link-exchange/'+operate,
                type: 'POST',
                dataType: 'json',
                data: dataForm,
                contentType: 'application/json; charset=UTF-8'
            })
                .done(function (res) {
                    if(res.data.status){
                        location.href='/link-exchange-manage/link-exchange';
                    }else{
                        showErrorMessage("保存失败");
                    }
                })
                .fail(function() {
                    console.log("error");
                    $('.jq-btn-form').removeAttr('disabled');
                })
        });
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
            var htm = '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <span class="txt">友情链接：'+msg+'</span></div>';
            $('.form-error').append(htm);
        }
        //验证网址的合法性
        function IsURL(str_url){
            str_url = str_url.match(/^(https|http)?:\/\/.+/);
            if (str_url == null){
                return false;
            }else{
                return true;
            }
        }

        //控制noFollow的checkbox
        $('.jq-noFollow').click(function () {
            if ($('.jq-noFollow').val() == "true") {
                $('.jq-noFollow').val("false");
            } else {
                $('.jq-noFollow').val("true");
            }
        });
        //关闭警告提示
        $('body').on('click','.form-error',function(){
            $('.jq-btn-form').removeAttr('disabled');
            if(!!currentErrorObj){currentErrorObj.focus();}
        });

        $('.jq-cancel').click(function () {
            location.href = "/link-exchange-manage/link-exchange";
        });

    });
})