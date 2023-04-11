package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.httpRequestType())) {
            return get(req);
        } else {
            return post(req);
        }
    }

    private Resp post(Req req) {
        queue.putIfAbsent(req.sourceName(), new ConcurrentLinkedQueue<>());
        boolean b = queue.getOrDefault(req.sourceName(), new ConcurrentLinkedQueue<>()).add(req.param());
        if (b) {
            return new Resp(null, "200");
        } else {
            return new Resp(null, "204");
        }
    }

    private Resp get(Req req) {
        String text = queue.getOrDefault(req.sourceName(), new ConcurrentLinkedQueue<>()).poll();
        if (text == null || text.isEmpty()) {
            return new Resp(text, "204");
        } else {
            return new Resp(text, "200");
        }
    }
}