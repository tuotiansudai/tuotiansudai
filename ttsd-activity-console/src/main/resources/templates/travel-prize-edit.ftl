<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="travel-prize.js" headLab="activity-manage" sideLab="" title="修改旅游奖品">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal travel-form" method="post" action="/activity-console/activity-manage/travel/edit">

            <div class="form-group">
                <label class="col-sm-2 control-label">奖品名称: </label>
                <div class="col-sm-4">
                    <input type="hidden" name="id" class="id" value="<#if dto??>${dto.id?c!}</#if>">
                    <input type="text" name="name" class="form-control travel-name" value="<#if dto??>${dto.name!}</#if>" placeholder="" datatype="*" errormsg="奖品名称不能为空">
                </div>
                <div class="col-sm-7">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">奖品价值: </label>
                <div class="col-sm-4">
                    <input type="text" name="price" value="<#if dto??>${dto.price!}</#if>" class="form-control travel-price" placeholder="" datatype="*" errormsg="奖品价值不能为空">
                </div>
                <div class="col-sm-7">

                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">奖品图片: </label>

                <div class="col-sm-4 ">
                    <input type="text" name="image" readonly value="<#if dto??>${dto.image!}</#if>" class="form-control travelImageUrl" placeholder="" datatype="*" errormsg="奖品图片不能为空">

                    <div class="image">
                        <#if dto??&&dto.image??>
                            <img style="width:100%" src="${commonStaticServer}${dto.image!}" alt="奖品图片"/>
                        </#if>
                    </div>
                </div>

                <div class="col-sm-4 travelImage">
                    <input type="file" name="imageFile" imageWidth="280" imageHeight="160"/>
                </div>
                <div class="col-sm-4 text-danger">
                    (图片大小为:280px * 160)
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">奖品介绍: </label>

                <div class="col-sm-4 ">
                    <input type="text" name="introduce" readonly class="form-control travelIntroduceUrl" value="<#if dto??>${dto.introduce!}</#if>"  placeholder="" datatype="*" errormsg="奖品介绍不能为空">

                    <div class="imageIntroduce">
                        <#if dto??&&dto.introduce??>
                            <img style="width:100%" src="${commonStaticServer}${dto.introduce!}" alt="奖品介绍"/>
                        </#if>
                    </div>
                </div>

                <div class="col-sm-4 travelIntroduceImage">
                    <input type="file" name="imageIntroduceFile" imageWidth="1000" />
                </div>
                <div class="col-sm-4 text-danger">
                    (图片宽度大小为:1000px )
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">奖品获奖资格: </label>
                <div class="col-sm-4">
                    <div class="input-group">
                        <div class="input-group-addon">投资满</div>
                        <input type="text" name="investAmount" value="<#if dto??>${(dto.investAmount/100)?c!}</#if>"  class="form-control travelInvestAmount" placeholder="" datatype="*" errormsg="获奖资格不能为空">
                        <div class="input-group-addon">元</div>
                    </div>
                </div>
                <div class="col-sm-7">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"></label>

                <div class="col-sm-4 form-error">
                </div>
            </div>
            <div class="form-group">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="col-sm-4">
                </div>

                <div class="col-sm-3">
                    <button type="button" class="btn jq-btn-form btn-primary travel-confirm">确定</button>
                    <button type="button" class="btn jq-btn-form btn-primary travel-cancel">取消</button>
                </div>
            </div>

        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>