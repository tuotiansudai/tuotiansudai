/* create question */
var _ = require('underscore');
var comm = require("./commonFun");
var $createQuestion=$('#createQuestion');

if($createQuestion.length) {
    comm.initToken();
    var $formQuestion=$('.form-question',$createQuestion),
        $question=$('.ask-con',$createQuestion),
        $addition=$('.addition',$createQuestion),
        $captcha=$('.captcha',$createQuestion),
        $formSubmit=$('.formSubmit',$formQuestion),
        tagValid=true;

    var utils = {
        showError:function(element,value) {
            element.parent().find('.error').text(value).show();
        },
        hideError:function(element,value) {
            element.parent().find('.error').hide();
        },
        validLen:function(element,num) {
            var len=element.val().split('').length;
            if(len<=num && len>0) {
                this.hideError(element);
            }
            else {
                var errorMsg='请描述您的问题';

                if(len>num) {
                    errorMsg='您的问题不能超过'+num+'个字符'
                }
                this.showError(element,errorMsg);
                tagValid=false;

            }
        },
        radioChecked:function(element) {
            var checkLen=$('input.tag:checked').length;

            if(checkLen>0) {
                this.hideError(element);
                if(checkLen>3) {
                   element.prop('checked',false);
                }
            }
            else {
                this.showError(element)
                tagValid=false;
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
                default:
                    return utils.radioChecked($ele)
                    break;
            }

        });
    };

    $formQuestion.find('input').on('blur change',function(event) {
        $(this).checkFrom();
    });

    $formSubmit.on('click',function() {
        $formQuestion.find('input').checkFrom();

        if(tagValid) {
            //var data=$formQuestion.serialize();
            var serializeData=$formQuestion.serializeArray(),
                jsonData=comm.serializeObject(serializeData),
                jsnData;
            //jsnData=JSON.stringify(jsonData);
            $.ajax({
                url: "/question",
                //data: {"question":"oop","addition":"oop","tags":"SECURITIES","captcha":"123456"},
                data: '{"question":"oop","addition":"oop","tags":"SECURITIES","captcha":"123456"}',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function(resData) {
                    console.log(resData);
                })
                .fail(function(data) {
                    console.log(data);
                });
        }

    });
}

