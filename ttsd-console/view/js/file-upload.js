/**
 * Created by belen on 15/8/17.
 */
$(function () {
    //模拟json数据

    var data = {
        title: '数组',
        list: [
            {name: "idcard", txt: "身份证", img: []},
            {name: "room", txt: "房屋抵押", img: []},
            {name: "cars", txt: "汽车抵押", img: []}
        ]

    }
    var _html = template('upload', data);

    //添加申请材料
    $('.btn-upload').click(function () {
        $('.upload-box').append(_html);
        $(".file-loading").fileinput({
            language: "zh",
            uploadUrl: "/",
            showUpload: false,
            allowedFileExtensions: ["jpg", "png", "gif", "jpeg"]
        });
        $('.selectpicker').selectpicker({
            style: 'btn-default',
            size: 4
        });
    });

    //添加材料名称
    $('body').on('click', '.jq-add', function () {
        var _this = $(this);
        var txt = _this.siblings('.files-input').val();
        $.ajax({
            url: API_SELECT,
            type: 'POST',
            dataType: 'json',
            data: {txt: txt},
        })
            .done(function (data) {
                if (data.status) {
                    ajaxGet(API_SELECT, _this);
                } else {

                }
                console.log("success");
            })
            .fail(function () {
                console.log("error");
            })
            .always(function () {
                console.log("complete");
            });

    });


    // 渲染页面 slect ajax
    var ajaxGet = function (url, ele) {
        var url = url;
        var ele = ele;
        $.get(url, function (res) {
            if (res.status) {
                var ret = template('select', res);
                ele.siblings('.select-box').children().remove();
                ele.siblings('.select-box').append(ret);
                $('.selectpicker').selectpicker({
                    style: 'btn-default',
                    size: 4
                });
            }
        });
    }


    //选择下拉框赋值给input
    $('body').on('click', '.dropdown-menu li', function () {
        var _this = $(this);
        var txt = _this.find('.text').text();
        $.each(data.list, function (i, value) {
            if (value.txt == txt) {
                _this.closest('.col-file-box').find('.jq-txt').attr('name', value.name)
                    .val(txt);
                _this.closest('.col-file-box').find('.jq-src').attr('name', value.name + '_src');
            }

        })
    });


    // 循环上传图片分配对应位置
    var indexPic = function () {
        var _parent = $('.upload-box');
        var formGroup = _parent.find('.form-group');
        formGroup.each(function (index) {
            var str = [];
            var arr = formGroup.eq(index).find('.jq-src');
            if (formGroup.eq(index).find('.file-preview-frame').index()) {
                arr.val('');
                console.log(index+':'+arr.val())
            } else {
                formGroup.eq(index).find('.file-preview-frame').each(function (i) {
                    var _img = formGroup.eq(index).find('.file-preview-frame').eq(i).find('img').attr('title');
                    str.push(_img);
                    arr.val(str);
                    console.log(index+':'+arr.val())
                });
            }
        });
    };
    $('.jq-btn-form').click(function () {
        indexPic();
        var result = $('.form-horizontal').serialize();
        $.post(API_SELECT,result);
    })


})