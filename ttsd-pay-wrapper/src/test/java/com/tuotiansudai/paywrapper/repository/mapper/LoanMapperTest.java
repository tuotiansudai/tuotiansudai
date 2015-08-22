package com.tuotiansudai.paywrapper.repository.mapper;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
//@Transactional(value = "payTransactionManager",rollbackFor = Exception.class)
public class LoanMapperTest {
//    @Autowired
//    private LoanMapper loanMapper;
//
//    @Autowired
//    private LoanTitleMapper loanTitleMapper;
//
//    @Autowired
//    private TitleMapper titleMapper;
//
//    @Test
//    public void createLoanTest(){
//        LoanDto loanDto = new LoanDto();
//        IdGenerator idGenerator = new IdGenerator();
//        loanDto.setLoanLoginName("xiangjie");
//        loanDto.setAgentLoginName("xiangjie");
//        String id = String.valueOf(idGenerator.generate());
//        loanDto.setId(id);
//        loanDto.setProjectName("店铺资金周转");
//        loanDto.setActivityRate("12");
//        loanDto.setShowOnHome("1");
//        loanDto.setPeriods("30");
//        loanDto.setActivityType("DIRECTIONAL_INVEST");
//        loanDto.setContractId("123");
//        loanDto.setDescriptionHtml("asdfasdf");
//        loanDto.setDescriptionText("asdfasd");
//        loanDto.setFundraisingEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        loanDto.setFundraisingStartTime("2015-8-13 13:26:36");
//        loanDto.setInvestFeeRate("15");
//        loanDto.setInvestIncreasingAmount("1");
//        loanDto.setLoanAmount("10000");
//        loanDto.setType("loan_type_1");
//        loanDto.setMaxInvestAmount("100000000000");
//        loanDto.setMinInvestAmount("0");
//        List<LoanTitleModel> loanTitleModelList = new ArrayList<LoanTitleModel>();
//        for(int i=0;i<5;i++){
//            LoanTitleModel loanTitleModel = new LoanTitleModel();
//            loanTitleModel.setId(new BigInteger(String.valueOf(idGenerator.generate())));
//            loanTitleModel.setLoanId(new BigInteger(id));
//            loanTitleModel.setTitleId(new BigInteger("1234567890"));
//            loanTitleModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
//            loanTitleModelList.add(loanTitleModel);
//        }
//        loanDto.setLoanTitles(loanTitleModelList);
//        try {
//            LoanModel loanModel = new LoanModel(loanDto);
//            loanMapper.createLoan(loanModel);
//            loanTitleMapper.createLoanTitle(loanDto.getLoanTitles());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void createTitleTest(){
//        TitleModel titleModel = new TitleModel();
//        IdGenerator idGenerator = new IdGenerator();
//        titleModel.setId(new BigInteger(String.valueOf(idGenerator.generate())));
//        titleModel.setType("base");
//        titleModel.setTitle("房产证");
//        titleMapper.createTitle(titleModel);
//    }
//
//    @Test
//    public void findAlltitles(){
//        List<TitleModel> titleModels = titleMapper.findAllTitles();
//    }

}
