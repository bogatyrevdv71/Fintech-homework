package ru.fintech.db.hw1;

/**
 * Created by DB on 24.11.2017.
 */
public class NewsObject {
    private String text;
    private long date;

    NewsObject(String text, long date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    long getDate() {
        return date;
    }
}
