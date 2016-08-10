/* create question */
var $createQuestion=$('#createQuestion');
if ($createQuestion.length) {
    //return true;
}
var $formQuestion=$('.form-question',$createQuestion),
    $question=$('.ask-con',$createQuestion),
    $addition=$('.addition',$createQuestion),
    $captcha=$('.captcha',$createQuestion),
    $formSubmit=$('.formSubmit',$formQuestion);

var utils = {
    showError:function(element) {
        element.next('error').show();
    },
    hideError:function(element) {
        element.next('error').hide();
    },
    validLen:function(element,num) {
        var len=element.val().split('').length;
        if(len<=num && len>=0) {
            _.partial(this.hideError,element);
            return true;
        }
        else {
            _.partial(this.showError,element);
            return false;

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
                _.partial(utils.validLen,$ele,30);
                break;
            case 'addition':
                _.partial(utils.validLen,$ele,10000);
                break;
            case 'captcha':
                _.partial(utils.validLen,$ele,5);
                break;
        }

    });

};

$formQuestion.find('input').checkFrom();

$formSubmit.on('click',function() {
    var data=$formQuestion.serialize();
    $.ajax({
        type: "GET",
        url: "test.json",
        data: data,
        dataType: "json",
        beforeSend:function ( xhr ) {

        }
    })
        .done(function(resData) {
            console.log(resData);
        })
        .fail(function(data) {
            console.log(data);
        });
});
