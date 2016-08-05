package com.tuotiansudai.ask.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.repository.model.UserModel;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class AnswerMapperTest extends BaseMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Test
    public void shouldCreateAnswer() throws Exception {
        QuestionModel questionModel = new QuestionModel("asker", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);

        AnswerModel answerModel = new AnswerModel("answerer", questionModel.getId(), "answer");
        answerMapper.create(answerModel);

        List<AnswerModel> savedAnswers = answerMapper.findByLoginName("answerer");
        assertThat(savedAnswers.size(), is(1));
        assertThat(savedAnswers.get(0).getId(), is(answerModel.getId()));
    }

    @Test
    public void shouldUpdateAnswer() throws Exception {
        QuestionModel questionModel = new QuestionModel("ask", "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));
        questionMapper.create(questionModel);

        AnswerModel answerModel = new AnswerModel("answerer", questionModel.getId(), "answer");
        answerMapper.create(answerModel);

        answerModel.setBestAnswer(true);
        answerModel.setFavorite(1);
        answerModel.setApproved(true);
        answerModel.setApprovedBy("answerer");
        answerModel.setApprovedTime(new Date());
        answerMapper.update(answerModel);

        List<AnswerModel> updatedAnswers = answerMapper.findByLoginName("answerer");

        AnswerModel updatedAnswer = updatedAnswers.get(0);
        assertThat(updatedAnswer.getId(), is(answerModel.getId()));
        assertThat(updatedAnswer.isBestAnswer(), is(true));
        assertThat(updatedAnswer.getFavorite(), is(1));
        assertThat(updatedAnswer.isApproved(), is(true));
        assertThat(updatedAnswer.getApprovedBy(), is("answerer"));
        assertNotNull(updatedAnswer.getApprovedTime());
    }
}
