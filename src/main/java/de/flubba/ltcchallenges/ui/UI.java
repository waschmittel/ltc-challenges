package de.flubba.ltcchallenges.ui;

import de.flubba.ltcchallenges.challengeprint.Difficulty;
import de.flubba.ltcchallenges.challengeprint.RandomChallengePrinter;
import de.flubba.ltcchallenges.util.OsType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static de.flubba.ltcchallenges.util.OsType.MacOS;

public final class UI {

    private UI() {
    }

    public static void createAndShow() {
        JFrame frame = setupWindow();
        layoutFrame(frame);
        display(frame);
    }

    private static JFrame setupWindow() {
        JFrame frame = new JFrame("LTC Challenges");
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        if (OsType.get() == MacOS) {
            var rootPane = frame.getRootPane();
            rootPane.putClientProperty("apple.awt.transparentTitleBar", true);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    public static void layoutFrame(JFrame frame) {
        var pane = frame.getContentPane();
        var grid = new GridLayout(2, 2);
        pane.setLayout(grid);
        pane.setBackground(Color.BLACK);

        pane.add(makeButton("easy",
                new Color(50, 255, 50),
                new Color(100, 200, 100),
                () -> RandomChallengePrinter.printChallenge(Difficulty.EASY)));
        pane.add(makeButton("medium",
                new Color(255, 255, 50),
                new Color(200, 200, 100),
                () -> RandomChallengePrinter.printChallenge(Difficulty.MEDIUM)));
        pane.add(makeButton("hard",
                new Color(255, 100, 0),
                new Color(200, 80, 50),
                () -> RandomChallengePrinter.printChallenge(Difficulty.HARD)));
        pane.add(makeButton("very hard",
                new Color(255, 0, 0),
                new Color(200, 50, 50),
                () -> RandomChallengePrinter.printChallenge(Difficulty.VERY_HARD)));
    }

    private static JButton makeButton(String text, Color fg, Color bg, Runnable callback) {
        var button = new JButton(text);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setFont(new Font(button.getFont().getName(), Font.BOLD, 60));
        button.setBorder(new LineBorder(fg, 10));
        button.addActionListener(e -> callback.run());
        return button;
    }

    private static void display(JFrame frame) {
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();

        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        frame.setResizable(false);
        device.setFullScreenWindow(frame);
    }
}
