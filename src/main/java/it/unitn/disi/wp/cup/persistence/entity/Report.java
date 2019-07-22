package it.unitn.disi.wp.cup.persistence.entity;

import java.time.OffsetDateTime;

public class Report {
    private Long id;
    private OffsetDateTime date;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
