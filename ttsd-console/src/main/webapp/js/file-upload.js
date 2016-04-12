/**
 * Created by belen on 15/8/17.
 */
$(function () {
    /*

     API_SELECT : 数据请求地址申请材料标题
     uploadUrl : 图片存放地址，可忽略
     json数据格式参考 select.json 文件
     name 跟 txt 属性名字变化，请跟随替换
     84-88 行代码

     */

    var _html = '';
    var data = '';
    //初始化数据
    $.get(API_SELECT, function (data) {
        data = data;
        _html = template('upload', data);
    })
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
                    data = data;
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






})