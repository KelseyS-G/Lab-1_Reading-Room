package com.readingroom.domain;

/**
 * One lending of one item to one member. Holds real references (not title strings),
 * so a loan always points at exactly the item and member it was made for.
 * Only {@link Library} creates or closes a loan (the constructor and
 * {@code markReturned} are package-private), so the loan rules live in one place.
 */
public final class Loan {
    public enum Status { ACTIVE, RETURNED }

    private final int id;
    private final Member member;
    private final LendingItem item;
    private final int loanDays;
    private Status status = Status.ACTIVE;

    Loan(int id, Member member, LendingItem item, int loanDays) {
        this.id = id;
        this.member = member;
        this.item = item;
        this.loanDays = loanDays;
    }

    public int id() {
        return id;
    }

    public Member member() {
        return member;
    }

    public LendingItem item() {
        return item;
    }

    public int loanDays() {
        return loanDays;
    }

    public Status status() {
        return status;
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    void markReturned() {
        if (status == Status.RETURNED) {
            throw new IllegalStateException("Loan #" + id + " is already returned");
        }
        status = Status.RETURNED;
    }
}
