package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final Map<String, Map<String, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        return "GET".equals(req.httpRequestType()) ? get(req) : post(req);
    }

    private Resp post(Req req) {
        var map = queue.putIfAbsent(req.sourceName(), new ConcurrentHashMap<>());
        map.forEach((k, v) -> v.add(req.param()));
        return map.isEmpty() ? new Resp(null, "204") : new Resp(null, "200");
    }

    private Resp get(Req req) {
        queue.putIfAbsent(req.sourceName(), new ConcurrentHashMap<>());
        var mapQueue = queue.get(req.sourceName());
        mapQueue.putIfAbsent(req.param(), new ConcurrentLinkedQueue<>());
        var queue = mapQueue.get(req.param());
        String text = queue.poll();
        return text == null || text.isEmpty() ? new Resp("", "204") : new Resp(text, "200");
    }
}
