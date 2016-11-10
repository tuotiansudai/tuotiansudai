<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.anxin_sign}" pageJavascript="${js.anxin_sign}" activeNav="我的账户" activeLeftNav="安心签" title="安心签">
<div class="safety-signed-frame" id="safetySignedList">
    <h2 class="column-title"><em>安心签</em></h2>
    <div class="safety-signed-list">
        <input type="hidden" data-skip-auth="${anxinProp.skipAuth?c}" class="bind-data">
        <ul class="info-list" >
            <li>
                <span class="info-title"> CFCA安心签服务</span>
                <span class="binding-set">
                    <i class="fa fa-check-circle ok"></i>已开启
                </span>
            </li>
            <li class="switch-sms">
                <span class="info-title">安心签免验服务</span>
                <span class="binding-set sms-open" style="display: none;">
                    <i class="fa fa-check-circle ok"></i>已开启
                    <a class="setlink" href="javascript:void(0);">关闭</a>
                </span>
                <span class="binding-set sms-close" style="display: none;">
                    <i class="fa fa-times-circle no"></i>已关闭
                    <a class="setlink" href="javascript:void(0);">开启</a>
                </span>
            </li>
          </ul>
        <p class="operation-info">
            1、用户点击“开启”并成功“保存”才视为开启自动投标设置成功。 <br/>
            2、投资金额：可设置，最小为1元，最高为100万元（取100的整数倍）。<br/>
            3、保留金额：不会加入自动投标的金额。<br/>
            4、本金复投：本金收回后将在次日零点继续自动投标，若想提现请提前设置保留金额或关闭自动投标。<br/>
            5、项目期限：选择您想投资项目的期限，可多选。<br/>
            6、自动投标成交金额：用户可用余额、设置的最大金额、项目剩余金额三者中的最小值（取100的整数倍）。<br/>
            7、用户开通自动投标功能即视为委托拓天速贷平台与达到用户指定标准的借款人签署借款合同，并承担该合同项下的一切权利及义务。<br/>
            8、自动投标会根据您的设置，筛选并自动为您投资，但我们不能保证对所有的项目投资成功。<br/>
            9、全部设置完成后，会在次日零点开启自动投标。<br/>
            10、自动投标开启后，若有多个项目可投，将尽可能优先新手专享进行投资。<br/>
            11、自动投标开启后，如果不想自动投标，请手动关闭。
        </p>
    </div>


    <div class="open-safety-box clearfix" id="safetyToOpen" style="display: none;">
        <span class="info">开通免短信授权服务，投资快人一步！</span>
        <span class="init-checkbox-style on">
             <input type="checkbox" id="readOk" class="default-checkbox" checked>
         </span>
        <label for="readOk" class="agreeOpen"><em>我已阅读并同意</em>《短信免责申明》</label>

        <div class="button-bar">
            <button class="btn-normal ok fl" type="button">确认</button>
            <button class="btn cancel fr" type="button">取消</button>

        </div>
    </div>

    <div class="open-safety-box clearfix" id="safetyToClose" style="display: none;">
        <span class="info">关闭安心签免短信授权服务后，您每次交易均需进行短信验证码授权。</span>
        <div class="button-bar">
            <a href="javascript:void(0);" class="btn-normal fl cancel">取消</a>
            <a href="javascript:void(0);" class="btn fr ok">确认</a>
        </div>
    </div>

</div>
</@global.main>