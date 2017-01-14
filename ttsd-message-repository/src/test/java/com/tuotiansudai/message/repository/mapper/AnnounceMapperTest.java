package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.message.repository.model.AnnounceModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class AnnounceMapperTest extends BaseMapperTest {

    @Autowired
    private AnnounceMapper announceMapper;

    @Test
    public void shouldCreateAnnounce() throws Exception {
        AnnounceModel announceModel = new AnnounceModel("title", "content", "contentText", false);
        announceMapper.create(announceModel);

        assertNotNull(announceMapper.findById(announceModel.getId()));
    }

    @Test
    public void shouldDeleteAnnounce() throws Exception {
        AnnounceModel announceModel = new AnnounceModel("title", "content", "contentText", false);
        announceMapper.create(announceModel);
        announceMapper.delete(announceModel.getId());

        assertNull(announceMapper.findById(announceModel.getId()));
    }

    @Test
    public void shouldUpdateAnnounce() throws Exception {
        AnnounceModel announceModel = new AnnounceModel("title", "content", "contentText", false);
        announceMapper.create(announceModel);
        announceModel.setTitle("title1");
        announceModel.setContent("content1");
        announceModel.setContentText("contentText1");
        announceModel.setShowOnHome(false);
        announceModel.setUpdatedTime(new Date());
        announceMapper.update(announceModel);

        AnnounceModel updatedAnnounce = announceMapper.findById(announceModel.getId());
        assertThat(updatedAnnounce.getTitle(), is(announceModel.getTitle()));
        assertThat(updatedAnnounce.getContent(), is(announceModel.getContent()));
        assertThat(updatedAnnounce.getContentText(), is(announceModel.getContentText()));
        assertThat(updatedAnnounce.isShowOnHome(), is(announceModel.isShowOnHome()));
    }
}
