<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.cash_snowball_2017}" pageJavascript="${js.cash_snowball_2017}" activeNav="" activeLeftNav="" title="现金滚雪球_活动中心_拓天速贷" keywords="拓天速贷,逢万返百,现金滚雪球,奖励不封顶" description="活动期间，投资带有“逢万返百”标签的项目，累计年化投资额每满1万元，
活动期间，投资带有“逢万返百”标签的项目，累计年化投资额达10万元以上，即返100元现金.如累计投资年化10万元则返1000元现金，多投多得，上不封顶。活动期间，投资带有“逢万返百”标签的项目，累计年化投资额达10万元以上，可叠加最高2018元现金奖励。">
<div class="snow-banner">
</div>
<div class="activity-wrap" id="cash_snowball">
    <div class="activity-page-frame page-width1200" id="activityPageFrame">
      <div class="cash_container page-width">
          <div class="cash-main my-page">
              <div class="title-top">

              </div>
              <div class="invest-wrap">
                  <div class="wap-title-top"></div>

                      <h2 class="activity-title-top">
                          活动期间，投资带有<strong>“逢万返百”</strong>标签的项目，<span class="mobile-style">累计年化投资额每满1万元，即返<strong>100元</strong>现金;</span>
                          <span class="font-wrap">如累计投资年化10万元则返1000元现金，<span class="mobile-style">多投多得，上不封顶。</span></span>
                      </h2>
                  <@global.isNotAnonymous>
                      <div class="amount-investment">
                          您的累计年化投资额为${annualizedAmount!}元
                      </div>
                  </@global.isNotAnonymous>
                      <div class="run-way" id="ware_DOM">
                          <span class="red-ware red-ware-top red-ware-1">
                             100
                              </span>
                          <span class="red-ware red-ware-bottom red-ware-2">
                             200
                              </span>
                          <span class="red-ware red-ware-top red-ware-3">
                             300
                              </span>
                          <span class="red-ware red-ware-bottom red-ware-4">
                            400
                              </span>
                          <span class="red-ware red-ware-top red-ware-5">
                             500
                              </span>
                          <span class="christmas-tree christmas-tree-1"></span>
                          <span class="christmas-tree christmas-tree-2"></span>
                          <span class="christmas-man"></span>
                          <div class="way">
                            <@global.isNotAnonymous>
                              <span id="money_tip" class="money-tip"><em>${cashAmount!}</em>元返现</span>
                            </@global.isNotAnonymous>
                              <div class="progress" id="progress">
                                  <span class="circle circle-1"></span>
                                  <span class="thread thread-1"></span>
                                  <span class="circle circle-2"></span>
                                  <span class="thread thread-2"></span>
                                  <span class="circle circle-3"></span>
                                  <span class="thread thread-3"></span>
                                  <span class="circle circle-4"></span>
                                  <span class="thread thread-4"></span>
                                  <span class="circle circle-5"></span>
                              </div>
                          </div>
                      </div>
                  <div class="run-way mobile-run-way" id="mobile_ware_DOM">
                          <span class="red-ware red-ware-1">
                             100
                              </span>
                      <span class="red-ware red-ware-2">
                             200
                              </span>
                      <span class="red-ware red-ware-3">
                              300
                              </span>

                      <span class="christmas-tree christmas-tree-1"></span>
                      <span class="christmas-tree christmas-tree-2"></span>

                      <div class="way">
                          <@global.isNotAnonymous>
                              <span id="mobile_money_tip" class="money-tip"><em>${cashAmount!}</em>元返现</span>
                          </@global.isNotAnonymous>
                          <div class="progress" id="progress">
                              <span class="circle circle-1"></span>
                              <span class="thread thread-1"></span>
                              <span class="circle circle-2"></span>
                              <span class="thread thread-2"></span>
                              <span class="circle circle-3"></span>
                          </div>
                      </div>
                  </div>
              <@global.isAnonymous>
                  <div class="already-obtained">
                      您还未登录，登录看看自己的投资返现吧！
                  </div>
                  <span class="invest-btn login-button invest-button to-login">去登录</span>
              </@global.isAnonymous>

                  <@global.isNotAnonymous>
                      <div class="already-obtained">
                          <#if cashAmount == "0">
                              <div class="already-obtained">
                                  您还没有现金奖励，快去投资吧！
                              </div>
                          <#else>
                              <div class="tips">
                                  您已获得<strong id="returnMoney">${cashAmount!}元</strong>返现，<span class="mobile-style">再投<strong>${nextAmount!}元</strong>（年化）可再返<strong>100元</strong></span>
                              </div>
                          </#if>
                          <span class="invest-btn invest-button to-invest">去投资</span>
                      </div>
                  </@global.isNotAnonymous>



              </div>
              <div class="investment-records">
                  <h2><span>投资记录</span></h2>
                  <div class="record-list">

                      <#if record?has_content>
                          <ul id="record_list">
                              <#list record as cashSnowballActivityModel>
                                  <li><em>${(cashSnowballActivityModel.updatedTime?string('yyyy-MM-dd HH:mm'))!}</em><span class="congratulations">恭喜</span>${cashSnowballActivityModel.mobile}<span class="congratulations">用户</span><span class="mobile-style">累计年化投资${(cashSnowballActivityModel.annualizedAmount/100)?string('0.00')}元 <i>，</i></span>获得${(cashSnowballActivityModel.cashAmount/100)?string('0')}元现金<span class="congratulations">奖励</span></li>
                              </#list>
                          </ul>
                      <#else>
                              <div class="no-record">目前还没有人获得现金奖励，快去投资吧！</div>
                      </#if>
                      <div class="no-record">您还没有人获得现金奖励，快去投资吧！</div>

                  </div>

              </div>
              <div class="remarks">
                  <dl class="clearfix">
                      <dt>注：</dt>
                      <dd>1、本活动现金奖励将于项目放款当日内发放到个人账户，用户可直接提现或者用于投资；</dd>

                      <dd class="two">2、在同一项目中如出现不满1万元的年化投资额，均可与其他“逢万返百”项目投资额进行累计。</dd>
                  </dl>
              </div>
              <div class="cash-superposition">
                  <h2 class="superposition-title"></h2>
                  <h4 class="superposition-info">
                      活动期间，投资带有<em>“逢万返百”</em>标签的项目，<span class="mobile-style">累计年化投资额达10万元以上，</span>
                      <span class="wrap">可叠加最高<strong>2018</strong>元现金奖励。</span>
                  </h4>
                  <div class="invest-bar-graph">

                  </div>
                  <p class="cash-font">恭喜您获取<strong>100</strong>元的叠加现金奖励</p>
                  <span class="invest-btn-2 invest-btn to-invest">去投资</span>
                  <p class="comment">本活动现金奖励将于活动结束后3个工作日内统一发放至个人账户，用户可直接提现或者用于投资</p>

              </div>
              <div class="kindly-tips">
                  <dl>
                      <dt>温馨提示:</dt>
                      <dd class="computed">
                          1、本次“逢万返百”累计方式为用户年化投资额，年化投资额计算公式：<br/>
                          <table>
                              <tr><td>60天 - 90天项目</td><td>年化投资额 = 实际投资金额 / 4</td></tr>
                              <tr><td>120天 - 180天项目 </td><td>年化投资额 = 实际投资金额 / 2</td></tr>
                              <tr><td>330天 - 360天项目 </td><td>年化投资额 = 实际投资金额</td></tr>

                          </table>
                      </dd>
                      <dd>
                          2、活动仅限带有 “逢万返百” 标签的项目，请用户投资时看准项目标签，没有标签的项目不参与该两项活动；
                      </dd>
                      <dd>3、“逢万返百” 项目不参与阶梯加息奖励，不可与平台优惠券同享；</dd>
                      <dd>
                          4、用户投资所获现金奖励，可在PC端“我的账户”或App端“我的”中查看；
                      </dd>
                      <dd>5、为了保证获奖结果的公平性，“逢万返百”项目不允许进行债权转让；</dd>
                      <dd>6、活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
                      <dd>7、活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
                  </dl>
              </div>

          </div>
      </div>
    </div>
    <#include "../../module/login-tip.ftl" />

</div>

</@global.main>

