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
