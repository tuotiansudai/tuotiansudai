package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.security.MyUser;
import com.tuotiansudai.service.HeroRankingService;
import com.tuotiansudai.util.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml", "classpath:spring-security.xml"})
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
    public void shouldObtainHeroRankingIsSuccess() throws Exception{
        mockLoginUser("investor", "13900000000");
        HeroRankingView heroRankingView = new HeroRankingView();
        heroRankingView.setLoginName("loginName");
        heroRankingView.setMobile("13900000000");
        heroRankingView.setSumAmount(2000000000000l);
        heroRankingView.setUserName("userName");
        List<HeroRankingView> heroRankingViews = Lists.newArrayList(heroRankingView);

        when(heroRankingService.obtainHeroRanking(any(Date.class))).thenReturn(heroRankingViews);
        when(randomUtils.encryptLoginName(anyString(),anyString(),anyInt())).thenReturn(heroRankingView.getLoginName());

        this.mockMvc.perform(get("/activity/hero-ranking/invest/2016-07-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("records[0].loginName").value(heroRankingView.getLoginName()))
                .andExpect(jsonPath("records[0].mobile").value(heroRankingView.getMobile()))
                .andExpect(jsonPath("records[0].sumAmount").value(heroRankingView.getSumAmount()))
                .andExpect(jsonPath("records[0].userName").value(heroRankingView.getUserName()));


    }

    private void mockLoginUser(String loginName, String mobile){
        MyUser user = new MyUser(loginName,"", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_PATRON"), mobile, "fdafdsa");
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }
}
