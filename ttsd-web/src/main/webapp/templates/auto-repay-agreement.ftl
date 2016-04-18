<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.auto_repay}" activeNav="我的账户" activeLeftNav="我的借款" title="自动还款">
<div class="content-container auto-repay">
    <h4 class="column-title"><em class="tc">自动还款</em></h4>

    <div class="CertifiedImg-repay"></div>

    <div class="borderBox">
        <p class="notice">
            <b>注意事项：</b> <br/>
            1. 设置并保存后，将开启自动还款。<br/>
            2. 自动还款开启后，在每个还款日的下午2点，系统将为您自动还款，请您在此之前确保您的账户余额充足；若您的余额不足，平台将给您进行短信提醒。<br/>
            3. 自动还款开启后，如果不想自动还款，请手动关闭。<br/>
            4. 如果您同意开通自动还款请前往联动优势托管平台进行授权，立即享受自动还款功能。<br/>
        </p>

        <div class="btnBox tc">
            <form action="/agreement" method="post" target="_blank">
                <input type="hidden" name="autoRepay" value="true"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" id="btnAuthority" class="btn btn-normal">立即授权</button>
            </form>
        </div>
    </div>

</div>

<div class="auto-repay pad-m tc" style="display: none;">
    <button id="finishAuthor" class="btn btn-normal">已完成授权</button>
</div>
</@global.main>