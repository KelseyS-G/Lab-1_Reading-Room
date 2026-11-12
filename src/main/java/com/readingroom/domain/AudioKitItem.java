package com.readingroom.domain;

/** An audio kit: 7 days for a standard member. */
public final class AudioKitItem extends LendingItem {
    public AudioKitItem(String title) {
        super(title);
    }

    @Override
    public String typeLabel() {
        return "AUDIO_KIT";
    }

    @Override
    protected int baseLoanDays() {
        return 7;
    }
}
