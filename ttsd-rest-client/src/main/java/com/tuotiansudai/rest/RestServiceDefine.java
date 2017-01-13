package com.tuotiansudai.rest;

public class RestServiceDefine {

    private RestfulService AskService;

    public RestfulService getAskService() {
        return AskService;
    }

    public void setAskService(RestfulService askService) {
        AskService = askService;
    }

    public static class RestfulService {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
