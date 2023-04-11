package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {
    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", "temperature=18")
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenTwoPostThenGetQueue() {
        QueueService queueService = new QueueService();
        queueService.process(
                new Req("POST", "queue", "weather", "temperature=18")
        );
        queueService.process(
                new Req("POST", "queue", "weather", "temperature=8")
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
        assertThat(result.status(), is("200"));
        assertThat(result1.text(), is("temperature=8"));
    }

    @Test
    public void whenNoPostThenGetQueue() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.status(), is("204"));
    }

    @Test
    public void whenPostThenGetTwoQueue() {
        QueueService queueService = new QueueService();
        queueService.process(
                new Req("POST", "queue", "weather", "temperature=18")
        );
        queueService.process(
                new Req("POST", "queue", "pressure", "pressure=755mmHg")
        );
        queueService.process(
                new Req("POST", "queue", "weather", "temperature=8")
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result3 = queueService.process(
                new Req("GET", "queue", "pressure", null)
        );
        assertThat(result.text(), is("temperature=18"));
        assertThat(result.status(), is("200"));
        assertThat(result2.text(), is("temperature=8"));
        assertThat(result3.text(), is("pressure=755mmHg"));
    }
}