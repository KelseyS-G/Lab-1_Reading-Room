package com.readingroom.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates items, members and loans. It contains no printing and no
 * per-item-type branching: the item decides its own loan period, the member
 * type adjusts it, and the item refuses an invalid move itself.
 *
 * <p>Every operation validates first and changes state last, so a refused
 * request leaves the library exactly as it was.
 */
public final class Library {
    private final String name;
    private final List<LendingItem> items = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();
    private int nextLoanId = 1;

    public Library(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Library name must not be blank");
        }
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void addItem(LendingItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null");
        }
        if (items.contains(item)) {
            throw new IllegalArgumentException("Item already registered: " + item.title());
        }
        items.add(item);
    }

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member must not be null");
        }
        if (members.contains(member)) {
            throw new IllegalArgumentException("Member already registered: " + member.name());
        }
        members.add(member);
    }

    /**
     * Lend an item to a member.
     *
     * @throws LendingException if the item is already on loan, or either party is not registered
     */
    public Loan borrow(Member member, LendingItem item) {
        requireRegistered(member, item);
        item.lendTo(member); // throws if the item is not available: nothing below runs
        var loan = new Loan(nextLoanId++, member, item, item.loanDaysFor(member));
        member.recordBorrowed(item.title());
        loans.add(loan);
        return loan;
    }

    /**
     * Take an item back.
     *
     * @throws LendingException if the item is not on loan, or is not registered
     */
    public Loan returnItem(LendingItem item) {
        if (!items.contains(item)) {
            throw new LendingException("Unknown item: " + item.title());
        }
        item.receiveBack(); // throws if the item is not on loan
        var loan = loans.stream()
                .filter(l -> l.item() == item && l.isActive())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Item on loan but no active loan found: " + item.title()));
        loan.markReturned();
        return loan;
    }

    /** Snapshots: callers get copies and can never edit the library's own lists. */
    public List<LendingItem> items() {
        return List.copyOf(items);
    }

    public List<Member> members() {
        return List.copyOf(members);
    }

    public List<Loan> loans() {
        return List.copyOf(loans);
    }

    public List<Loan> activeLoans() {
        return loans.stream().filter(Loan::isActive).toList();
    }

    /** What this member currently has out: the question a volunteer is asked most at the desk. */
    public List<Loan> activeLoansFor(Member member) {
        return loans.stream()
                .filter(l -> l.isActive() && l.member() == member)
                .toList();
    }

    private void requireRegistered(Member member, LendingItem item) {
        if (!members.contains(member)) {
            throw new LendingException("Unknown member: " + member.name());
        }
        if (!items.contains(item)) {
            throw new LendingException("Unknown item: " + item.title());
        }
    }
}
