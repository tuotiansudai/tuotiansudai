<div class="get-skipphone-tip" id="getSkipPhone">
        <div class="tip-item">
            <span>
                <input type="text" class="skip-phone-code" id="skipPhoneCode" maxlength="6" placeholder="请输入验证码">
            </span>
            <span>
                <input type="button" class="get-skip-code" value="获取短信验证码" id="getSkipCode">
                <i class="microphone" id="microPhone">
                    <img src="${staticServer}/images/icons/microphone.png">
                </i>
            </span>
        </div>
        <div class="tip-item">
            <span class="skip-error" id="skipError"></span>
            <a href="/activity/landing-anxin" class="skip-intro" target="_blank">什么是安心签？</a>
        </div>
        <div class="tip-item">
            <button class="get-skip-btn" id="getSkipBtn">立即授权</button>
        </div>
        <div class="tip-item">
            <label>
                <i class="skip-icon active"></i>
                <input type="hidden" id="tipCheck" value="true">
            </label>
            <a class="skip-text">
                我已阅读并同意<span class="anxin_layer link-agree-free-SMS">《短信免责申明》</span>
            </a>
        </div>
</div>
<div class="skip-success-tip" id="skipSuccess">
    <span><i class="skip-icon"><img src="${staticServer}/images/icons/skip-success.png"></i>授权成功！</span>
</div>