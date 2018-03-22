<div class="profile-box">
    <i class="profile"></i>
    <ul class="welcome-info">
    <@global.isNotAnonymous>
        <li class="username"><@global.security.authentication property="principal.mobile"/>，您好</li>
        <li class="new-answer-alert <@newAnswerAlert><@global.security.authentication property="principal.username"/></@newAnswerAlert>">
            提问有新回答<br/><a href="${global.applicationContext}/question/my-questions">点击查看</a>
        </li>
        <li class="new-answer-adopted-alert <@newAnswerAdoptedAlert><@global.security.authentication property="principal.username"/></@newAnswerAdoptedAlert>">
            回答被采纳为最佳答案<br/><a href="${global.applicationContext}/answer/my-answers">点击查看</a>
        </li>
    </@global.isNotAnonymous>

    <@global.isAnonymous>
        <li class="username">游客</li>
    </@global.isAnonymous>
    </ul>
    <div class="button-layer">
    <@global.isNotAnonymous>
        <a href="${global.applicationContext}/question/my-questions" class="btn">我的提问<em>(<@myQuestions><@global.security.authentication property="principal.username"/></@myQuestions>)</em></a>
        <a href="${global.applicationContext}/answer/my-answers" class="btn">我的回答<em>(<@myAnswers><@global.security.authentication property="principal.username"/></@myAnswers>)</em></a>
    </@global.isNotAnonymous>
    <@global.isAnonymous>
        <a href="${webServer}/login" class="btn">登录</a>
        <a href="${webServer}/register/user" class="btn">注册</a>
    </@global.isAnonymous>
    </div>
    <div class="vertical-line"></div>
</div>