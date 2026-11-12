package com.readingroom.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** A library member. Valid from construction; history can be read but not edited from outside. */
public final class Member {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id;
    private final String name;
    private final MemberType type;
    private final List<String> history = new ArrayList<>();

    public Member(String name, MemberType type) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name must not be blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Member type must not be null");
        }
        this.id = NEXT_ID.getAndIncrement();
        this.name = name;
        this.type = type;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public MemberType type() {
        return type;
    }

    /** Read-only view: callers cannot add to or clear the member's history. */
    public List<String> history() {
        return Collections.unmodifiableList(history);
    }

    /** Package-private: only the domain records history, as a side effect of a successful loan. */
    void recordBorrowed(String itemTitle) {
        history.add("Borrowed " + itemTitle);
    }
}
