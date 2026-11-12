package com.readingroom.domain;

/** An audio kit: 7 days for a standard member. */
public final class AudioKitItem extends LendingItem {
    public AudioKitItem(String title) {
        super(title);
    }

    // Same label the original report printed, kept so the output stays identical.
    @Override
    public String typeLabel() {
        return "AUDIO_KIT";
    }

    @Override
    protected int baseLoanDays() {
        return 7;
    }
}
