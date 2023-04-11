package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final Map<String, Map<String, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.httpRequestType())) {
            return get(req);
        } else {
            return post(req);
        }
    }

    private Resp post(Req req) {
        var map = queue.putIfAbsent(req.sourceName(), new ConcurrentHashMap<>());
        for (var m : map.entrySet()) {
            map.putIfAbsent(m.getKey(), new ConcurrentLinkedQueue<>()).add(req.param());
        }
        if (!map.isEmpty()) {
            return new Resp(null, "200");
        } else {
            return new Resp(null, "204");
        }
    }

    private Resp get(Req req) {
        queue.putIfAbsent(req.sourceName(), new ConcurrentHashMap<>());
        var mapQueue = queue.get(req.sourceName());
        mapQueue.putIfAbsent(req.param(), new ConcurrentLinkedQueue<>());
        var queue = mapQueue.get(req.param());
        String text = queue.poll();
        if (text == null || text.isEmpty()) {
            text = "";
            return new Resp(text, "204");
        } else {
            return new Resp(text, "200");
        }
    }
}
