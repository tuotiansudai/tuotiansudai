<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="consumeDetail.js" headLab="project-manage" sideLab="start" title="借款申请信息">
<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal jq-form" id="formDom">

        <section id="section-four">
            <h3><span>借款人基本信息</span></h3>
            <hr class="top-line">
            <div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人性别: </label>

                    <div class="col-sm-8 font_mid">
                    <#--${loan.loanerDetails.gender='MALE'?'男':'女'}-->
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人年龄: </label>

                    <div class="col-sm-8 font_mid">
                    <#--${loan.loanerDetails.age}-->
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人身份证号: </label>

                    <div class="col-sm-8 font_mid">
                    <#--${loan.loanerDetails.identityNumber}-->
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人婚姻情况: </label>

                    <div class="col-sm-8 font_mid">
                        <#--${loan.loanerDetails.marriage='UNMARRIED'?'未婚':loan.loanerDetails.marriage='MARRIED'?:'已婚':loan.loanerDetails.marriage='DIVORCE'?'离异':'不明'}-->
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人所在地区: </label>

                    <div class="col-sm-3">
                        <#--<input name="region" value="${loan.loanerDetails.region}" type="text" class="form-control" datatype="*" errormsg="借款人所在地区不能为空">-->
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人家庭年收入（元）: </label>

                    <div class="col-sm-8 font_mid">
                    <#--${loan.loanerDetails.income}-->
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人从业情况: </label>

                    <div class="col-sm-8 font_mid">
                    <#--${loan.loanerDetails.employmentStatus}-->
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">芝麻信用分: </label>

                    <div class="col-sm-8 font_mid">
                        开发工程师
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款用途: </label>

                    <div class="col-sm-3">
                        <#--<input name="purpose" value="${(loan.loanerDetails.purpose)!}" type="text" maxlength="6" class="form-control" datatype="*" errormsg="借款用途不能为空">-->
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">其他资产: </label>

                    <div class="col-sm-8 font_mid">
                        开发工程师
                    </div>
                </div>

            </div>
        </section>

        <section id="section-four">
            <h3><span>证明材料</span></h3>
            <hr class="top-line">
            <div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">身份信息认证: </label>

                    <ul class="col-sm-8 img_list">
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">收入认证: </label>

                    <ul class="col-sm-8 img_list">
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">信用报告: </label>

                    <ul class="col-sm-8 img_list">
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">婚姻状况证明: </label>

                    <ul class="col-sm-8 img_list">
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">资产证明: </label>

                    <ul class="col-sm-8 img_list">
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">资产认证: </label>

                    <ul class="col-sm-8 img_list">
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                    </ul>
                </div>


                <div class="form-group">
                    <label class="col-sm-2 control-label">共同借款人: </label>

                    <div class="col-sm-8 font_mid">
                        李四
                    </div>
                </div>

                <div class="form-group">

                    <ul class="col-sm-8 img_list col-lg-offset-2">
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                        <li class="img_item">
                            <img src="" alt="">
                        </li>
                    </ul>
                </div>

            </div>
        </section>

        <section id="section-five">
            <h3><span>风控信息</span></h3>
            <hr class="top-line">
            <div class="wind_control" id="wind_control">
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                            <input name="estimate" type="radio"
                                   value="radio_1"> 手机认证
                    </label>

                    <input type="file" style="display: none" id="window_1" data-value="">
                    <ul class="img_list">
                        <li class="img_item item_small">
                            <i class="item_small_i">❎</i>
                            <img src="https://www.yasuotu.com/img/question-6.jpg" alt="" class="item_small_img">
                        </li>
                        <li class="img_item item_small">
                            <i class="item_small_i">❎</i>
                            <img src="https://www.yasuotu.com/img/question-6.jpg" class="item_small_img" alt="">
                        </li>
                    </ul>
                    <div class="col-sm-1 btn_container">
                        <button class="btn btn-primary" onclick="$('#window_1').click()">上传</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <input name="estimate" type="radio"
                               value="radio_2"> 收入认证
                    </label>
                    <input type="file" style="display: none" id="window_2" data-value="">
                    <ul class="img_list">

                    </ul>
                    <div class="col-sm-1 btn_container">
                        <button class="btn btn-primary" onclick="$('#window_2').click()">上传</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <input name="estimate" type="radio"
                               value="radio_3"> 信用报告
                    </label>

                    <input type="file" style="display: none" id="window_3" data-value="">
                    <ul class="img_list">

                    </ul>
                    <div class="col-sm-1  btn_container">
                        <button class="btn btn-primary" onclick="$('#window_3').click()">上传</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <input name="estimate" type="radio"
                               value="radio_4"> 资产认证
                    </label>

                    <input type="file" style="display: none" id="window_4" data-value="">
                    <ul class="img_list">

                    </ul>
                    <div class="col-sm-1  btn_container">
                        <button class="btn btn-primary" onclick="$('#window_4').click()">上传</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <input name="estimate" type="radio"
                               value="radio_5"> 身份信息认证
                    </label>

                    <input type="file" style="display: none" id="window_5" data-value="">
                    <ul class="img_list">

                    </ul>
                    <div class="col-sm-1  btn_container">
                        <button class="btn btn-primary" onclick="$('#window_5').click()">上传</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <input name="estimate" type="radio"
                               value="radio_6"> 婚姻状况认证
                    </label>

                    <input type="file" style="display: none" id="window_6" data-value="">
                    <ul class="img_list">

                    </ul>
                    <div class="col-sm-1  btn_container">
                        <button class="btn btn-primary" onclick="$('#window_6').click()">上传</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <input name="estimate" type="radio"
                               value="radio_7"> 住址信息认证
                    </label>

                    <input type="file" style="display: none" id="window_7" data-value="">
                    <ul class="img_list">

                    </ul>
                    <div class="col-sm-1  btn_container">
                        <button class="btn btn-primary" onclick="$('#window_7').click()">上传</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <input name="estimate" type="radio"
                               value="radio_8"> 工作信息认证
                    </label>

                    <input type="file" style="display: none" id="window_8" data-value="">
                    <ul class="img_list">

                    </ul>
                    <div class="col-sm-1  btn_container">
                        <button class="btn btn-primary" onclick="$('#window_8').click()">上传</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <input name="estimate" type="radio"
                               value="radio_9"> 共同借款人
                    </label>

                    <label class="col-sm-1 control-label">
                        姓名
                    </label>
                    <div class="col-sm-2">
                        <input type="text" class="form-control">
                    </div>
                    <label class="col-sm-1 control-label" style="width:110px">
                        身份证号
                    </label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control">
                    </div>
                </div>

            </div>
            <div class="form-group" id="add_input" style="display: none">
                <label class="col-sm-3 control-label">
                    <input type="text" class="form-control">
                </label>

                <label class="col-sm-1 control-label">
                    <button class="btn btn-primary" style="margin-top:0px" id="add_wind_control">保存</button>
                </label>
            </div>
            <button class="btn btn-primary" id="add_wind_input" style="margin-top:30px">添加</button>
        </section>

        <div class="form-group">
            <label class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn form-submit-btn btn-primary" data-url="/project-manage/loan/create">
                    保存
                </button>
            </div>
        </div>
    </form>
    <!-- Modal -->
    <div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <h5>确认提交？</h5>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-default btn-submit">确认</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="pledge-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <h5>抵押物信息最多添加4项</h5>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>


</div>
<!-- content area end -->

</@global.main>
