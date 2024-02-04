package com.matei.users.service;

import lombok.Getter;

@Getter
public class TextBoardException extends RuntimeException {

    private final Type type;

    private TextBoardException(String message, Type type) {
        super(message);
        this.type = type;
    }

    public static TextBoardException notFound(String message) {
        return new TextBoardException(message, Type.NOT_FOUND);
    }

    public static TextBoardException conflict(String message) {
        return new TextBoardException(message, Type.CONFLICT);
    }

    public static TextBoardException forbidden(String message) {
        return new TextBoardException(message, Type.FORBIDDEN);
    }

    public enum Type {
        NOT_FOUND, BAD_REQUEST, FORBIDDEN, CONFLICT, INTERNAL_SERVER_ERROR
    }
}
