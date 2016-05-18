window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'bootstrap','Validform','Validform_Datatype', 'bootstrapSelect','ueditor','jquery-ui','csrf'], function ($) {
    $(function () {
        var $selectDom = $('.selectpicker'), //select表单
            $submitBtn = $('.article-save'), //提交按钮
            boolFlag = false, //校验布尔变量值
            $errorDom = $('.form-error'), //错误提示节点
            $articleForm = $('.article-form');
        //渲染select表单
        $selectDom.selectpicker();

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

        $('.thumbPicture,.showPicture').on('change',function(){
            var $self = $(this);
            var file = $self.find('input').get(0).files[0];
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
                    if($self.hasClass('thumbPicture')){
                        $('.article-thumbPicture').val(data.url);
                        $('.thumbPictureImage').html('');
                        $('.thumbPictureImage').append('<img style="width:100%" src="/upload/' + data.url + '" alt="缩略图">');

                    }
                    if($self.hasClass('showPicture')){
                        $('.article-showPicture').val(data.url)
                        $('.showPictureImage').html('');
                        $('.showPictureImage').append('<img style="width:100%" src="/upload/' + data.url + '" alt="展示图">');
                    }
                }
            });
        });

        //表单校验初始化参数
        $(".article-form").Validform({
            btnSubmit: '.article-save',
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

                if ($articleTitle.length > 17) {
                    showErrorMessage('标题最多17个中文字符', $('.article-title', curform));
                    return false;
                }
                if ($articleSource.length > 17) {
                    showErrorMessage('文章来源最多17个中文字符', $('.article-source', curform));
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
                    var articleId = $('.articleId').val();
                    console.log(articleId);
                    if(articleId != ''){
                        $articleForm[0].action = "/announce-manage/article/edit/"+articleId;
                    }else{
                        $articleForm[0].action = "/announce-manage/article/create";
                    }
                    $self.attr('disabled', 'disabled');
                    $articleForm[0].submit();
                }

            }
        });
    });
})