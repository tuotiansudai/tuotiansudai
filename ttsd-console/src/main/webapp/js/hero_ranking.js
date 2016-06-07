require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf'], function ($) {
    $(function () {
        var boolFlag = false, //校验布尔变量值
            $errorDom = $('.form-error'), //错误提示节点
            $submitBtn = $('.prize-save'), //提交按钮
            $prizeForm = $('.prize-form');
        var _URL = window.URL || window.webkitURL;
        $('.prize-image').on('change',function(){
            var $self = $(this),
                file = $self.find('input').get(0).files[0];
                checkImage(file).done(function(){
                    var formData = new FormData();
                    formData.append('upfile',file);
                    $.ajax({
                        url:'/ueditor?action=uploadimage',
                        type:'POST',
                        data:formData,
                        dataType:'JSON',
                        contentType: false,
                        processData: false
                    })
                        .done(function(data){
                            if(data.state){
                                $('.image-url').val(data.title);
                                $('.thumbImage').html('');
                                $('.thumbImage').append('<img style="width:100%" src="/' + data.title + '" alt="神秘大奖缩略图">');
                            }
                        });
                }).fail(function(message){
                    showErrorMessage(message, $('.image-url', $prizeForm));
                });

        });

        var checkImage = function(file){
            var defer = $.Deferred(),
                img = new Image();
                img.src = _URL.createObjectURL(file);
                img.onload = function(){
                    if(this.width > 250){
                        defer.reject('图片长宽应为250px!');
                        return;

                    }
                    if(this.height > 250){
                        defer.reject('图片长宽应为250px!');
                        return;
                    }
                    defer.resolve(file);
                }
            return defer.promise();

        };

        //表单校验初始化参数
        $(".prize-form").Validform({
            btnSubmit: '.prize-save',
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            ignoreHidden:true,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function(curform) {
                $errorDom.html('');

            },
            callback: function(form) {
                boolFlag = true;
                return false;
            }
        });

        //关闭警告提示
        $('body').on('click', '.form-error', function () {
            $submitBtn.removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });
        //提交表单
        $submitBtn.on('click', function(event) {
            event.preventDefault();
            var $self = $(this);
            if (boolFlag) {
                if (confirm("确认提交更新?")) {
                    $self.attr('disabled', 'disabled');
                    $prizeForm[0].submit();
                }

            }
        });
        /**
         * @msg  {[string]} //文字信息
         * @obj  {[object]} //传入的dom节点
         * @return {[html]} //生成html
         */
        function showErrorMessage(msg, obj) {
            currentErrorObj = obj;
            var html = '';
            html += '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">';
            html += '<button type="button" class="close" data-dismiss="alert" aria-label="Close">';
            html += '<span aria-hidden="true">&times;</span>';
            html += '</button>';
            html += '<span class="txt">创建失败：' + msg + '</span>';
            html += '</div>';
            $errorDom.append(html);
        }
    });
})