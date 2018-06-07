package com.tuotiansudai.ask.dto;

import com.tuotiansudai.ask.repository.model.Tag;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

public class QuestionRequestDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^.{1,200}$")
    private String question;

    @Pattern(regexp = "^.{0,4000}$")
    private String addition;

    private List<Tag> tags;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
