package com.readingroom.app;

import com.readingroom.domain.Library;
import com.readingroom.domain.Loan;
import com.readingroom.domain.LendingItem;

/** Presentation only: turns domain objects into the end-of-day report text. */
public final class ReportPrinter {
    private ReportPrinter() {
    }

    public static String endOfDay(Library lib) {
        var sb = new StringBuilder();
        sb.append('\n');
        sb.append("=== %s - end of day ===\n".formatted(lib.name()));
        sb.append("Items:\n");
        for (LendingItem item : lib.items()) {
            sb.append("  \"%s\" (%s): %s%s\n".formatted(
                    item.title(),
                    item.typeLabel(),
                    item.status(),
                    item.borrower().map(b -> " - with " + b.name()).orElse("")));
        }
        sb.append("Active loans:\n");
        for (Loan loan : lib.activeLoans()) {
            sb.append("  Loan #%d: \"%s\" -> %s\n".formatted(
                    loan.id(), loan.item().title(), loan.member().name()));
        }
        return sb.toString();
    }

    public static String lent(Loan loan) {
        return "Lent \"%s\" to %s for %d days".formatted(
                loan.item().title(), loan.member().name(), loan.loanDays());
    }

    public static String returned(Loan loan) {
        return "Returned \"%s\"".formatted(loan.item().title());
    }
}
