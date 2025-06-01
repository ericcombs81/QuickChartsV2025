package main;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;


//NOTE: This is called from CoordinateChartPrinterVBox.  There are really two things going on here:
//Calling it with an individual, or with the whole group.  To make it easier (now - sorry), I made
//It two completely seperate bits of logic.  So if you modify one, modify both and then don't 
//cuss me.  I hope there is a future me that will fix this.  Future me has more time and money.


public class CoordinateChartPrinter {

    static PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

    public static void exportMarcherChart(Ball marcher, String filePath, String title) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float margin = 30;
        float yStart = page.getMediaBox().getHeight() - margin;
        float yPosition = yStart;

        contentStream.setFont(font, 12); // Smaller title font
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Coordinate Chart for marcher #" + marcher.number + ": " + title);
        contentStream.endText();

        yPosition -= 30;

        // Header row with smaller font
        contentStream.setFont(font, 8);
        drawRow(contentStream, margin, yPosition, "Set", "Counts", "Left / Right", "Home / Visitor", false);

        yPosition -= 12;
        
        int pageNumber = 1;

        Point[] points = marcher.points;
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            if (point == null) continue;

            // New page if needed
            if (yPosition < margin + 40) {
                contentStream.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = yStart;
                
                pageNumber++;

                // Draw header: "Marcher #X Page Y"
                contentStream.setFont(font, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Coordinate Chart for marcher #" + marcher.number + " - Page " + pageNumber + ": " + title);
                contentStream.endText();

                yPosition -= 20;

                contentStream.setFont(font, 8);
                drawRow(contentStream, margin, yPosition, "Set", "Counts", "Left / Right", "Home / Visitor", false);
                yPosition -= 12;
            }

            String xLabel = CoordinateCalculator.getXString(point);
            String yLabel = CoordinateCalculator.getYString(point);
            String counts = Frame.top.counts[i] != null ? Frame.top.counts[i] : "";

            boolean shaded = (i % 2 == 1);
            drawRow(contentStream, margin, yPosition, String.valueOf(i + 1), counts, xLabel, yLabel, shaded);

            yPosition -= 12;
        }

        try {
            contentStream.close();
            document.save(filePath);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save PDF: " + e.getMessage());
        }
    }

    private static void drawRow(PDPageContentStream contentStream, float x, float y, String col1, String col2, String col3, String col4, boolean shaded) throws IOException {
        float rowHeight = 10; // was 14
        float rowWidth = 500;

        if (shaded) {
            contentStream.setNonStrokingColor(0.9f);
            contentStream.addRect(x - 2, y - 3, rowWidth, rowHeight); // was y - 4
            contentStream.fill();
            contentStream.setNonStrokingColor(0);
        }

        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(padRight(col1, 4));
        contentStream.newLineAtOffset(40, 0);
        contentStream.showText(padRight(col2, 6));
        contentStream.newLineAtOffset(60, 0);
        contentStream.showText(padRight(col3, 40));
        contentStream.newLineAtOffset(250, 0);
        contentStream.showText(col4);
        contentStream.endText();
    }


    private static String padRight(String text, int length) {
        if (text.length() >= length) return text;
        return String.format("%-" + length + "s", text);
    }
    
    //Below is the logic for the whole damn thing
    public static void printAll(Ball[] marchers, String filePath, String title) {
        PDDocument document = new PDDocument();

        try {
            for (Ball marcher : marchers) {
                if (marcher != null) {
                    appendMarcherToDocument(document, marcher, title);
                }
            }

            document.save(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save PDF: " + e.getMessage());
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void appendMarcherToDocument(PDDocument document, Ball marcher, String title) throws IOException {
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float margin = 30;
        float yStart = page.getMediaBox().getHeight() - margin;
        float yPosition = yStart;

        contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Coordinate Chart for marcher #" + marcher.number + ": " + title);
        contentStream.endText();

        yPosition -= 30;

        contentStream.setFont(font, 8);
        drawRow(contentStream, margin, yPosition, "Set", "Counts", "Left / Right", "Home / Visitor", false);
        yPosition -= 12;

        int pageNumber = 1;

        Point[] points = marcher.points;
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            if (point == null) continue;

            if (yPosition < margin + 40) {
                contentStream.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = yStart;
                pageNumber++;

                contentStream.setFont(font, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Coordinate Chart for marcher #" + marcher.number + " - Page " + pageNumber  + ": " + title);
                contentStream.endText();

                yPosition -= 20;
                contentStream.setFont(font, 8);
                drawRow(contentStream, margin, yPosition, "Set", "Counts", "Left / Right", "Home / Visitor", false);
                yPosition -= 12;
            }

            String xLabel = CoordinateCalculator.getXString(point);
            String yLabel = CoordinateCalculator.getYString(point);
            String counts = Frame.top.counts[i] != null ? Frame.top.counts[i] : "";

            boolean shaded = (i % 2 == 1);
            drawRow(contentStream, margin, yPosition, String.valueOf(i + 1), counts, xLabel, yLabel, shaded);

            yPosition -= 12;
        }

        contentStream.close();
    }



}
