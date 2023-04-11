package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
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
        return null;
    }

    private Resp get(Req req) {
        return null;
    }
}
