function menuCommon(optionInit) {
    let defaultOption = {
        menuCurrentClass:'current',
        menuTag:'span',
        pageDom:$('body'),
        contentClass:'overview-content'
    };

    let option = $.extend({},defaultOption,optionInit);

    option.pageDom.find('.menu-category span').on('click',function() {
        let $this = $(this),
            index = $this.index();

        $this.addClass(option.menuCurrentClass).siblings(option.menuTag).removeClass(option.menuCurrentClass);

        option.pageDom.find('.'+option.contentClass).eq(index).show().siblings('.'+option.contentClass).hide();
    });

}

module.exports = menuCommon;