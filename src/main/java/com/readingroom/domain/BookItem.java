package com.readingroom.domain;

/** A book: 14 days for a standard member. */
public final class BookItem extends LendingItem {
    public BookItem(String title) {
        super(title);
    }

    // The label is what the end-of-day report has always printed for a book, so it
    // stays "BOOK" to keep the output identical to the original program.
    @Override
    public String typeLabel() {
        return "BOOK";
    }

    @Override
    protected int baseLoanDays() {
        return 14;
    }
}
