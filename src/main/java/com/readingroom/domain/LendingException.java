package com.readingroom.domain;

/**
 * Thrown when a request would break a lending rule (for example lending an item
 * that is already on loan). Unchecked: it signals a refused operation, and the
 * domain stays unchanged when it is thrown.
 */
public class LendingException extends RuntimeException {
    public LendingException(String message) {
        super(message);
    }
}
