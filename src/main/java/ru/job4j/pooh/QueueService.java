package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        return "GET".equals(req.httpRequestType()) ? get(req) : post(req);
    }

    private Resp post(Req req) {
        queue.putIfAbsent(req.sourceName(), new ConcurrentLinkedQueue<>());
        boolean b = queue.getOrDefault(req.sourceName(), new ConcurrentLinkedQueue<>()).add(req.param());
        return b ? new Resp(null, "200") : new Resp(null, "204");
    }

    private Resp get(Req req) {
        String text = queue.getOrDefault(req.sourceName(), new ConcurrentLinkedQueue<>()).poll();
        return text == null || text.isEmpty()
                ? new Resp(text, "204") : new Resp(text, "200");
    }
}