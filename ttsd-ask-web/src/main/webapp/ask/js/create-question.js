/* create question */
var _ = require('underscore');
var comm = require("./commonFun");
var $createQuestion=$('#createQuestion');
var $questionDetailTag=$('#questionDetailTag');

//刷新验证码
var refreshCaptcha=function() {
    $('.captchaImg').attr('src', '/captcha?' + new Date().getTime().toString());
}
$('.captchaImg').on('click',function() {
    refreshCaptcha();
});

var utils = {
    showError:function(element,value) {
        element.parent().find('.error').text(value).show();
    },
    hideError:function(element,value) {
        element.parent().find('.error').hide();
    },
    validLen:function(element,num) {
        var len=element.val().split('').length;
        var name=element[0].name,
            value=$.trim(element.val());
        var $wordstip=element.parent().find('.words-tip');
        var $formSubmit=element.parents('form').find('.formSubmit');
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
                if(!/^\d{5}$/.test(value)) {
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
                if(/^(\S){0,10}$/.test(value)){
                    answerValid=false;
                    this.showError(element, errorMsg);
                }
                else {
                    answerValid=true;
                    this.hideError(element);
                }

                break;
            default:
                break;

        }
        //switch end
        if($createQuestion.length) {
            if(tagValid && questionValid && additionValid && captchaValid) {
                $formSubmit.prop('disabled',false);
            }
            else {
                $formSubmit.prop('disabled',true);
            }
        }

        if($questionDetailTag.length) {
            if(captchaValid && answerValid) {
                $formSubmit.prop('disabled',false);
            }
            else {
                $formSubmit.prop('disabled',true);
            }
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
            case 'answer':
                return utils.validLen($ele,5);
                break;
            default:
                return utils.radioChecked($ele)
                break;
        }

    });
};
//我来回答
if($questionDetailTag.length) {
    var $formAnswer=$('.formAnswer',$questionDetailTag),
        $answerButton=$('.answer-button',$questionDetailTag),
        $toAnswerBox=$('.to-answer-box',$questionDetailTag),
        $formAnswerSubmit=$('.formSubmit',$formAnswer);
    var answerValid=false,
        captchaValid=false;
    $answerButton.find('button').on('click',function(index) {
        $toAnswerBox.toggle();
    });

    $formAnswer.find('input.captcha').on('blur',function() {
        $(this).checkFrom();
    });

    $formAnswer.find('textarea.answer').on('blur',function() {
        $(this).checkFrom();
    });

    $formAnswerSubmit.on('click',function() {
            $.ajax({
                url: "/answer",
                data: $formAnswer.serialize(),
                type: 'POST'
            }).done(function(data) {
                if (data.data.status) {
                    comm.popWindow('','回答成功!',{ width:'200px'});
                    setTimeout(function() {
                        $('.popWindow,.popWindow-overlay').fadeOut();
                        window.location.reload();
                    },3000);
                }
            })
                .fail(function(data) {

                });

    });
    //采纳此条信息
    $('.mark-this-answer',$questionDetailTag).on('click',function() {
        var $this=$(this);
        var answerId=$this.next('.answerId').data('id');
        $.ajax({
            url:'/answer/'+answerId+'/best',
            type:'POST'
        }).done(function(data) {
            if(data.data.status) {
               window.location.reload();
           }
        });
    });

    $('.agree-ok',$questionDetailTag).on('click',function() {

        var $this=$(this),
            value=$.trim($this.text());
        var answerId=$this.parent().find('.answerId').data('id');
        $.ajax({
            url:'/answer/'+answerId+'/favor',
            type:'POST'
        }).done(function(data) {
            if(data.data.status) {
                $this.addClass('active');
                $this.text(value-0+1);
            }
        });
    });
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

    $formQuestion.find('input.ask-con,input.tag').on('change keyup',function(event) {
        $(this).checkFrom();
    });

    $formQuestion.find('textarea.addition').on('keyup',function() {
        $(this).checkFrom();
    });

    $formQuestion.find('input.captcha').on('blur',function() {
        $(this).checkFrom();
    });

    $formSubmit.on('click',function() {
            $.ajax({
                    url: "/question",
                    data: $formQuestion.serialize(),
                    type: 'POST'
                }).done(function(data) {
                    if (data.data.status) {
                        location.href='question/my-questions';
                    }
                    else {
                        refreshCaptcha();
                        $('.captchaImg').parent().find('.error').show().text('验证码错误');
                    }
                })
                .fail(function(data) {
                        comm.popWindow('error','error',{ width:'300px'});
                });


    });
}

