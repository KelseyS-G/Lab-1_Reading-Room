package com.readingroom.app;

import com.readingroom.domain.AudioKitItem;
import com.readingroom.domain.BookItem;
import com.readingroom.domain.LendingException;
import com.readingroom.domain.LendingItem;
import com.readingroom.domain.Library;
import com.readingroom.domain.Member;
import com.readingroom.domain.MemberType;

/**
 * The same afternoon the original program ran. The domain never prints; this class
 * prints, and turns a refused operation into a "Refused: ..." line.
 */
public final class LendingTrackerApp {

    public static void main(String[] args) {
        var lib = new Library("The Reading Room");

        LendingItem novel = new BookItem("Things Fall Apart");
        LendingItem audioCourse = new AudioKitItem("French Audio Course");
        lib.addItem(novel);
        lib.addItem(audioCourse);

        var kofi = new Member("Kofi Mensah", MemberType.STANDARD);
        var ama = new Member("Ama Owusu", MemberType.STUDENT);
        lib.addMember(kofi);
        lib.addMember(ama);

        // a normal afternoon
        borrow(lib, kofi, novel);
        borrow(lib, ama, audioCourse);
        giveBack(lib, audioCourse);

        // ... and the sort of thing that worries them: now refused by the item itself
        borrow(lib, ama, novel);

        System.out.print(ReportPrinter.endOfDay(lib));
    }

    private static void borrow(Library lib, Member member, LendingItem item) {
        try {
            System.out.println(ReportPrinter.lent(lib.borrow(member, item)));
        } catch (LendingException e) {
            System.out.println("Refused: " + e.getMessage());
        }
    }

    private static void giveBack(Library lib, LendingItem item) {
        try {
            System.out.println(ReportPrinter.returned(lib.returnItem(item)));
        } catch (LendingException e) {
            System.out.println("Refused: " + e.getMessage());
        }
    }
}
