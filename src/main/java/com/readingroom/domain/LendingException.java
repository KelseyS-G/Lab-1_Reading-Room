package com.readingroom.domain;

/**
 * Thrown when a request would break a lending rule (for example lending an item
 * that is already on loan). Unchecked: it signals a refused operation, and the
 * domain stays unchanged when it is thrown.
 *
 * <p>It also carries the item involved (when there is one), so a caller or a log
 * can tell exactly which item the refusal was about.
 */
public class LendingException extends RuntimeException {
    private final transient LendingItem item;

    public LendingException(String message, LendingItem item) {
        super(message);
        this.item = item;
    }

    /** The item the refused request was about. */
    public LendingItem item() {
        return item;
    }
}
