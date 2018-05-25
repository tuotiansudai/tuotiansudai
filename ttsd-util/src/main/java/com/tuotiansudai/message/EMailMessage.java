
package com.tuotiansudai.message;

import java.io.Serializable;
import java.util.List;

public class EMailMessage implements Serializable {

    private List<String> addresses;

    private String title;

    private String content;

    public EMailMessage() {
    }

    public EMailMessage(List<String> addresses, String title, String content) {
        this.addresses = addresses;
        this.title = title;
        this.content = content;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
