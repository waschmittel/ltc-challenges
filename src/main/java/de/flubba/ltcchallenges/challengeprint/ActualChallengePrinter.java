package de.flubba.ltcchallenges.challengeprint;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.BitImageWrapper;
import com.github.anastaciocintra.escpos.image.Bitonal;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.output.PrinterOutputStream;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.LinkedList;

@Slf4j
public final class ActualChallengePrinter {
    private ActualChallengePrinter() {}

    private static final int LINE_WIDTH = 48;

    public static void print(Difficulty difficulty, String task, int cursor) {
        try {
            var starPrinterName = Arrays.stream(PrinterOutputStream.getListPrintServicesNames())
                    .filter(name -> name.toLowerCase().contains("star"))
                    .findFirst()
                    .get();
            log.info("printer found: {}", starPrinterName);
            //this call is slow, try to use it only once and reuse the PrintService variable.
            PrintService printService = PrinterOutputStream.getPrintServiceByName(starPrinterName);
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);

            try (EscPos escpos = new EscPos(printerOutputStream)) {
                printLogo(escpos);
                var header = new Style().setFontSize(Style.FontSize._3, Style.FontSize._3)
                        .setUnderline(Style.Underline.TwoDotThick)
                        .setJustification(EscPosConst.Justification.Center);
                var difficultyStyle = new Style().setFontSize(Style.FontSize._2, Style.FontSize._1)
                        .setJustification(EscPosConst.Justification.Center);

                escpos.initializePrinter(); // TODO: check if this resets the print queue
                escpos.writeLF(header, "LTC challenge");
                escpos.writeLF(difficultyStyle, difficulty.title);
                escpos.feed(2);
                printWrapped(escpos, task);
                escpos.feed(2);
                escpos.writeLF("_".repeat(LINE_WIDTH));
                escpos.writeLF("mastered by:");
                escpos.feed(5);
                escpos.writeLF("_".repeat(LINE_WIDTH));

                var metaInfo = new Style().setJustification(EscPosConst.Justification.Right);
                escpos.writeLF(metaInfo, "%s #%s - %s".formatted(difficulty.title, cursor, LocalDateTime.now().format(FORMATTER)));

                escpos.feed(3);
                escpos.cut(EscPos.CutMode.FULL);
            }
        } catch (IOException | URISyntaxException e) {
            log.error("Could not print", e);
            throw new RuntimeException(e);
        }
    }

    private static void printLogo(EscPos escpos) throws URISyntaxException, IOException {
        BitImageWrapper imageWrapper = new BitImageWrapper();
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        Bitonal algorithm = new BitonalThreshold(127);
        EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(ImageIO.read(ActualChallengePrinter.class.getClassLoader().getResource("ltc-logo.png").toURI().toURL())), algorithm);
        escpos.write(imageWrapper, escposImage);
    }

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendValue(
                    ChronoField.MONTH_OF_YEAR)
            .appendLiteral("-")
            .appendValue(
                    ChronoField.DAY_OF_MONTH)
            .appendLiteral(" ")
            .appendValue(
                    ChronoField.HOUR_OF_DAY)
            .appendLiteral(":")
            .appendValue(
                    ChronoField.MINUTE_OF_HOUR)
            .appendLiteral(":")
            .appendValue(
                    ChronoField.SECOND_OF_MINUTE)
            .toFormatter();

    static void printWrapped(EscPos pos, String challenge) throws IOException {
        var style = new Style().setBold(true);

        var words = challenge.split("\\s");

        var lines = new LinkedList<String>();
        var currentLine = "";
        for (var word : words) {
            String linePlusWord = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (linePlusWord.length() > LINE_WIDTH) {
                lines.add(currentLine);
                currentLine = word;
            } else {
                currentLine = linePlusWord;
            }
        }
        lines.add(currentLine);

        for (var line : lines) {
            pos.writeLF(style, line);
        }
    }
}
