package de.flubba.ltcchallenges;

import com.formdev.flatlaf.FlatIntelliJLaf;
import de.flubba.ltcchallenges.challengeprint.ChallengeRandomizer;
import de.flubba.ltcchallenges.challengeprint.Cursor;
import de.flubba.ltcchallenges.challengeprint.Difficulty;
import de.flubba.ltcchallenges.ui.UI;
import de.flubba.ltcchallenges.util.OsType;

import javax.swing.*;

import static de.flubba.ltcchallenges.util.OsType.MacOS;

public final class LtcChallenges {
    private LtcChallenges() {
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        if (OsType.get() == MacOS) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "LTC Challenges");
        }
        UIManager.setLookAndFeel(new FlatIntelliJLaf());

        ChallengeRandomizer.initRandomFiles();
        System.out.println(Cursor.getAndIncrement(Difficulty.EASY));
        System.out.println(Cursor.getAndIncrement(Difficulty.EASY));
        System.out.println(Cursor.getAndIncrement(Difficulty.HARD));

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(UI::createAndShow);
    }
}
