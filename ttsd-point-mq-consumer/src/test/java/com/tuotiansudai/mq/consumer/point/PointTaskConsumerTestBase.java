package com.tuotiansudai.mq.consumer.point;

import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

class PointTaskConsumerTestBase {
    void shouldCompleteNewbieTask(PointTaskService mockedPointTaskService, PointTask expectPointTask) {
        String loginName = "helloworld";
        final ArgumentCaptor<PointTask> pointTaskCaptor = ArgumentCaptor.forClass(PointTask.class);
        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(mockedPointTaskService).completeNewbieTask(pointTaskCaptor.capture(), loginNameCaptor.capture());
        mockedPointTaskService.completeNewbieTask(expectPointTask, loginName);
        assertEquals(loginName, loginNameCaptor.getValue());
        assertEquals(expectPointTask, pointTaskCaptor.getValue());
    }

    void shouldCompleteAdvancedTask(MessageConsumer consumer, PointTaskService mockedPointTaskService, PointTask expectPointTask) {
        String loginName = "helloworld";
        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<PointTask> pointTaskCaptor = ArgumentCaptor.forClass(PointTask.class);
        doNothing().when(mockedPointTaskService).completeAdvancedTask(pointTaskCaptor.capture(), loginNameCaptor.capture());
        consumer.consume(loginName);
        assertEquals(expectPointTask, pointTaskCaptor.getValue());
        assertEquals(loginName, loginNameCaptor.getValue());
    }
}
