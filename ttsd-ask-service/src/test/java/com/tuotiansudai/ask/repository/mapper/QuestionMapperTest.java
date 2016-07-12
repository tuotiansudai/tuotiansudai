package com.tuotiansudai.ask.repository.mapper;

import com.google.common.collect.Lists;
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

public class QuestionMapperTest extends BaseMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void shouldCreateQuestion() throws Exception {
        UserModel asker = this.createUser("asker");
        QuestionModel questionModel = new QuestionModel(asker.getLoginName(), "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));

        questionMapper.create(questionModel);

        List<QuestionModel> savedQuestions = questionMapper.findByLoginName(asker.getLoginName());

        assertThat(savedQuestions.size(), is(1));
        assertThat(savedQuestions.get(0).getId(), is(questionModel.getId()));
    }

    @Test
    public void shouldUpdateQuestion() throws Exception {
        UserModel asker = this.createUser("asker");
        QuestionModel questionModel = new QuestionModel(asker.getLoginName(), "question", "addition", Lists.newArrayList(Tag.SECURITIES, Tag.BANK));

        questionMapper.create(questionModel);

        questionModel.setQuestion("newQuestion");
        questionModel.setAddition("newAddition");
        questionModel.setAnswers(1);
        questionModel.setApproved(true);
        questionModel.setApprovedBy(asker.getLoginName());
        questionModel.setApprovedTime(new Date());
        questionModel.setTags(Lists.newArrayList(Tag.OTHER));

        questionMapper.update(questionModel);

        List<QuestionModel> updatedQuestions = questionMapper.findByLoginName(asker.getLoginName());

        QuestionModel updatedQuestionModel = updatedQuestions.get(0);
        assertThat(updatedQuestionModel.getId(), is(questionModel.getId()));
        assertThat(updatedQuestionModel.getQuestion(), is("newQuestion"));
        assertThat(updatedQuestionModel.getAddition(), is("newAddition"));
        assertThat(updatedQuestionModel.getAnswers(), is(1));
        assertThat(updatedQuestionModel.isApproved(), is(true));
        assertThat(updatedQuestionModel.getApprovedBy(), is(asker.getLoginName()));
        assertThat(updatedQuestionModel.getTags(), Is.<List<Tag>>is(Lists.newArrayList(Tag.OTHER)));
        assertNotNull(updatedQuestionModel.getApprovedTime());
    }
}
