window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'bootstrap','Validform','Validform_Datatype', 'bootstrapSelect','ueditor','jquery-ui','csrf','bootstrapDatetimepicker'], function ($) {
    $(function () {
        var $selectDom = $('.selectpicker'), //select表单
            $submitBtn = $('.article-save'), //提交按钮
            $previewBtn = $('.preview'), //按钮
            boolFlag = false, //校验布尔变量值
            $errorDom = $('.form-error'), //错误提示节点
            $timingDate = $('#timingTime'), //开始时间
            $articleForm = $('.article-form');
        //渲染select表单
        $selectDom.selectpicker();

        $timingDate.datetimepicker({
            format: 'YYYY-MM-DD HH:mm'
        })

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
        var _URL = window.URL || window.webkitURL;
        $('.thumbPicture,.showPicture').on('change',function(){
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
                            if($self.hasClass('thumbPicture')){
                                $('.article-thumbPicture').val(data.title);
                                $('.thumbPictureImage').html('');
                                $('.thumbPictureImage').append('<img style="width:100%" src="' + data.title + '" alt="缩略图">');

                            }
                            if($self.hasClass('showPicture')){
                                $('.article-showPicture').val(data.title)
                                $('.showPictureImage').html('');
                                $('.showPictureImage').append('<img style="width:100%" src="' + data.title + '" alt="展示图">');
                            }
                        }
                    });
            }).fail(function(message){
                if($self.hasClass('thumbPicture')){
                    showErrorMessage(message, $('.article-thumbPicture', $articleForm));
                }
                if($self.hasClass('showPicture')){

                    showErrorMessage(message, $('.article-showPicture', $articleForm));
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

        //表单校验初始化参数
        $(".article-form").Validform({
            btnSubmit: '.article-save,.preview',
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
                var $articleTitle = $('.article-title').val(),
                    $articleSource = $('.article-source').val();
                var subSectionVal = $('input[name="subSection"]:checked').val();

                if ($articleTitle.length > 30) {
                    showErrorMessage('标题最多30个中文字符', $('.article-title', curform));
                    return false;
                }
                if ($articleSource.length > 25) {
                    showErrorMessage('文章来源最多17个中文字符', $('.article-source', curform));
                    return false;
                }
                var sectionVal=$('#section').val();
                if(sectionVal =='KNOWLEDGE' &&(subSectionVal == null || subSectionVal== '')){
                    showErrorMessage('网贷课堂子栏目必选', $('.subSectionLabel', curform));
                    return false;
                }


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
                if (confirm("确认提交审核?")) {
                    $('.article-content').val(getContent());
                    $self.attr('disabled', 'disabled');
                    $articleForm[0].target='';
                    $articleForm[0].action = "/announce-manage/article/create";
                    $articleForm[0].submit();
                }

            }
        });
        //预览
        $previewBtn.on('click', function(event) {
            event.preventDefault();
            var $self = $(this);
            if (boolFlag) {

                $('.article-content').val(getContent());
                $self.attr('disabled', 'disabled');
                $articleForm[0].target='_blank';
                $articleForm[0].action = "/announce-manage/article/preview";
                $articleForm[0].submit();
            }

        });
        //栏目改变
        $('#section').on('change',function(){
           if( $(this).val() == 'KNOWLEDGE'){
               $('#subSectionDiv').show();
           }else{
               $('#subSectionDiv').hide();
           }
        });
    });
})