package com.readingroom.domain;

import java.util.Optional;

/**
 * Something the library lends. This class OWNS the lending rule:
 * an item can only be lent while available, and only returned while on loan.
 *
 * <p>The single private {@code borrower} field is the one source of truth.
 * The status is derived from it, so status and borrower can never disagree.
 *
 * <p>Subclasses only say what differs between kinds of item: their label and
 * their base loan period. They cannot touch the borrower, so they cannot
 * bypass the rule.
 */
public abstract class LendingItem {
    private final String title;
    private Member borrower; // null = available

    protected LendingItem(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Item title must not be blank");
        }
        this.title = title;
    }

    /** Label shown in reports, e.g. "BOOK". */
    public abstract String typeLabel();

    /** Loan period in days for a standard member. */
    protected abstract int baseLoanDays();

    public String title() {
        return title;
    }

    public ItemStatus status() {
        return borrower == null ? ItemStatus.AVAILABLE : ItemStatus.ON_LOAN;
    }

    public Optional<Member> borrower() {
        return Optional.ofNullable(borrower);
    }

    /** Package-private: only Library (same package) can move an item, and only through these guards. */
    void lendTo(Member member) {
        if (borrower != null) {
            throw new LendingException("\"" + title + "\" is already on loan to " + borrower.name(), this);
        }
        borrower = member;
    }

    void receiveBack() {
        if (borrower == null) {
            throw new LendingException("\"" + title + "\" is not on loan", this);
        }
        borrower = null;
    }

    /** Loan period for this member: the item's base period, adjusted by the member type. */
    final int loanDaysFor(Member member) {
        return member.type().loanDaysFor(baseLoanDays());
    }
}
