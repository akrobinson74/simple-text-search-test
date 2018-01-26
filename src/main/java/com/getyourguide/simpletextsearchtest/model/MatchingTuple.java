package com.getyourguide.simpletextsearchtest.model;

public class MatchingTuple {
    private final Integer id;
    private final String date;
    private final String message;

    public MatchingTuple(
        final Integer id,
        final String date,
        final String message) {
        this.id = id;
        this.date = date;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchingTuple that = (MatchingTuple) o;

        if (!id.equals(that.id)) return false;
        if (!date.equals(that.date)) return false;
        return message.equals(that.message);
    }

    @Override
    public String toString() {
        return "MatchingTuple{" +
            "id=" + id +
            ", date='" + date + '\'' +
            ", message='" + message + '\'' +
            '}';
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
