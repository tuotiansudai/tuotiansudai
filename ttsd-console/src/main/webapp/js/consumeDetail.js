require(['jquery', 'underscore', 'template', 'mustache', 'text!/tpl/loaner-details.mustache', 'text!/tpl/loaner-enterprise-details.mustache', 'text!/tpl/pledge-house.mustache', 'text!/tpl/pledge-vehicle.mustache', 'text!/tpl/pledge-enterprise.mustache', 'text!/tpl/loan-extra-rate.mustache', 'text!/tpl/loan-title-template.template', 'text!/tpl/loan-title-select-template.template', 'text!/tpl/loaner-enterprise-info.mustache', 'text!/tpl/loaner-enterprise-factoring-info.mustache', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'fileinput', 'fileinput_locale_zh', 'Validform', 'Validform_Datatype', 'csrf','layer'],
    function ($, _, template, Mustache, loanerDetailsTemplate, loanerEnterpriseDetailsTemplate, pledgeHouseTemplate, pledgeVehicleTemplate, pledgeEnterpriseTemplate, loanExtraRateTemplate, loanTitleTemplate, loanTitleSelectTemplate, loanerEnterpriseInfoTemplate, loanerEnterpriseFactoringInfoTemplate) {
        var loanParam = ['id', 'name', 'agent', 'productType', 'pledgeType', 'loanType', 'pledgeType', 'activityType',
            'loanAmount', 'baseRate', 'activityRate', 'originalDuration', 'minInvestAmount', 'maxInvestAmount', 'investIncreasingAmount',
            'fundraisingStartTime', 'fundraisingEndTime', 'deadline', 'contractId', 'status'];

        var loanDetailsParam = ['declaration', 'extraRateRuleIds', 'extraSource', 'activity', 'activityDesc', 'nonTransferable', 'disableCoupon', 'pushMessage', 'grantReward', 'rewardRate', 'estimate', 'introduce'];

        var loanerDetailsParam = ['userName', 'identityNumber', 'gender', 'age', 'marriage', 'region', 'income', 'employmentStatus', 'purpose', 'source'];

        var loanerEnterpriseDetailsParam = ['juristicPerson', 'shareholder', 'address', 'purpose', 'source'];

        var loanerEnterpriseInfoParam = ['companyName', 'address', 'purpose', 'source'];

        var loanerEnterpriseFactoringInfoParam = ['factoringCompanyName', 'factoringCompanyDesc'];

        var pledgeHouseParam = ['pledgeLocation', 'estimateAmount', 'pledgeLoanAmount', 'square', 'propertyCardId', 'propertyRightCertificateId', 'estateRegisterId', 'authenticAct'];

        var pledgeVehicleParam = ['pledgeLocation', 'estimateAmount', 'pledgeLoanAmount', 'brand', 'model'];

        var pledgeEnterpriseParam = ['pledgeLocation', 'estimateAmount', 'guarantee'];

        var arrayParam = ['extraRateIds', 'extraSource'];

        var arrayPledgeParam = ['pledgeHouse', 'pledgeVehicle', 'pledgeEnterprise'];

        var loanIdElement = $('input[name="id"]');
        var loanNameElement = $('select[name="name"]'); //标的名称Element
        var loanTypeElement = $('select[name="loanType"]'); //标的类型Element
        var productTypeElement = $('select[name="productType"]'); //标的产品类型Element
        var pledgeTypeElement = $('input[name="pledgeType"]'); //标的抵押类型Element
        var extraElement = $('#extra'); //加息开关
        var extraRuleElement = $('.extra-rate'); //加息开关
        var extraSourceElement = $('.extra-source'); //extraSource
        var $formSubmitBtn = $('.form-submit-btn');
        var $currentFormSubmitBtn;
        var sectionOneElement = $('#section-one'); //项目信息Section
        var sectionTwoElement = $('#section-two'); //房产车辆抵押借款人信息Section 或 税易经营性借款企业借款人信息Section
        var sectionThreeElement = $('#section-three'); //抵押物信息信息Section
        var loanTitleTemplateHtml;
        var pledgeRadioCheckVehicle = $('#defaultPledgeRadioCheckVehicle').val() === 'true';

        //修改section
        var changeSection = function () {
            var loanName = loanNameElement.val();
            if ('房产抵押借款' === loanName) {
                pledgeTypeElement.val("HOUSE");
                sectionTwoElement.html(Mustache.render(loanerDetailsTemplate));
                sectionThreeElement.html("<div class='house-pledge'><h3><span class='house-title'>房产信息</span> <button type='button' class='jq-add-house-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>" + Mustache.render(pledgeHouseTemplate) + '</div>');
            }

            if ('车辆消费借款' === loanName) {
                pledgeTypeElement.val("VEHICLE");
                sectionTwoElement.html(Mustache.render(loanerDetailsTemplate));
                sectionThreeElement.html("<div class='vehicle-pledge'><h3><span class='vehicle-title'>车辆信息</span> <button type='button' class='jq-add-vehicle-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>" + Mustache.render(pledgeVehicleTemplate) + '</div>');
            }

            if ('个人资金周转' === loanName) {
                pledgeTypeElement.val("PERSONAL_CAPITAL_TURNOVER");
                pledgeRadioCheckVehicle = true;
                sectionTwoElement.html(Mustache.render(loanerDetailsTemplate));
                sectionThreeElement.html("<div class='vehicle-pledge'><h3><input type='radio' id='vehicleRadio' checked><span class='vehicle-title'>车辆信息</span> <button type='button' class='jq-add-vehicle-pledge btn btn-info' style='margin-left: 10px;'>+</button><input type='radio' id='houseRadio' style='margin-left: 150px;'>房产信息</h3>"  + Mustache.render(pledgeVehicleTemplate) + '</div>');
            }

            if ('经营性借款' === loanName && $('#projectName option:selected').attr('data-pledgetype') === 'ENTERPRISE_CREDIT') {
                pledgeTypeElement.val("ENTERPRISE_CREDIT");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseDetailsTemplate));
                sectionThreeElement.html(Mustache.render(''));
            }

            if ('经营性借款' === loanName && $('#projectName option:selected').attr('data-pledgetype') === 'ENTERPRISE_PLEDGE') {
                pledgeTypeElement.val("ENTERPRISE_PLEDGE");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseDetailsTemplate));
                sectionThreeElement.html("<div class='enterprise-pledge'><h3><span class='enterprise-title'>抵押物信息</span> <button type='button' class='jq-add-enterprise-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>" + Mustache.render(pledgeEnterpriseTemplate) + '</div>');
            }

            if ('经营性借款' === loanName && $('#projectName option:selected').attr('data-pledgetype') === 'ENTERPRISE_FACTORING') {
                pledgeTypeElement.val("ENTERPRISE_FACTORING");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseInfoTemplate));
                sectionThreeElement.html(Mustache.render(loanerEnterpriseFactoringInfoTemplate));
            }

            if ('经营性借款' === loanName && $('#projectName option:selected').attr('data-pledgetype') === 'ENTERPRISE_BILL') {
                pledgeTypeElement.val("ENTERPRISE_BILL");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseInfoTemplate));
                sectionThreeElement.html(Mustache.render(''));
            }

            $('.selectpicker').selectpicker();
        };

        if (!loanIdElement.val()) {
            changeSection();
        }

        //根据项目名称改变section
        loanNameElement.change(function () {
            changeSection();
            if (['_90', '_180', '_360'].indexOf(productTypeElement.val()) !== -1 && extraElement.is(':checked')) {
                checkedExtraRate();
            }
        });

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

        //初始化数据
        // $.get("/project-manage/loan/titles", function (data) {
        //     loanTitleTemplateHtml = template.compile(loanTitleTemplate)({_data: data});
        //
        //     if (loanIdElement.val()) {
        //         $.each(rereq, function (index, item) {
        //             var uploadBox = $('.upload-box').append(loanTitleTemplateHtml);
        //             $(".file-loading").fileinput({
        //                 language: "zh",
        //                 uploadUrl: "/upload",
        //                 showUpload: true,
        //                 dropZoneTitle: '选择图片文件到这里 &hellip;',
        //                 initialPreviewShowDelete: true,
        //                 allowedFileExtensions: ["jpg", "png", "gif", "jpeg"],
        //                 initialPreview: item
        //             });
        //             $('.selectpicker').selectpicker({
        //                 style: 'btn-default',
        //                 size: 8
        //             });
        //
        //             $('.loan-title-selector', uploadBox.children().last()).selectpicker('val', index);
        //             $('select.loan-title-selector').last().parent().find('input.jq-txt').val(index);
        //         });
        //     }
        // });

        //风控信息
        // $.get("/", function (data) {
        //     var wind_control_temp='';
        //     for(var i=0;i<data.length;i++){
        //
        //         if(data[i].title=="共同借款人"){
        //             wind_control_temp+=`
        //             <div class="form-group">
        //                 <label class="col-sm-2 control-label">
        //                     <input name="${data[i].id}" type="checkbox" checked="${data[i].checked}"  data-id="${data[i].id}" data-title="${data[i].title}"
        //                            > 共同借款人
        //                 </label>
        //
        //                 <label class="col-sm-1 control-label">
        //                     姓名
        //                 </label>
        //                 <div class="col-sm-2">
        //                     <input type="text" class="form-control" name='username'>
        //                 </div>
        //                 <label class="col-sm-1 control-label" style="width:110px">
        //                     身份证号
        //                 </label>
        //                 <div class="col-sm-3">
        //                     <input type="text" class="form-control" name='pid'>
        //                 </div>
        //             </div>`
        //         }else{
        //             var temp_item += '<ul class="img_list">'
        //             for(var j=0;j<data[i].detail.length;j++){
        //                 temp_item+=`
        //                 <li class="img_item item_small">
        //                     <i class="item_small_i" data-num="${j}" data-del="${data[i].id}">❎</i>
        //                     <img src="${data[i].detail[j]}" alt="" class="item_small_img">
        //                 </li>
        //             `
        //             }
        //             temp_item+='</ul>'
        //             wind_control_temp+=`
        //             <div class="form-group">
        //                 <label class="col-sm-2 control-label">
        //                         <input name="${data[i].id}" type="checkbox" checked="${data[i].checked}" data-title="${data[i].title}" data-id="data[i].id"
        //                                > ${data[i].title}
        //                 </label>
        //
        //                 <input type="file" style="display: none" id="window_${i}" data-name="${data[i].id}">
        //                 ${temp_item}
        //                 <div class="col-sm-1 btn_container">
        //                     <button class="btn btn-primary" onclick="$('#window_${i}').click()" type='button'>上传</button>
        //                 </div>
        //             </div>`
        //         }
        //     }
        //     $('#wind_control').html(wind_control_temp)
        // });

        // 上传图片
        $('body').on('change','input[type="file"]',function(e){
            console.log(e)
            var file = e.target.files[0]
            var id = e.target.id
//        //创建formdata对象
            var formdata = new FormData();
            formdata.append("upfile",file);
            // $.ajax({
            //     url: "/",
            //     type: 'POST',
            //     dataType: 'json',
            //     data: formdata,
            //     processData: false,
            //     contentType: false
            // }).done(function (data) {
            //     console.log(data)
            // })
            var name = $(this).data('name')
            var detail =$('input[name="'+name+'"]')
            var detail_arr = detail.val().split(',')
            console.log(detail_arr)
            detail_arr.push('https://www.yasuotu.com/img/question-'+detail_arr.length+1+'.jpg')
            detail.val(detail_arr.join(','))
            console.log(detail_arr)
            $('#'+id).next().append('<li class="img_item item_small"><i class="item_small_i" data-num="'+(detail_arr.length-1)+'" data-del="'+name+'">❎</i><img src="https://www.yasuotu.com/img/question-6.jpg" class="item_small_img" alt=""></li>')
        })
        // 删除图片
        $('body').on('click', '.item_small_i', function () {
            var name = $(this).data('del')
            var num = $(this).data('num')
            var detail =$('input[name="'+name+'"]')
            var detail_arr = detail.val().split(',')
            console.log(detail_arr)
            detail_arr.splice(num,1)
            detail.val(detail_arr.join(','))
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
            var length=$('#wind_control').children().length
            // $.ajax({
            //     url: "/",
            //     type: 'POST',
            //     dataType: 'json',
            //     data: value,
            //     processData: false,
            //     contentType: 'application/json; charset=UTF-8'
            // }).done(function (data) {
            //     console.log(data)
            // })
            console.log(length)
            console.log(value)
            $('#wind_control').append(`<div class="form-group"><label class="col-sm-2 control-label">
                        <input name="${data.name}" type="checkbox"
                               > ${value}
                    </label>
                    <input type="file" style="display: none" id="window_${length}" data-name="${data.id}">
                    <ul class="img_list">

                    </ul>
                    <div class="col-sm-1 btn_container">
                        <button class="btn btn-primary" onclick="$('#window_${length}').click()">上传</button>
                    </div></div>`)
        })
        $('#form-save-btn').click(function(e){
            e.preventDefault();
            var senddata={}
            senddata.address = $('input[name="address"]').val()
            senddata.loanUsage = $('input[name="loanUsage"]').val()
            var ele_checked =  $('#wind_control').find('input:checked')
            console.log(ele_checked.length)
            senddata.relationModels=[]
            for(var key=0;key<ele_checked.length;key++){
                console.log(key)
                // console.log(ele_checked[key].value)
                if($(ele_checked[key]).data('title')=='共同借款人'){
                    senddata.relationModels.push({
                        loanApplicationId:' ',
                        titleId:$(ele_checked[key]).data('id'),
                        detail:[$('input[name="username"]').val(),$('input[name="pid"]').val()]
                    })
                }else{
                    var detail = ele_checked[key].value
                    senddata.relationModels.push({
                        loanApplicationId:' ',
                        titleId:$(ele_checked[key]).data('id'),
                        detail:detail
                    })
                }
            }
            // $.ajax({
            //     url: "/",
            //     type: 'POST',
            //     dataType: 'json',
            //     data: senddata,
            //     processData: false,
            //     contentType: 'application/json; charset=UTF-8'
            // }).done(function (data) {
            //     console.log(data)
            // })
        })
    });

/**
 * Created by qiqiannan on 2018/12/21.
 */
