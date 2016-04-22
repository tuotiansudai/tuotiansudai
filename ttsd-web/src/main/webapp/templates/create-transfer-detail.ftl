<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.create_transfer_detail}" pageJavascript="${js.create_transfer_detail}" activeNav="我的账户" activeLeftNav="债权转让" title="债权转让">
<div class="content-container create-transfer-detail">
    <h4 class="column-title"><em class="tc">债权转让</em></h4>
    <div class="create-info-container">
        <h5>可转让债权>><span>申请转让</span></h5>
        <div class="clearfix transfer-con">
            <ul class="transfer-list-one">
                <li>转让价格：</li>
                <li>项目本金：</li>
                <li>转让手续费：</li>
                <li>转让截止时间：</li>
            </ul>
            <ul class="transfer-list-two">
                <li><input type="text" name="" id="price" value="" /> 元<span class="price-range">转让价格只能设置在9950.00~10000.00元之间</span></li>
                <li>1000000.00元</li>
                <li>50.00元<i class="fa fa-question-circle" aria-hidden="true"></i></li>
                <li>2016-04-13 0点</li>
            </ul>
        </div>
        <p><b class="btn-icon "></b>我已阅读并同意<a href="">债权转让协议书(范本)</a></p>
        <p>
            <input type="submit" value="确认" class="btn btn-normal submit-btn"/>
            <input type="reset" value="取消" class="btn reset-btn"/>
        </p>        
    </div>
</div>
</@global.main>
