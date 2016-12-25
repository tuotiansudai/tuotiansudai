package com.tuotiansudai.console.controller;

import com.tuotiansudai.ask.repository.model.AnswerStatus;
import com.tuotiansudai.ask.repository.model.IncludeQuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.ask.service.IncludeQuestionService;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.ImportExcelDto;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/ask-manage")
public class AskController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private IncludeQuestionService includeQuestionService;

    private static final String PREFIX = "http://ask.tuotiansudai.com/question";

    @RequestMapping(path = "/questions", method = RequestMethod.GET)
    public ModelAndView getQuestions(@RequestParam(value = "question", required = false) String question,
                                     @RequestParam(value = "mobile", required = false) String mobile,
                                     @RequestParam(value = "status", required = false) QuestionStatus status,
                                     @RequestParam(value = "index", defaultValue = "1", required = false) int index) {

        BaseDto<BasePaginationDataDto> questions = questionService.findQuestionsForConsole(question, mobile, status, index, 10);

        ModelAndView modelAndView = new ModelAndView("/question-list", "questions", questions);
        modelAndView.addObject("question", question);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("status", status);
        modelAndView.addObject("statusOptions", QuestionStatus.values());
        return modelAndView;
    }

    @RequestMapping(path = "/answers", method = RequestMethod.GET)
    public ModelAndView getAnswers(@RequestParam(value = "question", required = false) String question,
                                   @RequestParam(value = "mobile", required = false) String mobile,
                                   @RequestParam(value = "status", required = false) AnswerStatus status,
                                   @RequestParam(value = "index", defaultValue = "1", required = false) int index) {

        BaseDto<BasePaginationDataDto> answers = answerService.findAnswersForConsole(question, mobile, status, index, 10);

        ModelAndView modelAndView = new ModelAndView("/answer-list", "answers", answers);
        modelAndView.addObject("question", question);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("status", status);
        modelAndView.addObject("statusOptions", AnswerStatus.values());
        return modelAndView;
    }

    @RequestMapping(path = "/include-questions", method = RequestMethod.GET)
    public ModelAndView getIncludeQuestions(@RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        BaseDto<BasePaginationDataDto> includeQuestions = includeQuestionService.findAllIncludeQuestions(index, 5);
        ModelAndView modelAndView = new ModelAndView("/include-question-list", "includeQuestions", includeQuestions);
        return modelAndView;
    }

    @RequestMapping(value = "/import-excel", method = RequestMethod.POST)
    @ResponseBody
    public ImportExcelDto importExcel(HttpServletRequest request) throws Exception {
        ImportExcelDto importExcelDto = new ImportExcelDto();
        String uuid = UUIDGenerator.generate();
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multiRequest.getFile("file");
        InputStream inputStream;
        try {
            if (!multipartFile.getOriginalFilename().endsWith(".xls")) {
                importExcelDto.setStatus(false);
                importExcelDto.setMessage("上传失败！请使用2003格式的表格进行上传！");
                return importExcelDto;
            }
            inputStream = multipartFile.getInputStream();
        } catch (NullPointerException e) {
            importExcelDto.setStatus(false);
            importExcelDto.setMessage("您已经取消上传！");
            return importExcelDto;
        }
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        List<String> listSuccess = new ArrayList<>();
        List<String> listFailed = new ArrayList<>();
        for (int rowNum = 0; rowNum < hssfSheet.getLastRowNum() + 1; rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            int firstCellNum = hssfRow.getFirstCellNum();
            HSSFCell hssfCell = hssfRow.getCell(firstCellNum);

            String strVal = getStringVal(hssfCell);
            String questionId = strVal.substring(strVal.lastIndexOf("/") + 1);
            if(!strVal.startsWith(PREFIX)){
                listFailed.add(strVal);
            }else{
                IncludeQuestionModel includeQuestionModel = includeQuestionService.getIncludeQuestionByQuestionId(Long.parseLong(questionId));
                if(includeQuestionModel != null){
                    listFailed.add(strVal);
                }else{
                    QuestionModel questionModel = questionService.findById(Long.parseLong(questionId));
                    if(questionModel == null){
                        listFailed.add(strVal);
                    }else{
                        IncludeQuestionModel includeQuestionModel1 = new IncludeQuestionModel(questionModel.getId(),
                                questionModel.getQuestion(), strVal, questionModel.getTags());
                        includeQuestionService.create(includeQuestionModel1);
                        listSuccess.add(strVal);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(listFailed)) {
            importExcelDto.setStatus(false);
            importExcelDto.setMessage("批量导入失败, 请检查:<br/>链接地址是不正确<br/>文章已经被收录<br/>文章不存在");
        } else {
            importExcelDto.setStatus(true);
            importExcelDto.setFileUuid(uuid);
            importExcelDto.setTotalCount(hssfSheet.getLastRowNum() + 1);
            importExcelDto.setMessage("批量导入成功! 共导入" + listSuccess.size() + "条记录");
        }
        return importExcelDto;
    }

    private String getStringVal(HSSFCell hssfCell) {
        switch (hssfCell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                return hssfCell.getStringCellValue();
            case Cell.CELL_TYPE_STRING:
                return hssfCell.getStringCellValue();
            default:
                return "";
        }
    }

    @RequestMapping(path = "/question/approve", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> approveQuestion(@RequestParam List<Long> ids) {
        questionService.approve(LoginUserInfo.getLoginName(), ids);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(path = "/question/reject", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> rejectQuestion(@RequestParam List<Long> ids) {
        questionService.reject(LoginUserInfo.getLoginName(), ids);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(path = "/answer/approve", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> approveAnswer(@RequestParam List<Long> ids) {
        answerService.approve(LoginUserInfo.getLoginName(), ids);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(path = "/answer/reject", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> rejectAnswer(@RequestParam List<Long> ids) {
        answerService.reject(LoginUserInfo.getLoginName(), ids);

        return new BaseDto<>(new BaseDataDto(true));
    }

}
