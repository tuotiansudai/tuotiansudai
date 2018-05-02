<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.buy_loan}" pageJavascript="${m_js.buy_loan}" title="直投项目购买详情">

<div class="my-account-content apply-transfer"  id="applyTransfer">

    <div class="benefit-box">
        <div class="target-category-box" data-url="loan-transfer-detail.ftl">
            <div class="newer-title">
                <span>房产抵押借款17070</span>
                <span class="tip-text">剩余可投 : 5,000.00元</span>
            </div>
            <ul class="loan-info clearfix">
                <li>
                    <span>
                        <i>3天</i>
                    </span>
                    <em>项目期限</em>
                </li>
                <li>
                    <span>
                        <i>15%</i>
                    </span>
                    <em>约定年化利率</em>
                </li>
                <li>
                    <span>
                        <i>12.30元</i>
                    </span>
                    <em>万元收益</em>
                </li>
            </ul>
        </div>
        <div class="bg-square-box"></div>
    </div>
    <form id="investForm">
    <div class="input-amount-box">
        <ul class="input-list">
            <li>
                <label>投资金额</label>
                <input type="text" value="" name="price" class="input-amount" placeholder="50.00起投">
                <em>元</em>
            </li>
            <li class="mt-10">
                <label>预期收益</label>
                <span class="number-text"><strong>0</strong>元</span>
            </li>
            <li class="select-coupon" data-url="select-coupon.ftl">
                <label>优惠券</label>
                <input type="text" value="" name="price"  placeholder="无可用优惠券" readonly="readonly">
                <em><i class="fa fa-angle-right"></i></em>
            </li>
        </ul>
    </div>

    <button type="submit" class="btn-wap-normal" disabled>立即投资</button>

    </form>


    <div class="transfer-notice">
        <div class="agreement-box">
            <span class="init-checkbox-style on">
                 <input type="checkbox" id="readOk" class="default-checkbox" checked>
             </span>
            <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-service">《安心签服务协议》</a>、<a href="javascript:void(0)" class="link-agree-privacy">《隐私条款》</a> 和<a href="javascript:void(0)" class="link-agree-number"> 《CFCA数字证书服务协议》</a></lable>
        </div>
        <a href="">查看《借款转让协议样本》</a>
    </div>


</div>

</@global.main>
