package com.tuotiansudai.activity;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.controller.HeroRankingController;
import com.tuotiansudai.activity.service.HeroRankingService;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml", "classpath:spring-session.xml"})
@WebAppConfiguration
@Transactional
public class HeroRankingControllerTest {

    @Mock
    private HeroRankingService heroRankingService;

    private MockMvc mockMvc;

    @InjectMocks
    private HeroRankingController heroRankingController;
    @Mock
    private RandomUtils randomUtils;
    @Mock
    private LoanMapper loanMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".ftl");

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.heroRankingController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void shouldObtainHeroRankingIsSuccess() throws Exception {
        mockLoginUser("investor");
        HeroRankingView heroRankingView = new HeroRankingView();
        heroRankingView.setLoginName("loginName");
        heroRankingView.setMobile("13900000000");
        heroRankingView.setSumAmount(2000000000000L);
        heroRankingView.setUserName("userName");
        List<HeroRankingView> heroRankingViews = Lists.newArrayList(heroRankingView);
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.COMPLETE);

        when(heroRankingService.obtainHeroRanking(any(ActivityCategory.class),any(Date.class))).thenReturn(heroRankingViews);
        when(randomUtils.encryptMobile(anyString(),anyString(),any(Source.class))).thenReturn(heroRankingView.getLoginName());
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);

        this.mockMvc.perform(get("/activity/hero-ranking/invest/2016-07-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("records[0].loginName").value(heroRankingView.getLoginName()))
                .andExpect(jsonPath("records[0].mobile").value(heroRankingView.getMobile()))
                .andExpect(jsonPath("records[0].sumAmount").value(heroRankingView.getSumAmount()))
                .andExpect(jsonPath("records[0].userName").value(heroRankingView.getUserName()));


    }

    private void mockLoginUser(String loginName) {
        User user = new User(loginName, "", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_PATRON"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }
}
