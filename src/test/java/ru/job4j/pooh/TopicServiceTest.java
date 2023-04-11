package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {
    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", "client407")
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", "temperature=18")
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", "client407")
        );
        /* Режим topic. Пытаемся забрать данные из индивидуальной очереди в топике weather. Очередь client6565.
        Эта очередь отсутствует, т.к. client6565 еще не был подписан, поэтому он получит пустую строку.
        Будет создана индивидуальная очередь для client6565 */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", "client6565")
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void whenTwoTopic() {
        TopicService topicService = new TopicService();
        topicService.process(
                new Req("GET", "topic", "weather", "client407")
        );
        topicService.process(
                new Req("GET", "topic", "pressure", "client407")
        );
        topicService.process(
                new Req("POST", "topic", "weather", "temperature=18")
        );
        topicService.process(
                new Req("POST", "topic", "pressure", "pressure=755mmHg")
        );
        topicService.process(
                new Req("POST", "topic", "weather", "temperature=8")
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", "client407")
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", "client407")
        );
        Resp result3 = topicService.process(
                new Req("GET", "topic", "pressure", "client407")
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is("temperature=8"));
        assertThat(result3.text(), is("pressure=755mmHg"));
    }
}