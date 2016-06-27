require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf'], function ($) {

    $(function () {

        var $errorDom = $('.form-error'),
            $bannerForm = $('.banner-form'),
            $submitBtn = $('#btnSave'),
            boolFlag = false;

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

        var _URL = window.URL || window.webkitURL;

        var checkImage = function(file,width,height){
            var defer = $.Deferred(),
                img = new Image();
            img.src = _URL.createObjectURL(file);
            img.onload = function(){
                if(this.width != width){
                    defer.reject('图片长宽应为'+width);
                    return;
                }
                if(this.height != height){
                    defer.reject('图片长宽应为' + height);
                    return;
                }
                defer.resolve(file);
            }
            return defer.promise();
        };

        $('.webImageUrl,.appImageUrl').on('change',function(){
            var $self = $(this),
                imageWidth,
                imageHeight;
            var file = $self.find('input').get(0).files[0];
            var formData = new FormData();
            formData.append('upfile',file);
            imageWidth = $self.find('input').attr("imageWidth");
            imageHeight = $self.find('input').attr("imageHeight");
            checkImage(file,imageWidth,imageHeight).done(function(){
                var formData = new FormData();
                formData.append('upfile',file);
                $.ajax({
                    url:'/ueditor?action=uploadimage',
                    type:'POST',
                    data:formData,
                    dataType:'JSON',
                    contentType: false,
                    processData: false
                }).done(function(data){
                    if(data.state){
                        if($self.hasClass('webImageUrl')){
                            $('.banner-webImageUrl').val(data.title);
                            $('.webImageUrlImage').html('');
                            $('.webImageUrlImage').append('<img style="width:100%" src="/' + data.title + '" alt="大图">');
                        }
                        if($self.hasClass('appImageUrl')){
                            $('.banner-appImageUrl').val(data.title)
                            $('.appImageUrlImage').html('');
                            $('.appImageUrlImage').append('<img style="width:100%" src="/' + data.title + '" alt="小图">');
                        }
                    }
                });
            }).fail(function(message){
                if($self.hasClass('webImageUrl')){
                    showErrorMessage(message, $('.banner-webImageUrl', $bannerForm));
                }
                if($self.hasClass('appImageUrl')){
                    showErrorMessage(message, $('.banner-appImageUrl', $bannerForm));
                }
            });
        });

        $('body').on('click', '.form-error', function () {
            $submitBtn.removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });

        $(".banner-form").Validform({
            btnSubmit: '#btnSave',
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
                var order = $('.order').val();
                if (isNaN(order) || parseInt(order) <= 0) {
                    showErrorMessage('序号必须为正整数', $('.order', curform));
                    return false;
                }

                if ($('.source:checked').length <= 0) {
                    showErrorMessage('终端必须选择', $('.source', curform));
                    return false;
                }

            },
            callback: function(form) {
                boolFlag = true;
                return false;
            }
        });

        $submitBtn.on('click', function(event) {
            event.preventDefault();
            var $self = $(this);
            if (boolFlag) {
                if (confirm("确认提交审核?")) {
                    $self.attr('disabled', 'disabled');
                    var operate;
                    if($('.jq-id').val() != null && $('.jq-id').val() != '') {
                        if($('.jq_operator').val() == 'edit'){
                            operate = 'edit';
                        }
                        else{
                            operate = 'reuse';
                        }

                    } else {
                        operate = 'create';
                    }
                    $bannerForm[0].target='';
                    $bannerForm[0].action = "/banner-manage/"+operate;
                    $bannerForm[0].submit();
                }

            }
        });

    });

});