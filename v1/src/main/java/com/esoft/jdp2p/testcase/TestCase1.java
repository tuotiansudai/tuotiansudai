package com.esoft.jdp2p.testcase;

import java.util.List;

import javax.annotation.Resource;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.service.impl.AuthInfoBO;
import com.esoft.archer.testcase.TestService;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.exception.ExistWaitAffirmInvests;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:E:\\myeclipse9_workspace\\p2p_standard_2.0\\WebRoot\\WEB-INF\\applicationContext.xml" })
public class TestCase1 {

	// @Resource
	// private SessionFactory sessionFactory;

	// @Resource
	// private TestBean testBean;

	@Resource
	private AuthInfoBO authInfoBO;

	@Resource
	private Scheduler scheduler;
	//
	@Resource
	private HibernateTemplate ht;
	//
	@Resource
	private TestService testService;
	//
	// @Resource
	// private RechargeService rechargeService;
	//
	@Resource
	private RepayService repayService;

	@Test
	public void test() {
		testService.test();
	}

	// @Test
	public void test2() {
		// Loan loan = ht.get(Loan.class, "20140211000001");
		// loanService.giveMoneyToBorrower("20140211000001");
		// loanService.calculateManagementFee(loan.getMoney(), loan.getType(),
		// loan.getDeadline(), loan.getRiskLevel());
	}

	// @Test
	public void test1() {
		// 构造一个Runner
		TestRunnable runner = new TestRunnable() {
			@Override
			public void runTest() throws Throwable {
				// 测试内容
				HttpGet get = new HttpGet(
						"http://localhost:8080/touzimao/payreturn/huanxunzhifu");
				// 使用HttpClient发送get请求，获得返回结果HttpResponse
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(get);

				// File pathToBinary = new
				// File("D:\\Program Files\\Mozilla Firefox\\Firefox.exe");
				// FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
				// FirefoxProfile firefoxProfile = new FirefoxProfile();
				// WebDriver driver = new
				// FirefoxDriver(ffBinary,firefoxProfile);
				// driver.get("http://localhost:8080/archer_new/");
				// driver.quit();

				// Recharge recharge = ht.get(Recharge.class, "26v26v");
				// System.out.println();
				// System.out.println();
				// System.out.println();
				// System.out.println(Thread.currentThread());
				// System.out.println();
				// System.out.println();
				// System.out.println();
				// if (recharge != null
				// && recharge.getStatus().equals(
				// UserConstants.RechargeStatus.WAIT_PAY)) {
				// rechargeService.rechargePaySuccess(recharge.getId());
				// }
			}
		};
		int runnerCount = 100;
		// Rnner数组，相当于并发多少个。
		TestRunnable[] trs = new TestRunnable[runnerCount];
		for (int i = 0; i < runnerCount; i++) {
			trs[i] = runner;
		}
		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		try {
			// 开发并发执行数组里定义的内容
			System.out.println(System.currentTimeMillis());
			mttr.runTestRunnables();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
