package com.tuotiansudai.ask.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.Tag;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AnswerMapperTest extends BaseMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Test
    public void shouldCreateAnswer() throws Exception {
        QuestionModel questionModel = new QuestionModel("asker", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);

        AnswerModel answerModel = new AnswerModel("answerer", "mobile", "fakeMobile", questionModel.getId(), "answer");
        answerMapper.create(answerModel);

        List<AnswerModel> savedAnswers = answerMapper.findByLoginName("answerer", 0, 1);
        assertThat(savedAnswers.size(), is(1));
        assertThat(savedAnswers.get(0).getId(), is(answerModel.getId()));
    }

    @Test
    public void shouldUpdateAnswer() throws Exception {
        QuestionModel questionModel = new QuestionModel("ask", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);

        AnswerModel answerModel = new AnswerModel("answerer", "mobile", "fakeMobile", questionModel.getId(), "answer");
        answerMapper.create(answerModel);

        answerModel.setBestAnswer(true);
        answerModel.setFavoredBy(Lists.newArrayList("user"));
        answerModel.setApprovedBy("answerer");
        answerModel.setApprovedTime(new Date());
        answerMapper.update(answerModel);

        List<AnswerModel> updatedAnswers = answerMapper.findByLoginName("answerer", 0, 1);

        AnswerModel updatedAnswer = updatedAnswers.get(0);
        assertThat(updatedAnswer.getId(), is(answerModel.getId()));
        assertThat(updatedAnswer.isBestAnswer(), is(true));
        assertTrue(updatedAnswer.getFavoredBy().contains("user"));
        assertThat(updatedAnswer.getApprovedBy(), is("answerer"));
        assertNotNull(updatedAnswer.getApprovedTime());
    }

    @Test
    public void aspectCreateAnswer() throws Exception {
        QuestionModel questionModel = new QuestionModel("asker", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);
        AnswerModel answerModel = new AnswerModel("answerer", "mobile", "fakeMobile", questionModel.getId(), "answer😆");
        answerMapper.create(answerModel);
        List<AnswerModel> savedAnswers = answerMapper.findByLoginName("answerer", 0, 1);
        assertThat(savedAnswers.size(), is(1));
        assertThat(savedAnswers.get(0).getId(), is(answerModel.getId()));
    }

    @Test
    public void aspectUpdateAnswer() throws Exception {
        QuestionModel questionModel = new QuestionModel("asker", "mobile", "fakeMobile", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);
        AnswerModel answerModel = new AnswerModel("answerer", "mobile", "fakeMobile", questionModel.getId(), "answer");
        answerMapper.create(answerModel);
        answerModel.setAnswer("answer😆");
        answerMapper.update(answerModel);
        List<AnswerModel> savedAnswers = answerMapper.findByLoginName("answerer", 0, 1);
        assertThat(savedAnswers.size(), is(1));
        assertThat(savedAnswers.get(0).getId(), is(answerModel.getId()));
    }
}
