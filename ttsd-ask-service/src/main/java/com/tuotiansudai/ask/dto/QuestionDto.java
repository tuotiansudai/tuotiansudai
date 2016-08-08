package com.tuotiansudai.ask.dto;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.Tag;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QuestionDto implements Serializable {

    private long id;

    private String question;

    private String addition;

    private int answers;

    private String mobile;

    private List<String> tags;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdTime;

    public QuestionDto(QuestionModel questionModel, String mobile) {
        this.id = questionModel.getId();
        this.question = questionModel.getQuestion();
        this.addition = questionModel.getAddition();
        this.answers = questionModel.getAnswers();
        this.mobile = mobile;
        this.tags = Lists.transform(questionModel.getTags(), new Function<Tag, String>() {
            @Override
            public String apply(Tag input) {
                return input.getDescription();
            }
        });
        this.createdTime = questionModel.getCreatedTime();
    }

    public long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAddition() {
        return addition;
    }

    public int getAnswers() {
        return answers;
    }

    public String getMobile() {
        return mobile;
    }

    public List<String> getTags() {
        return tags;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
