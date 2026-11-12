package com.readingroom.domain;

/** A book: 14 days for a standard member. */
public final class BookItem extends LendingItem {
    public BookItem(String title) {
        super(title);
    }

    @Override
    public String typeLabel() {
        return "BOOK";
    }

    @Override
    protected int baseLoanDays() {
        return 14;
    }
}
