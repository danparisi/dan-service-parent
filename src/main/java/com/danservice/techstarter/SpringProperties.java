package com.danservice.techstarter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class SpringProperties {
    public Spring spring = new Spring();

    @Getter
    @Setter
    @ToString
    public static class Spring {
        public Map<String, String> config = new HashMap<>();
        public Cloud cloud = new Cloud();
    }

    @Getter
    @Setter
    @ToString
    public static class Cloud {
        public Consul consul = new Consul();
    }

    @Getter
    @Setter
    @ToString
    public static class Consul {
        public int port;
        public String host;
    }
}