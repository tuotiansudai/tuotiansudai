/* create question */
var _ = require('underscore');
var comm = require("./commonFun");
var $createQuestion=$('#createQuestion');
var $questionDetailTag=$('#questionDetailTag');
var utils = {
    showError:function(element,value) {
        element.parent().find('.error').text(value).show();
    },
    hideError:function(element,value) {
        element.parent().find('.error').hide();
    },
    validLen:function(element,num) {
        var len=element.val().split('').length;
        var name=element[0].name;
        var $wordstip=element.parent().find('.words-tip');
        var errorMsg='';

        switch (name) {
            case 'question':
                errorMsg = '请描述您的问题';
                $wordstip.removeClass('error')
                    .find('em')
                    .text(len);

                if (len > num) {
                    errorMsg = '您的问题不能超过' + num + '个字符';
                    $wordstip.addClass('error');
                }

                if(len<=num && len>0) {
                    this.hideError(element);
                    questionValid = true;
                }
                else {
                    questionValid = false;
                    this.showError(element, errorMsg);
                }
                break;

            case 'addition':
                $wordstip.removeClass('error')
                    .find('em')
                    .text(len);
                if (len > num) {
                    errorMsg = '问题补充' + num + '个字符';
                    $wordstip.addClass('error');
                }
                if(len<=num && len>0) {
                    this.hideError(element);
                    additionValid=true;
                }
                else {
                    additionValid = false;
                    this.showError(element, errorMsg);
                }
                break;
            case 'captcha':
                errorMsg = '请输入正确的验证码';
                if(!/^\d{5}$/.test(element.val())) {
                    captchaValid = false;
                    this.showError(element, errorMsg);
                }
                else {
                    this.hideError(element);
                    captchaValid = true;
                }
                break;
            case 'answer':
                errorMsg='回答不得少于10个字';

                break;
            default:
                break;

        }
        //switch end

        if(tagValid && questionValid && additionValid && captchaValid) {
            $formSubmit.prop('disabled',false);
        }
        else {
            $formSubmit.prop('disabled',true);
        }

    },
    radioChecked:function(element) {
        var checkLen=$('input.tag:checked').length;

        if(checkLen>0) {
            this.hideError(element);
            tagValid=true;
            if(checkLen>3) {
                element.prop('checked',false);
            }
        }
        else {
            this.showError(element)
            tagValid=false;
        }
        if(tagValid && questionValid && additionValid && captchaValid) {
            $formSubmit.prop('disabled',false);
        }
        else {
            $formSubmit.prop('disabled',true);
        }
    }
};

//我来回答
if($questionDetailTag.length) {
    var qanswerValid=false,
        qcaptchaValid=false;
}

//我要提问
if($createQuestion.length) {
    var $formQuestion=$('.form-question',$createQuestion),
        $question=$('.ask-con',$createQuestion),
        $addition=$('.addition',$createQuestion),
        $captcha=$('.captcha',$createQuestion),
        $formSubmit=$('.formSubmit',$formQuestion),
        tagValid=false,
        questionValid=false,
        additionValid=false,
        captchaValid=false;
    $.fn.checkFrom = function () {
        return this.each(function () {
            var $ele = $(this);
            var name=this.name,
                value=$ele.val();
            switch(name) {
                case 'question':
                    return utils.validLen($ele,30);
                    break;
                case 'addition':
                    return utils.validLen($ele,10000);
                    break;
                case 'captcha':
                    return utils.validLen($ele,5);
                    break;
                default:
                    return utils.radioChecked($ele)
                    break;
            }

        });
    };

    $formQuestion.find('input.ask-con,input.tag').on('change keyup',function(event) {
        $(this).checkFrom();
    });

    $formQuestion.find('textarea.addition').on('keyup',function() {
        $(this).checkFrom();
    });

    $formQuestion.find('input.captcha').on('blur',function() {
        $(this).checkFrom();
    });

    //刷新验证码
    $('.captchaImg',$formQuestion).on('click',function() {
        $(this).attr('src', '/captcha?' + new Date().getTime().toString());
    });

    $formSubmit.on('click',function() {
        $formQuestion.find('input').checkFrom();
        if(tagValid && questionValid && additionValid && captchaValid) {
            $.ajax({
                    url: "/question",
                    data: $formQuestion.serialize(),
                    type: 'POST'
                }).done(function(data) {
                
                    if (data.status) {
                        location.href='question/my-questions';
                    }
                })
                .fail(function(data) {
                        comm.popWindow('error','error',{ width:'300px'});
                });

        }

    });
}

