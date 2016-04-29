; (function ($) {
    jQuery.pageflip = {

        paging: function (recordCount,option) {

            var pageIndex = $.pageflip.defaultVal.pageIndex;
            var obj = $.extend($.pageflip.defaultVal,option);
            var _page = $(obj.PagePosition),
                        show_indexStart,
                        show_End,
                        omit = [],
                        txtPage = $("<input type='text' style='width:30px; margin-left:10px; margin-right:10px;'/>"),
                        btnPage = $('<input type="button" value="Go"/>'),
                        pageCount = Math.floor(recordCount / obj.pageSize) + (recordCount % obj.pageSize == 0 ? 0 : 1);

            if (pageCount > 0) {
                _page.show();
            } else {
                _page.hide();
            }

            _page.empty();

            var _a = (function () {
                var links = {};
                $.each(obj.pageLocalized, function (key, value) {
                    var _pageIndex = pageIndex;
                    switch (key) {
                        case 'first':
                            _pageIndex = 1;
                            break;
                        case 'prev':
                            _pageIndex = _pageIndex - 1;
                            break;
                        case 'next':
                            _pageIndex = _pageIndex + 1;
                            break;
                        case 'last':
                            _pageIndex = pageCount;
                            break;
                        default:
                            break;
                    }
                    links[key] = $("<a href='javascript:void(0);' class='jp-" + key + "'>" + value + "</a>").appendTo(_page).click(function () {
                        obj.pageIndex = pageIndex = _pageIndex;
                        obj.PageCallback();
                    });
                });
                return links;
            })();

            //手动跳转
            btnPage.click(function () {
                var _pageindex = parseInt($.trim(txtPage.val()));
                if (!!_pageindex && _pageindex <= pageCount) {
                  //  pageIndex = _pageindex;
                    $.pageflip.defaultVal.pageIndex=_pageindex;
                    obj.PageCallback();
                }
            });

            _page.append(txtPage).append(btnPage);

            if (pageIndex == 1) {
                _a["first"].add(_a["prev"]).addClass("jp-disabled").unbind("click");
            }
            if (!pageCount || pageIndex == pageCount) {
                _a["next"].add(_a["last"]).addClass("jp-disabled").unbind("click");
            }

            if (pageIndex <= 4) {
                show_indexStart = 2;
                show_End = 6;
                omit.push(show_End);
            } else if (pageIndex > pageCount - 4) {
                show_indexStart = pageCount - 5;
                show_End = pageCount - 1;
                omit.push(show_indexStart - 1);
            } else {
                show_indexStart = pageIndex - 2;
                show_End = pageIndex + 2;
                omit.push(show_indexStart - 1);
                omit.push(show_End);
            }

            for (var i = 1, length = pageCount; i <= length; i++) {
                var _i = i,
                        __a = $("<a href='javascript:void(0);'>" + i + "</a>").insertBefore(_page.find("a.jp-next")).click(function () {
                            obj.pageIndex = pageIndex = parseInt(this.innerHTML);
                            obj.PageCallback();
                        });

                if (i == pageIndex) {
                    __a.addClass("jp-current").unbind("click");
                }

                if (i != 1 && i != length) {
                    if (i < show_indexStart || i > show_End) {
                        __a.addClass("jp-hidden");
                    }
                    if ($.inArray(i, omit) > -1) {
                        $("<span>...</span>").insertAfter(__a);
                    }
                }
            }


        },
        pagingOther: function (recordCount,option) {

            var pageIndex = $.pageflip.defaultVal.pageIndex2;
            var obj = $.extend($.pageflip.defaultVal,option);
            var _page = $(obj.PagePosition),
                        show_indexStart,
                        show_End,
                        omit = [],
                        txtPage = $("<input type='text' style='width:30px; margin-left:10px; margin-right:10px;'/>"),
                        btnPage = $('<input type="button" value="Go"/>'),
                        pageCount = Math.floor(recordCount / obj.pageSize) + (recordCount % obj.pageSize == 0 ? 0 : 1);

            if (pageCount > 0) {
                _page.show();
            } else {
                _page.hide();
            }

            _page.empty();

            var _a = (function () {
                var links = {};
                $.each(obj.pageLocalized, function (key, value) {
                    var _pageIndex = pageIndex;
                    switch (key) {
                        case 'first':
                            _pageIndex = 1;
                            break;
                        case 'prev':
                            _pageIndex = _pageIndex - 1;
                            break;
                        case 'next':
                            _pageIndex = _pageIndex + 1;
                            break;
                        case 'last':
                            _pageIndex = pageCount;
                            break;
                        default:
                            break;
                    }
                    links[key] = $("<a href='javascript:void(0);' class='jp-" + key + "'>" + value + "</a>").appendTo(_page).click(function () {
                        obj.pageIndex2 = pageIndex = _pageIndex;
                        obj.PageCallbackOther();
                    });
                });
                return links;
            })();

            //手动跳转
            btnPage.click(function () {
                var _pageindex = parseInt($.trim(txtPage.val()));
                if (!!_pageindex && _pageindex <= pageCount) {
                  //  pageIndex = _pageindex;
                    $.pageflip.defaultVal.pageIndex2=_pageindex;
                    obj.PageCallbackOther();
                }
            });

            _page.append(txtPage).append(btnPage);

            if (pageIndex == 1) {
                _a["first"].add(_a["prev"]).addClass("jp-disabled").unbind("click");
            }
            if (!pageCount || pageIndex == pageCount) {
                _a["next"].add(_a["last"]).addClass("jp-disabled").unbind("click");
            }

            if (pageIndex <= 4) {
                show_indexStart = 2;
                show_End = 6;
                omit.push(show_End);
            } else if (pageIndex > pageCount - 4) {
                show_indexStart = pageCount - 5;
                show_End = pageCount - 1;
                omit.push(show_indexStart - 1);
            } else {
                show_indexStart = pageIndex - 2;
                show_End = pageIndex + 2;
                omit.push(show_indexStart - 1);
                omit.push(show_End);
            }

            for (var i = 1, length = pageCount; i <= length; i++) {
                var _i = i,
                        __a = $("<a href='javascript:void(0);'>" + i + "</a>").insertBefore(_page.find("a.jp-next")).click(function () {
                            obj.pageIndex2 = pageIndex = parseInt(this.innerHTML);
                            obj.PageCallbackOther();
                        });

                if (i == pageIndex) {
                    __a.addClass("jp-current").unbind("click");
                }

                if (i != 1 && i != length) {
                    if (i < show_indexStart || i > show_End) {
                        __a.addClass("jp-hidden");
                    }
                    if ($.inArray(i, omit) > -1) {
                        $("<span>...</span>").insertAfter(__a);
                    }
                }
            }


        }
    };

    $.pageflip.defaultVal = {
        pageIndex: 1,
        pageSize: 10,
        PageCallback: "",
        PageCallbackOther: "",
        PagePosition: "",
        pageLocalized: {
            first: "<<",
            prev: "<",
            next: ">",
            last: ">>"
        }
    };

})(jQuery);

  
