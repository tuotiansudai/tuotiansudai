package com.tuotiansudai.ask.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.repository.model.UserModel;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class QuestionMapperTest extends BaseMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void shouldCreateQuestion() throws Exception {
        QuestionModel questionModel = new QuestionModel("ask", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));

        questionMapper.create(questionModel);

        List<QuestionModel> savedQuestions = questionMapper.findByLoginName("ask", 0, 1);

        assertThat(savedQuestions.size(), is(1));
        assertThat(savedQuestions.get(0).getId(), is(questionModel.getId()));
    }

    @Test
    public void shouldUpdateQuestion() throws Exception {
        QuestionModel questionModel = new QuestionModel("ask", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));

        questionMapper.create(questionModel);

        questionModel.setQuestion("newQuestion");
        questionModel.setAddition("newAddition");
        questionModel.setAnswers(1);
        questionModel.setApproved(true);
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
        assertThat(updatedQuestionModel.isApproved(), is(true));
        assertThat(updatedQuestionModel.getApprovedBy(), is("ask"));
        assertThat(updatedQuestionModel.getTags(), Is.<List<Tag>>is(Lists.newArrayList(Tag.OTHER)));
        assertNotNull(updatedQuestionModel.getApprovedTime());
    }

    @Test
    public void shouldFindAllQuestions() throws Exception {
        QuestionModel questionModel = new QuestionModel("ask", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));

        questionMapper.create(questionModel);

        assertThat(questionMapper.findAllQuestions("ask", 0, 1).get(0).getId(), is(questionModel.getId()));
        assertThat(questionMapper.countAllQuestions("ask"), is(1L));

        assertThat(questionMapper.findAllQuestions(null, 0, 1).size(), is(0));
        assertThat(questionMapper.countAllQuestions(null), is(0L));
    }

    @Test
    public void shouldFindAllUnresolvedQuestions() throws Exception {
        QuestionModel questionModel = new QuestionModel("ask", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);

        assertThat(questionMapper.findAllUnresolvedQuestions("ask", 0, 1).get(0).getId(), is(questionModel.getId()));
        assertThat(questionMapper.countAllUnresolvedQuestions("ask"), is(1L));

        assertThat(questionMapper.findAllUnresolvedQuestions(null, 0, 1).size(), is(0));
        assertThat(questionMapper.countAllUnresolvedQuestions(null), is(0L));
    }

    @Test
    public void shouldFindAllHotQuestions() throws Exception {
        QuestionModel questionModel1 = new QuestionModel("ask", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionModel1.setLastAnsweredTime(new DateTime().minusDays(1).toDate());
        QuestionModel questionModel2 = new QuestionModel("ask", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionModel2.setLastAnsweredTime(new DateTime().minusDays(2).toDate());
        questionMapper.create(questionModel1);
        questionMapper.create(questionModel2);

        assertThat(questionMapper.findAllHotQuestions("ask", 0, 2).get(0).getId(), is(questionModel1.getId()));
        assertThat(questionMapper.findAllHotQuestions("ask", 0, 2).get(1).getId(), is(questionModel2.getId()));

        assertThat(questionMapper.findAllHotQuestions(null, 0, 1).size(), is(0));
    }
}
