/* create question */
var comm = require('./commonFun');
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
    validLen:function(element,min,max) {
        var min=min || false,
            max=max || false;
        var value=$.trim(element.val()),
            tempDes=value.replace(/\n/g,'\\n')
                .replace(/\r/g,'\\r');
        var checkWrap=value.replace(/\n/g,'')
                        .replace(/\r/g,'');

        var len=tempDes.split('').length;
        var name=element[0].name;
        var $wordstip=element.parent().find('.words-tip');
        var $formSubmit=element.parents('form').find('.formSubmit');
        var errorMsg='';

        switch (name) {
            case 'question':
                errorMsg = '请描述您的问题';
                $wordstip.removeClass('error')
                    .find('em')
                    .text(len);

                if(len<min || checkWrap.length==0 || len==0) {
                    errorMsg = '您的问题不能为空';
                    questionValid = false;
                    this.showError(element, errorMsg);
                    $wordstip.addClass('red-color');
                }
                else if(len>max){
                    errorMsg = '您的问题不能超过' + max + '个字符';
                    questionValid = false;
                    this.showError(element, errorMsg);
                    $wordstip.addClass('red-color');
                } else {
                    this.hideError(element);
                    questionValid = true;
                    $wordstip.removeClass('red-color');
                }

                break;

            case 'addition':
                $wordstip.removeClass('error')
                    .find('em')
                    .text(len);

                if(len<min || checkWrap.length==0 || len==0) {
                    errorMsg = '问题补充不能为空';
                    questionValid = false;
                    this.showError(element, errorMsg);
                    $wordstip.addClass('red-color');
                }
                else if(len>max){
                    errorMsg = '问题补充不能超过' + max + '个字符';
                    questionValid = false;
                    this.showError(element, errorMsg);
                    $wordstip.addClass('red-color');
                } else {
                    this.hideError(element);
                    questionValid = true;
                    $wordstip.removeClass('red-color');
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


                if(len<min || checkWrap.length==0 || len==0) {
                    errorMsg = '回答不得少于10个字';
                    answerValid = false;
                    this.showError(element, errorMsg);
                }
                else if(len>max){
                    errorMsg = '回答不能超过' + max + '个字符';
                    answerValid = false;
                    this.showError(element, errorMsg);
                } else {
                    this.hideError(element);
                    answerValid = true;
                }


                // if(len>=0 && len<=min) {
                //     errorMsg='回答不得少于10个字';
                //     answerValid=false;
                //     this.showError(element, errorMsg);
                // }
                // else if(len>max) {
                //     answerValid = false;
                //     errorMsg='回答不得多于1000个字';
                //     this.showError(element, errorMsg);
                // }
                // else {
                //     answerValid=true;
                //     this.hideError(element);
                // }
                // if(checkWrap.length==0 && checkWrap!=tempDes) {
                //     additionValid = false;
                //     this.hideError(element);
                // }
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
                utils.validLen($ele, 1,100);
                break;
            case 'addition':
                utils.validLen($ele, 1,500);
                break;
            case 'captcha':
                utils.validLen($ele,5,5);
                break;
            case 'answer':
                utils.validLen($ele, 10,1000);
                break;
            default:
                utils.radioChecked($ele)
                break;
        }

    });
};
$.fn.autoTextarea = function(options) {
    var defaults={
        maxHeight:null,
        minHeight:$(this).height()
    };
    var opts = $.extend({},defaults,options);
    return $(this).each(function() {
        $(this).bind("paste cut keydown keyup focus blur",function(){
            var height,style=this.style;
            this.style.height = opts.minHeight + 'px';
            if (this.scrollHeight > opts.minHeight) {
                if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
                    height = opts.maxHeight;
                    style.overflowY = 'scroll';
                } else {
                    height = this.scrollHeight;
                    style.overflowY = 'hidden';
                }
                style.height = height + 'px';
            }
        });
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

    $formAnswer.find('input.captcha').on('keyup', function () {
        $(this).checkFrom();
    });

    $formAnswer.find('textarea.answer').on('keyup', function () {
        $(this).checkFrom();
    });

    $formAnswer.find('textarea.answer').autoTextarea({
        maxHeight:800,
        minHeight:100
    });
    $formAnswerSubmit.on('click',function(event) {
        var value=$formAnswer.find('textarea').val();
        event.preventDefault();
        var temp=value.replace(/\n/g,'\\n')
            .replace(/\r/g,'\\r');
        $formAnswer.find('textarea').val(temp);
            $.ajax({
                url: "/answer",
                data: $formAnswer.serialize(),
                type: 'POST',
                dataType: 'json',
                beforeSend:function() {
                    $formAnswerSubmit.prop('disabled',true);
                }
            }).done(function(responseData) {
                 var response=responseData.data;
                    if (response.status) {
                        comm.popWindow('','回答成功!',{ width:'200px'},true);
                        setTimeout(function() {
                        $('.popWindow,.popWindow-overlay').fadeOut();
                        window.location.reload();
                    },3000);
                    }
                    else {
                        refreshCaptcha();
                        if(response.isCaptchaValid) { 
                            if(!response.isAnswerSensitiveValid) {
                                $formAnswer.find('.answer').next().show().text('您输入的内容不能包含敏感词');
                            }
                        }
                        else {
                            $formAnswer.find('.captcha').parent().find('.error').show().text('验证码错误');
                        }
                    }
            })
                .fail(function(data) {
                    comm.popWindow('','接口调用失败',{ width:'300px'});
                })
                .complete(function() {
                    $formAnswerSubmit.prop('disabled',false);
                });

    });
    //采纳此条信息
    $('.mark-this-answer',$questionDetailTag).on('click',function() {
        var $this=$(this);
        var answerId=$this.next('.answerId').data('id');
        $.ajax({
            url:'/answer/'+answerId+'/best',
            type:'POST'
        }).done(function(responseData) {
            var response=responseData.data;
            if(response.status) {
                comm.popWindow('','成功采纳此条信息!',{ width:'200px'},true);
                setTimeout(function() {
                        $('.popWindow,.popWindow-overlay').fadeOut();
                        window.location.reload();
                    },3000);
               
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

    $('.agree-ok-no',$questionDetailTag).on('click',function() {
        comm.popWindow('','<span class="clearfix">需要登录才能点赞哦～ </span><a href="https://tuotiansudai.com/login" class="btn-normal to-login">去登录</a>',{ width:'400px'});
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
        additionValid=true,
        captchaValid=false;

    $formQuestion.find('input.ask-con,input.tag').on('change keyup',function(event) {
        $(this).checkFrom();
    });

    $addition.on('keyup',function() {
        $(this).checkFrom();
    });

    $captcha.on('keyup', function () {
        $(this).checkFrom();
    });

    $formQuestion.find('textarea').autoTextarea({
        maxHeight:800,
        minHeight:100
    });
    $formSubmit.on('click',function(event) {
        var value=$formQuestion.find('textarea').val();
        event.preventDefault();

        var question=value.replace(/\n/g,'\\n')
                      .replace(/\r/g,'\\r');

        // var question=$('.question',$formQuestion).val();
        // var tag=$('.tag-list input.tag:checked',$formQuestion);

        $formQuestion.find('textarea').val(question);
        debugger

            $.ajax({
                    url: "/question",
                    data: $formQuestion.serialize(),
                    type: 'POST',
                    dataType: 'json',
                    beforeSend:function(xhr) {
                        $formSubmit.prop('disabled',true);
                    }
                }).done(function(responseData) {
                     var response=responseData.data;

                    if (response.status) {
                        location.href='question/my-questions';
                    }
                    else {
                        refreshCaptcha();
                        if(response.isCaptchaValid) {
                                if(!response.isQuestionSensitiveValid) {
                                    $question.next().show().text('您输入的内容不能包含敏感词');
                                    return;
                                }
                                if(response.isQuestionSensitiveValid && !response.isAdditionSensitiveValid) {
                                    $addition.next().show().text('您输入的内容不能包含敏感词');
                                }

                        }
                        else {
                            $captcha.parent().find('.error').show().text('验证码错误');
                        }


                    }
                })
                .fail(function(data) {
                        comm.popWindow('','接口调用失败',{ width:'300px'});
                })
                .complete(function(data) {
                   $formSubmit.prop('disabled',false);
                });


    });
}

