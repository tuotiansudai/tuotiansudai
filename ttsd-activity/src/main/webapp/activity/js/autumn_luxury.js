/**
 * [name]:奢侈品专题页
 * [user]:xuqiang
 * [date]:2016-08-24
 */
require(['jquery', 'layerWrapper', 'template', 'jquery.ajax.extension', 'circle','commonFun','register_common'], function ($, layer, tpl) {
    $(function () {
        if ($(window).width() < 700) {
            $('.product-img').each(function (index, el) {
                $(this).find('img').height($(this).siblings('.product-info').height());
            });
        }
        var browser = commonFun.browserRedirect();
        if (browser == 'mobile') {

            var urlObj=commonFun.parseURL(location.href);
            if(urlObj.params.tag=='yes') {
                $('.reg-tag-current').show();
            }
        }
        var scrollTimer;
        $("#rewardList").hover(function () {
            clearInterval(scrollTimer);
        }, function () {
            scrollTimer = setInterval(function () {
                scrollNews($("#rewardList"));
            }, 1000);
        }).trigger("mouseout");

        function scrollNews(obj) {
            var $self = obj.find(".user-list");
            var lineHeight = $self.find("tr:first").height();
            if ($self.find('tr').length > 5) {
                $self.animate({
                    "margin-top": -lineHeight + "px"
                }, 600, function () {
                    $self.css({
                        "margin-top": "0px"
                    }).find("tr:first").appendTo($self);
                })
            }
        }

        $('.record-list h3 span').on('click', function (event) {
            var $self = $(this),
                $item = $self.parent('h3').siblings('.record-item'),
                index = $self.index();
            $self.addClass('active').siblings('span').removeClass('active');
            $item.find('.record-data:eq(' + index + ')').addClass('active')
                .siblings('.record-data').removeClass('active');
        });

        var $loginName = $('div.login-name');
        var loginName = $loginName ? $loginName.data('login-name') : '';

        $("a.autumn-luxury-invest-channel").click(function () {
            $.ajax({
                url: '/activity/autumn/luxury/invest?loginName=' + loginName,
                type: 'POST'
            });
        });
    });
});