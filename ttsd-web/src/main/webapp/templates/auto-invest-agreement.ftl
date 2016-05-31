<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.auto_invest}" activeNav="我的账户" activeLeftNav="自动投标" title="自动投标">
<div class="content-container auto-invest">
    <h4 class="column-title"><em class="tc">自动投标</em></h4>

        <div class="CertifiedImg"></div>

        <div class="borderBox">
            <p class="notice">
                <b>注意事项：</b> <br/>
                1. 设置并保存后，将在次日零点开启自动投标。<br/>
                2. 自动投标开启后，若有多个项目可投，将尽可能优先选择可使用投资券的项目，投资券优先选择面值大的、快到期的。<br/>
                3. 自动投标开启后，如果不想自动投标，请手动关闭。<br/>
                4. 根据您的设置，筛选并自动为您投资，但我们不能保证对所有的项目投资成功。<br/>
                5. 用户开通自动投标功能即视为委托拓天速贷平台与达到用户指定标准的借款人签署借款合同，并承担该合同项下的一切权利及义务。<br/>
                6. 如果您同意开通自动投标请前往联动优势托管平台进行授权，立即享受自动投标功能。<br/>
            </p>

            <div class="btnBox tc">
                <form action="/agreement" method="post" <@global.role hasRole="'INVESTOR', 'LOANER'">target="_blank"</@global.role>>
                    <input type="hidden" name="autoInvest" value="true"/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" id="btnAuthority" class="btn btn-normal">立即授权</button>
                </form>
            </div>
        </div>

</div>

<div class="auto-invest pad-m tc" style="display: none;">
    <button id="finishAuthor" class="btn btn-normal">已完成授权</button>
</div>
</@global.main>