package uz.test.abitur.services.pdf;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;
import uz.test.abitur.dtos.pdf.ParagraphDTO;
import uz.test.abitur.dtos.pdf.TestHistoryPDFGenerateDTO;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GeneratePDF {
    public File generatePDF(ParagraphDTO dto, List<TestHistoryPDFGenerateDTO> subjects) throws IOException {
        File file = File.createTempFile("history", ".pdf");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file));
        Document doc = new Document(pdfDoc);
        List<Double> scores = dto.getScores();
        String firstName = dto.getFirstName();
        String lastName = dto.getLastName();
        String phoneNumber = dto.getPhoneNumber();
        LocalDateTime startedAt = dto.getStartedAt();
        LocalDateTime finishedAt = dto.getFinishedAt();
        Paragraph paragraph = new Paragraph("First name: " + firstName);
        doc.add(paragraph);
        paragraph = new Paragraph("Last name: " + lastName);
        doc.add(paragraph);
        paragraph = new Paragraph("Phone number: " + phoneNumber);
        doc.add(paragraph);
        paragraph = new Paragraph("Started at: " + startedAt);
        doc.add(paragraph);
        paragraph = new Paragraph("Finished at: " + finishedAt);
        doc.add(paragraph);
        paragraph = new Paragraph("Total score: %.5s".formatted(scores.stream().mapToDouble(Double::valueOf).sum()));
        doc.add(paragraph);


        for (int i = 0; i < subjects.size(); i++) {
            TestHistoryPDFGenerateDTO subject = subjects.get(i);
            Double score = scores.get(i);
            paragraph = getParagraph(subject.getSubjectName() + "   score: " + score);
            doc.add(paragraph);
            Table table = getTable();
            getReadyTable(table, subject.getListOfTrueOrFalseAnswers());
            doc.add(table);
        }
        doc.close();
        return file;
    }

    private static Table getTable() {
        Table table = new Table(new float[]{2, 1, 1, 1, 1, 2, 1, 1, 1, 1});
        table.setWidth(PageSize.A4.getWidth() - 50);
        table.setBorder(new SolidBorder(ColorConstants.WHITE, 0));
        return table;
    }

    private static Paragraph getParagraph(String subjectName) {
        Paragraph paragraph = new Paragraph(subjectName);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setBold();
        paragraph.setFontSize(20);
        return paragraph;
    }

    private static void getReadyTable(Table table, List<Boolean> list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            String result = (i + 1) + " . ";
            if (list.get(i) != null && list.get(i)) {
                result += "T";
            } else {
                result += "F";
            }
            Cell cell = new Cell();
            cell.setTextAlignment(TextAlignment.CENTER);
            cell.add(new Paragraph(result).setBold());
            table.addCell(cell);
        }
    }
}