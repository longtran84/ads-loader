package vn.fintechviet.user.dto;

import java.util.Date;

/**
 * Created by tungn on 10/3/2017.
 */
public class Message {
    private long id;
    private String subject;
    private String body;
    private Date createdDate;
    private byte read;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public byte getRead() {
        return read;
    }

    public void setRead(byte read) {
        this.read = read;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
