<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'apply_transfer' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.apply_transfer}" pageJavascript="${js.apply_transfer}" title="申请转让">

<div class="my-account-content apply-transfer"  id="applyTransfer">

    <div class="benefit-box">
        <ul class="benefit-list">
            <li>待收本金: 4010元</li>
            <li>转让服务费: 10元	</li>
            <li>转让截止时间：2017/05/02 00:00:00</li>
        </ul>
        <div class="bg-square-box"></div>
    </div>
    <form id="investForm">
    <div class="input-amount-box">
        <ul class="input-list">
            <li>
                <label>转让价格</label>
                <input type="text" value="" name="price" class="input-amount" placeholder="1,000,000.00">
                <em>元</em>
            </li>
        </ul>
    </div>

    <button type="submit" class="btn-wap-normal" disabled>确定提交</button>

    </form>


    <div class="transfer-notice">

        <div class="agreement-box">
        <span class="init-checkbox-style on">
             <input type="checkbox" id="readOk" class="default-checkbox" checked>
         </span>

            <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-service">《安心签服务协议》</a>、<a href="javascript:void(0)" class="link-agree-privacy">《隐私条款》</a> 和<a href="javascript:void(0)" class="link-agree-number"> 《CFCA数字证书服务协议》</a></lable>

        </div>

        <b>温馨提示:</b>
        1、您只能对某一项目全部转让，转让成功后剩余债权价值归承接人所有； <br/>
        2、转让期间，原债权提前回款，则系统自动取消转让；<br/>
        3、每次转让的有效期为5天，过期未转让成功则自动取消转让；<br/>
        4、服务费用的收取：持有债权不足30天的，收取转让债权本金的1%；持有30-90天的，收取本金的0.5%；持有90天以上的，暂不收取服务费用。
    </div>


</div>

    <#include '../module/anxin-agreement.ftl'>
</@global.main>
