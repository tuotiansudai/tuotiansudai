require(['jquery', 'bootstrap', 'Validform', 'Validform_Datatype', 'jquery-ui', 'csrf', 'layerWrapper'], function ($) {
    $(function () {
        var $body = $('body'),
            $confirmBtn = $('.confirm-btn'),
            $bannerDeactivated = $('.banner-deactivated'),
            $tooltip = $('.add-tooltip'),
            $bannerDelete = $('.banner-delete'),
            $tipCom = $('.tip-container');

        $('.bannerAD').click(function () {
            window.location.href = '/banner-manage/create';
        });

        $bannerDelete.on('click', function () {
            var $self = $(this),
                thisLink = $self.attr('data-link');
            if (!confirm("是否确认删除?")) {
                return;
            } else {
                $.ajax({
                    url: thisLink,
                    type: 'DELETE',
                    dataType: 'json'
                })
                    .done(function (res) {
                        if (res.data.status) {
                            window.location.href = '/banner-manage/list';
                        }
                    })
                    .fail(function (res) {
                        console.log("fail");
                    });
            }
        });

        var webShowLayer = function (src) {
            layer.open({
                type: 1,
                shade: false,
                title: false,
                area: ['1024px', '350px'], //宽高
                content: '<img src =' + src + '>'
            });
        }

        var appShowLayer = function (src) {
            layer.open({
                type: 1,
                shade: false,
                title: false,
                area: ['750px', '340px'], //宽高
                content: '<img src =' + src + '>'
            });
        }

        $('.webImg').click(function () {
            webShowLayer($(this).find('img').attr('src'));
        });

        $('.appImg').click(function () {
            appShowLayer($(this).find('img').attr('src'));
        });

    });
})