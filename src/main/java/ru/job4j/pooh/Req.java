package ru.job4j.pooh;

public record Req(String httpRequestType, String poohMode, String sourceName, String param) {

    public static Req of(String content) {
        String[] request = content.split(System.lineSeparator());
        String param;
        String sourceName;
        String[] request0 = request[0].split("/");
        if (request0[2].endsWith("P")) {
            sourceName = request0[2].substring(0, request0[2].indexOf(" "));
        } else {
            sourceName = request0[2];
        }
        if (request[0].startsWith("P")) {
            param = request[7];
        } else {
            if (request0[3].startsWith("c")) {
                param = request0[3].substring(0, request0[3].indexOf(" "));
            } else {
                param = "";
            }
        }
        return new Req(request0[0].trim(), request0[1], sourceName, param);
    }
}
