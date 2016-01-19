require(['jquery', 'template', 'csrf', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'Validform', 'Validform_Datatype'], function ($) {
    $(function () {
        var $selectDom = $('.selectpicker'), //select表单
            $errorDom = $('.form-error'), //错误提示节点
            $submitBtn = $('#btnSave'), //提交按钮
            $manualAppPushForm = $('.form-list'),
            boolFlag = false, //校验布尔变量值
            currentErrorObj = null;


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

        String.format = function () {
            if (arguments.length == 0)
                return null;
            var str = arguments[0];
            for (var i = 1; i < arguments.length; i++) {
                var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
                str = str.replace(re, arguments[i]);
            }
            return str;
        };

        $('body').on('click', '.form-error', function () {
            $submitBtn.removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });

        $('select.pushType').change(function () {
            var nowDate = new Date(),
                yyyy = nowDate.getFullYear(),
                MM = nowDate.getMonth() + 1,
                dd = nowDate.getDate();
            var yyyyMMdd = "" + yyyy;
            if (MM >= 10) {
                yyyyMMdd += MM + "";
            }
            else {
                yyyyMMdd += "0" + MM + "";
            }
            if (dd >= 10) {
                yyyyMMdd += dd;
            }
            else {
                yyyyMMdd += "0" + dd;
            }

            var pushTypeText = $(this).find("option:selected").text();
            var nameTemplate = "{0}-{1}-{2}"
            var pushType = $(this).val();
            $.get('/app-push-manage/manual-app-push/push-type/' + pushType, function (data) {
                var serialNo = data + 1;
                $('.name').val(String.format(nameTemplate, yyyyMMdd, pushTypeText, serialNo));
            });

        }).trigger('change');

        $('select.jumpTo').change(function () {
            var jumpTo = $(this).val();
            if (jumpTo == 'OTHER') {
                $('.jump-to-link').removeClass('app-push-link').val('');
            } else {
                $('.jump-to-link').addClass('app-push-link').val('');
            }

        }).trigger('change');

        $('.push_object_choose').click(function () {

            if($(this).val() == 'district'){
                $('.province').removeClass('app-push-link');
            }else{
                $('.pushObject').prop('checked',false);
                $('.province').addClass('app-push-link');
            }
        });

        //表单校验初始化参数
        $(".form-list").Validform({
            btnSubmit: '#btnSave',
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            ignoreHidden:true,
            tiptype: function (msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function (curform) {
                $errorDom.html('');
                var content = $('.content').val();
                if(typeof content == 'undefined' || content == '' ){
                    showErrorMessage('推送模板不能为空', $('.content', curform));
                    return false;
                }
                var jumpTo = $('.jumpTo').val();
                var jumpToLink = $('.jump-link-text').val();
                if (jumpTo == 'OTHER' && jumpToLink == '') {
                    showErrorMessage('链接地址不能为空', $('.jump-link-text', curform));
                    return false;
                }
                var reg_url = /^(http|https):\/\/([\w-]+\.)+[\w-]+(\/[\w-.\/?%&=]*)?$/;
                if(jumpTo == 'OTHER' && jumpToLink != '' && !reg_url.test(jumpToLink)){
                    showErrorMessage('链接地址输入不正确', $('.jump-link-text', curform));
                    return false;
                }


            },
            callback: function (form) {
                boolFlag = true;
                return false;
            }
        });

        //提交表单
        $submitBtn.on('click', function (event) {
            event.preventDefault();
            var $self = $(this);
            if (boolFlag) {
                $self.attr('disabled', 'disabled');
                $manualAppPushForm[0].submit();
            }
        });

    });
});