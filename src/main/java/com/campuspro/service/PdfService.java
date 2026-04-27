package com.campuspro.service;

import com.campuspro.model.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfService {

    @Autowired
    private DataService dataService;

    public byte[] generateBulletin(String studentId) throws Exception {
        Map<String, Object> bd = dataService.getBulletinData(studentId);
        Student student = (Student) bd.get("student");
        Classe classe = (Classe) bd.get("classe");

        Document doc = new Document(PageSize.A4, 40, 40, 60, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();

        // Colors
        BaseColor dark = new BaseColor(15, 23, 42);
        BaseColor accent = new BaseColor(99, 102, 241);
        BaseColor light = new BaseColor(226, 232, 240);
        BaseColor white = BaseColor.WHITE;

        // Fonts
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, white);
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, light);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, white);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, dark);
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, dark);
        Font boldInfoFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, dark);

        // Header banner
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell hCell = new PdfPCell();
        hCell.setBackgroundColor(accent);
        hCell.setPadding(20);
        hCell.setBorder(Rectangle.NO_BORDER);

        Paragraph title = new Paragraph("CampusPro", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        hCell.addElement(title);

        Paragraph subtitle = new Paragraph("Bulletin de Notes — Année 2024-2025", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        hCell.addElement(subtitle);
        header.addCell(hCell);
        doc.add(header);

        doc.add(Chunk.NEWLINE);

        // Student info
        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(100);
        info.setSpacingBefore(10);

        addInfoRow(info, "Nom & Prénom", student.getNomComplet(), boldInfoFont, infoFont, light, white);
        addInfoRow(info, "Identifiant", student.getId(), boldInfoFont, infoFont, light, white);
        addInfoRow(info, "Classe", classe != null ? classe.getNom() : "—", boldInfoFont, infoFont, light, white);
        addInfoRow(info, "Email", student.getEmail(), boldInfoFont, infoFont, light, white);
        doc.add(info);

        doc.add(Chunk.NEWLINE);

        // Notes table
        PdfPTable notesTable = new PdfPTable(3);
        notesTable.setWidthPercentage(100);
        notesTable.setWidths(new float[]{5, 2, 2});
        notesTable.setSpacingBefore(10);

        String[] cols = {"Matière", "Coeff.", "Note /20"};
        for (String col : cols) {
            PdfPCell c = new PdfPCell(new Phrase(col, headerFont));
            c.setBackgroundColor(dark);
            c.setPadding(10);
            c.setBorderColor(accent);
            notesTable.addCell(c);
        }

        @SuppressWarnings("unchecked")
        java.util.List<Map<String, Object>> lignes = (java.util.List<Map<String, Object>>) bd.get("lignes");
        boolean alt = false;
        for (Map<String, Object> ligne : lignes) {
            Matiere m = (Matiere) ligne.get("matiere");
            String noteStr = (String) ligne.get("noteStr");
            Double note = (Double) ligne.get("note");

            BaseColor rowBg = alt ? new BaseColor(241, 245, 249) : white;
            Font rowFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, dark);

            addNoteCell(notesTable, m.getNom(), rowBg, rowFont);
            addNoteCell(notesTable, String.valueOf(m.getCoefficient()), rowBg, rowFont);

            BaseColor noteBg = note >= 10 ? new BaseColor(220, 252, 231) : new BaseColor(254, 226, 226);
            Font noteFont2 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,
                    note >= 10 ? new BaseColor(22, 101, 52) : new BaseColor(153, 27, 27));
            PdfPCell nc = new PdfPCell(new Phrase(noteStr, noteFont2));
            nc.setBackgroundColor(noteBg);
            nc.setPadding(8);
            nc.setHorizontalAlignment(Element.ALIGN_CENTER);
            notesTable.addCell(nc);
            alt = !alt;
        }
        doc.add(notesTable);

        // Summary
        doc.add(Chunk.NEWLINE);
        PdfPTable summary = new PdfPTable(2);
        summary.setWidthPercentage(60);
        summary.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Font sumLabel = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, white);
        Font sumVal = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, white);

        PdfPCell lblCell = new PdfPCell(new Phrase("Moyenne Générale", sumLabel));
        lblCell.setBackgroundColor(accent);
        lblCell.setPadding(12);
        lblCell.setBorder(Rectangle.NO_BORDER);
        summary.addCell(lblCell);

        PdfPCell valCell = new PdfPCell(new Phrase(bd.get("moyenne") + " / 20", sumVal));
        valCell.setBackgroundColor(dark);
        valCell.setPadding(12);
        valCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valCell.setBorder(Rectangle.NO_BORDER);
        summary.addCell(valCell);

        PdfPCell mLabel = new PdfPCell(new Phrase("Mention", sumLabel));
        mLabel.setBackgroundColor(accent);
        mLabel.setPadding(12);
        mLabel.setBorder(Rectangle.NO_BORDER);
        summary.addCell(mLabel);

        PdfPCell mVal = new PdfPCell(new Phrase(bd.get("mention").toString(), sumVal));
        mVal.setBackgroundColor(dark);
        mVal.setPadding(12);
        mVal.setHorizontalAlignment(Element.ALIGN_CENTER);
        mVal.setBorder(Rectangle.NO_BORDER);
        summary.addCell(mVal);
        doc.add(summary);

        // Footer
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, new BaseColor(148, 163, 184));
        Paragraph footer = new Paragraph("Document généré automatiquement par CampusPro — Non modifiable", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        doc.add(footer);

        doc.close();
        return out.toByteArray();
    }

    private void addInfoRow(PdfPTable table, String label, String value,
                             Font labelFont, Font valueFont, BaseColor bg1, BaseColor bg2) {
        PdfPCell lCell = new PdfPCell(new Phrase(label, labelFont));
        lCell.setBackgroundColor(bg1);
        lCell.setPadding(8);
        lCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(lCell);

        PdfPCell vCell = new PdfPCell(new Phrase(value, valueFont));
        vCell.setBackgroundColor(bg2);
        vCell.setPadding(8);
        vCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(vCell);
    }

    private void addNoteCell(PdfPTable table, String text, BaseColor bg, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setBackgroundColor(bg);
        c.setPadding(8);
        c.setBorderColor(new BaseColor(226, 232, 240));
        table.addCell(c);
    }
}
