package com.tuotiansudai.ask.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class QuestionMapperTest extends BaseMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void shouldCreateQuestion() throws Exception {
        QuestionModel questionModel = new QuestionModel("ask", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));

        questionMapper.create(questionModel);

        List<QuestionModel> savedQuestions = questionMapper.findByLoginName("ask", 0, 1);

        assertThat(savedQuestions.size(), is(1));
        assertThat(savedQuestions.get(0).getId(), is(questionModel.getId()));
    }

    @Test
    public void shouldUpdateQuestion() throws Exception {
        QuestionModel questionModel = new QuestionModel("ask", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));

        questionMapper.create(questionModel);

        questionModel.setQuestion("newQuestion");
        questionModel.setAddition("newAddition");
        questionModel.setAnswers(1);
        questionModel.setApprovedBy("ask");
        questionModel.setApprovedTime(new Date());
        questionModel.setTags(Lists.newArrayList(Tag.OTHER));

        questionMapper.update(questionModel);

        List<QuestionModel> updatedQuestions = questionMapper.findByLoginName("ask", 0, 1);

        QuestionModel updatedQuestionModel = updatedQuestions.get(0);
        assertThat(updatedQuestionModel.getId(), is(questionModel.getId()));
        assertThat(updatedQuestionModel.getQuestion(), is("newQuestion"));
        assertThat(updatedQuestionModel.getAddition(), is("newAddition"));
        assertThat(updatedQuestionModel.getAnswers(), is(1));
        assertThat(updatedQuestionModel.getApprovedBy(), is("ask"));
        assertThat(updatedQuestionModel.getTags(), Is.<List<Tag>>is(Lists.newArrayList(Tag.OTHER)));
        assertNotNull(updatedQuestionModel.getApprovedTime());
    }

    @Test
    public void shouldFindAllQuestions() throws Exception {
        long count = questionMapper.countAllQuestions("ask");

        QuestionModel questionModel = new QuestionModel("ask", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));

        questionMapper.create(questionModel);

        assertThat(questionMapper.findAllQuestions("ask", 0, 1).get(0).getId(), is(questionModel.getId()));
        assertThat(questionMapper.countAllQuestions("ask"), is(count + 1L));
    }

    @Test
    public void shouldFindAllUnresolvedQuestions() throws Exception {
        long count = questionMapper.countAllUnresolvedQuestions("ask");

        QuestionModel questionModel = new QuestionModel("ask", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);

        assertThat(questionMapper.findAllUnresolvedQuestions("ask", 0, 1).get(0).getId(), is(questionModel.getId()));
        assertThat(questionMapper.countAllQuestions("ask"), is(count + 1L));
    }

    @Test
    public void shouldFindAllHotQuestions() throws Exception {
        QuestionModel questionModel1 = new QuestionModel("ask", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionModel1.setLastAnsweredTime(new DateTime().minusDays(1).toDate());
        QuestionModel questionModel2 = new QuestionModel("ask", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionModel2.setLastAnsweredTime(new DateTime().minusDays(2).toDate());
        questionMapper.create(questionModel1);
        questionMapper.create(questionModel2);

        assertThat(questionMapper.findAllHotQuestions("ask", 0, 2).get(0).getId(), is(questionModel1.getId()));
        assertThat(questionMapper.findAllHotQuestions("ask", 0, 2).get(1).getId(), is(questionModel2.getId()));
    }

    @Test
    public void shouldGetQuestionsByKeywordsIsOk(){
        String loginName = "caiBuDao";
        String mobile = "15500001111";
        String question = "testQuestion";
        QuestionModel questionModel = getQuestionModel(loginName, mobile, question);
        questionMapper.create(questionModel);

        List<QuestionModel> questionModels = questionMapper.findQuestionsByKeywords("testQuestion", 0, 10);
        assertTrue(CollectionUtils.isEmpty(questionModels));

        questionModel.setStatus(QuestionStatus.UNRESOLVED);
        questionMapper.update(questionModel);

        questionModels = questionMapper.findQuestionsByKeywords("testQuestion", 0, 10);
        assertTrue(CollectionUtils.isNotEmpty(questionModels));
    }

    private QuestionModel getQuestionModel(String loginName, String mobile, String question){
        return new QuestionModel(loginName, mobile, "fakeMobile", question, "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
    }

    @Test
    public void characterCreateQuestion(){
        QuestionModel questionModel = new QuestionModel("asker", "mobile", "fakeMobile", "question😆", "addition😆", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);
    }

    @Test
    public void characterUpdateQuestion(){
        QuestionModel questionModel = new QuestionModel("asker", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionModel.setQuestion("question😆");
        questionModel.setAddition("addition😆");
        questionMapper.update(questionModel);
    }
}
