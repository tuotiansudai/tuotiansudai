<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.risk_estimate}" pageJavascript="${m_js.risk_estimate}" title="投资偏好评估">
<div id="riskResultBox">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i></i></em>投资偏好评估</div>
  <div class="estimate-result">
      <div class="result-bg">
          您的投资偏好为<br/>
          <strong> ${estimate.getType()} </strong>
      </div>

  </div>
    <div class="estimate-text">
    ${estimate.getDescription()}
    </div>
    <div class="link-item">
        <a href="/m/risk-estimate?retry=true" class="btn-item to-risk">重新评估</a>
        <a href="/m/loan-list" class="btn-item to-invest">立即投资</a>
    </div>
</div>

</@global.main>
