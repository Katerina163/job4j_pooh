package ru.job4j.pooh;

public record Req(String httpRequestType, String poohMode, String sourceName, String param) {

    public static Req of(String content) {
        String[] request = content.split("[\\s/]+");
        String param;
        if ("POST".equals(request[0])) {
            param = request[request.length - 1];
        } else {
            param = "HTTP".equals(request[3]) ? "" : request[3];
        }
        return new Req(request[0], request[1], request[2], param);
    }
}
