require(['jquery', 'underscore', 'template', 'mustache', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'fileinput', 'fileinput_locale_zh', 'Validform', 'Validform_Datatype', 'csrf','layer'],
    function ($, _, template) {
        $('.selectpicker').selectpicker();

        var fundraisingStartTimeElement = $('#fundraisingStartTime');
        var fundraisingEndTimeElement = $('#fundraisingEndTime');
        fundraisingStartTimeElement.datetimepicker({
            format: 'YYYY-MM-DD HH:mm'
        });
        fundraisingEndTimeElement.datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            useCurrent: false

        });

        fundraisingStartTimeElement.on("dp.change", function (e) {
            fundraisingEndTimeElement.data("DateTimePicker").minDate(e.date);
        });
        fundraisingEndTimeElement.on("dp.change", function (e) {
            fundraisingStartTimeElement.data("DateTimePicker").maxDate(e.date);
        });

        initData();
        //风控信息
        function initData() {
            $.ajax({
                url: "/loan-application/" + $('#loanApplicationId').val() + "/titles",
                type: 'GET'
            }).done(function (data) {
                console.log(data);

                var wind_control_temp='';
                for(var i=0;i<data.length;i++){
                    if(data[i].title=="共同借款人"){
                        wind_control_temp+=`
                    <div class="form-group">
                        <label class="col-sm-2 control-label">
                            <input name="${data[i].id}" type="checkbox" ${data[i].checked? 'checked' : ''}  data-id="${data[i].id}" data-title="${data[i].title}"
                                   > 共同借款人
                        </label>

                        <label class="col-sm-1 control-label">
                            姓名
                        </label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" name='username' value="${data[i].details && data[i].details.length > 0 ? data[i].details[0] : ''}">
                        </div>
                        <label class="col-sm-1 control-label" style="width:110px">
                            身份证号
                        </label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" minlength="15" maxlength="18" name='pid' value="${data[i].details && data[i].details.length > 1 ? data[i].details[1] : ''}">
                        </div>
                    </div>`
                    }else{
                        var temp_item = '<ul class="img_list">';
                        for(var j=0;j<data[i].details.length;j++){
                            temp_item+=`
                        <li class="img_item item_small">
                            <i class="item_small_i" data-num="${j}" data-del="${data[i].id}">❎</i>
                            <img src="${data[i].details[j]}" alt="" class="item_small_img">
                        </li>
                    `
                        }
                        temp_item+='</ul>';

                        wind_control_temp += `
                            <div class="form-group">
                                <label class="col-sm-2 control-label">
                                    <input name="${data[i].id}" type="checkbox" ${data[i].checked ? 'checked' : ''} data-title="${data[i].title}" data-id="${data[i].id}"
                                       value="${data[i].details.join(',')}"> ${data[i].title}
                                </label>

                                <input type="file" style="display: none" id="window_${i}" data-name="${data[i].id}">
                                ${temp_item}
                                <div class="col-sm-1 btn_container">
                                    <button class="btn btn-primary" onclick="$('#window_${i}').click()" type='button'>上传</button>
                                </div>
                            </div>
                        `}
                }
                $('#wind_control').html(wind_control_temp)
            });
        }

        // 上传图片
        $('body').on('change','input[type="file"]',function(e){
            var file = e.target.files[0]
            var id = e.target.id;
        //创建formdata对象
            var formdata = new FormData();
            formdata.append("upfile",file);
            var name = $(this).data('name');
            var detail = $('input[name="'+name+'"]');
            var detail_arr = detail.val()===null || detail.val()==='' ? [] : detail.val().split(',');

            $.ajax({
                url: "/ueditor?action=uploadimage",
                type: 'POST',
                dataType: 'json',
                data: formdata,
                processData: false,
                contentType: false
            }).done(function (data) {
                detail_arr.push(data.url);
                detail.val(detail_arr.join(','));
                $('#'+id).next().append('<li class="img_item item_small"><i class="item_small_i" data-num="'+(detail_arr.length-1)+'" data-del="'+name+'">❎</i><img src="' + data.url + '"class="item_small_img" alt=""></li>')})
        });
        // 删除图片
        $('body').on('click', '.item_small_i', function () {
            var name = $(this).data('del');
            var num = $(this).data('num');
            var detail =$('input[name="'+name+'"]');
            var detail_arr = detail.val().split(',');
            detail_arr.splice(num,1);
            detail.val(detail_arr.join(','));
            $(this).parent().remove();
        });
        // 查看大图
        $('body').on('click', '.item_small_img', function (e) {
            var src = e.target.src
            layer.open({
                type: 1,
                closeBtn: 0, //不显示关闭按钮
                anim: 4,
                title:false,
                shadeClose: true, //开启遮罩关闭
                content: '<img src="'+src+'" style="width:100%">',
                area: ['500px'],
            });

        });
        // 添加风控信息title
        $('#add_wind_input').click(function(){
            $('#add_input').show();
        })
        $('#add_wind_control').click(function(){
            $('#add_input').hide();
            var value=$('#add_input').find('input').val();
            var length=$('#wind_control').children().length;
            console.log(value);

            $.ajax({
                url: "/loan-application/title?title=" + value,
                type: 'POST',
                dataType: 'json',
                processData: false,
                contentType: 'application/json; charset=UTF-8'
            }).done(function (data) {
                $('#wind_control').append(`
                          <div class="form-group">
                                <label class="col-sm-2 control-label">
                                    <input name="${data.id}" type="checkbox" data-title="${data.title}" data-id="${data.id}"> ${data.title}
                                </label>

                                <input type="file" style="display: none" id="window_${length}" data-name="${data.id}">
                                <div class="col-sm-1 btn_container">
                                    <button class="btn btn-primary" onclick="$('#window_${length}').click()" type='button'>上传</button>
                                </div>
                          </div>
                        `);
            });
        });

        $('#form-save-btn').click(function(e){
            e.preventDefault();
            var senddata={}
            senddata.address = $('input[name="address"]').val();
            senddata.loanUsage = $('input[name="loanUsage"]').val();
            var ele_checked =  $('#wind_control').find('input:checked');
            console.log(ele_checked.length)
            senddata.relationModels=[]
            for(var key=0;key<ele_checked.length;key++){
                console.log(key)
                // console.log(ele_checked[key].value)
                if($(ele_checked[key]).data('title')=='共同借款人'){
                    senddata.relationModels.push({
                        loanApplicationId:'',
                        titleId:$(ele_checked[key]).data('id'),
                        detail:$('input[name="username"]').val() + "," + $('input[name="pid"]').val()
                    })
                }else{
                    var detail = ele_checked[key].value;
                    senddata.relationModels.push({
                        loanApplicationId:'',
                        titleId:$(ele_checked[key]).data('id'),
                        detail:detail
                    })
                }
            }

            console.log(senddata);

            $.ajax({
                url: $(this).data('url'),
                data: JSON.stringify(senddata),
                type: 'POST',
                contentType: "application/json"
            }).done(function (data) {
                if (data.data.status) {
                    location.href = '/loan-application/consume-list';
                } else {
                    alert('保存失败');
                }
            });

        });

        $('#form-refuse-btn').click(function () {
            $.ajax({
                url: $(this).data('url'),
                type: 'POST'
            }).done(function (data) {
                if (data.data.status) {
                    location.href = '/loan-application/consume-list';
                } else {
                    alert('驳回失败');
                }
            });
        });

        $('#form-submut-audit-btn').click(function () {
            $.ajax({
                url: $(this).data('url'),
                type: 'POST'
            }).done(function (data) {
                if (data.data.status) {
                    location.href = '/loan-application/consume-list';
                } else {
                    alert('提交审核失败');
                }
            });
        });

        $('#form-approve-btn').click(function () {
            $.ajax({
                url: $(this).data('url'),
                type: 'POST'
            }).done(function (data) {
                if (data.data.status) {
                    location.href = '/loan-application/consume-list';
                } else {
                    alert('审核失败');
                }
            });
        });

    });

/**
 * Created by qiqiannan on 2018/12/21.
 */
