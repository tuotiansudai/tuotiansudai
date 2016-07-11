require(['jquery', 'bootstrap','Validform','Validform_Datatype', 'bootstrapDatetimepicker','jquery-ui','csrf'], function ($) {
    $(function () {
        var $activityCenterForm = $('.activity-form'),
            boolFlag = false, //校验布尔变量值
            $errorDom = $('.form-error'); //错误提示节点
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        var _URL = window.URL || window.webkitURL;

        $('.appPicture,.webPicture').on('change',function(){
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
                        if($self.hasClass('webPicture')){
                            $('.webPictureUrl').val(data.title);
                            $('.webPictureImage').html('');
                            $('.webPictureImage').append('<img style="width:100%" src="/' + data.title + '" alt="缩略图">');

                        }
                        if($self.hasClass('appPicture')){
                            $('.appPictureUrl').val(data.title)
                            $('.appPictureImage').html('');
                            $('.appPictureImage').append('<img style="width:100%" src="/' + data.title + '" alt="展示图">');
                        }
                    }
                });
            }).fail(function(message){
                if($self.hasClass('webPicture')){
                    $('.webPictureImage').html('');
                    $('.webPictureUrl').val('');
                    showErrorMessage(message, $('.webPictureUrl', $activityCenterForm));
                }
                if($self.hasClass('appPicture')){
                    $('.appPictureImage').html('');
                    $('.appPictureUrl').val('');
                    showErrorMessage(message, $('.appPictureUrl', $activityCenterForm));
                }

            });

        });

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

        //关闭警告提示
        $('body').on('click', '.form-error', function () {
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });


        //表单校验初始化参数
        $activityCenterForm.Validform({
            btnSubmit: '.activity-to_approve,.activity-rejection,.activity-approved',
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            ignoreHidden:true,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function($activityCenterForm) {
                $errorDom.html('');
                var activityDescription = $('.activity-description').val();

                if (activityDescription.length > 15) {
                    showErrorMessage('活动介绍最多15个中文字符', $('.activity-description', $activityCenterForm));
                    return false;
                }

            },
            callback: function($activityCenterForm) {
                boolFlag = true;
                return false;
            }
        });

        $('.activity-to_approve,.activity-rejection,.activity-approved').on('click', function(event) {
            event.preventDefault();
            var $self = $(this),
                actionUrl,
                operator;
            if (boolFlag) {
                $self.attr('disabled', 'disabled');
                if($self.hasClass("activity-to_approve")){
                    operator = "提交审核";
                    actionUrl = "/activity-manage/activity-center/TO_APPROVE";
                }else if($self.hasClass("activity-rejection")){
                    operator = "驳回";
                    actionUrl = "/activity-manage/activity-center/REJECTION";

                }else if($self.hasClass("activity-approved")){
                    operator = "审核通过";
                    actionUrl = "/activity-manage/activity-center/APPROVED";
                }
                $activityCenterForm[0].action = actionUrl;
                if(confirm('确定要'+operator +'吗?')) {
                    $activityCenterForm[0].submit();
                }else{
                    $self.removeAttr('disabled');
                }
            }
        });

    });
})