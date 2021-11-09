package com.example.smarttag;

public class Session {
    String apikey;
    Long client_id;
    String start_time;
    Boolean is_new_client;

    public Boolean getIs_new_client() {
        return is_new_client;
    }

    public void setIs_new_client(Boolean is_new_client) {
        this.is_new_client = is_new_client;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

}
