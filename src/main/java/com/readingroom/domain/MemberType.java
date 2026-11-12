package com.readingroom.domain;

/**
 * The kinds of member, and the one place the "longer loans" rule lives.
 * Students get twice the base loan period of the item.
 */
public enum MemberType {
    STANDARD(1),
    STUDENT(2);

    private final int loanMultiplier;

    MemberType(int loanMultiplier) {
        this.loanMultiplier = loanMultiplier;
    }

    /** Loan period in days for an item whose base period is {@code baseDays}. */
    public int loanDaysFor(int baseDays) {
        return baseDays * loanMultiplier;
    }
}
