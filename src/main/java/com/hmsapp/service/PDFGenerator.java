package com.hmsapp.service;

import com.hmsapp.entity.Booking;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PDFGenerator {

    public void generatePdf(String path, Booking booking) {
        try {
            // Create a writer instance
            PdfWriter writer = new PdfWriter(new File(path));

            // Create a PDF document
            PdfDocument pdfDocument = new PdfDocument(writer);

            // Create a document to add content
            Document document = new Document(pdfDocument);

            // Add a title
            document.add(new Paragraph("Booking Details").setBold().setFontSize(16));

            // Create a table with 2 columns
            float[] columnWidths = {3, 5}; // Column proportions
            Table table = new Table(columnWidths);

            // Add header cells
//            table.addCell(new Cell().add(new Paragraph("Field").setBold()));
//            table.addCell(new Cell().add(new Paragraph("Value").setBold()));

            // Add booking details to the table
            table.addCell(new Cell().add(new Paragraph("Booking ID").setBold()));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getId()))));

            table.addCell(new Cell().add(new Paragraph("Name").setBold()));
            table.addCell(new Cell().add(new Paragraph(booking.getName())));

            table.addCell(new Cell().add(new Paragraph("Email").setBold()));
            table.addCell(new Cell().add(new Paragraph(booking.getEmail())));

            table.addCell(new Cell().add(new Paragraph("Mobile").setBold()));
            table.addCell(new Cell().add(new Paragraph(booking.getMobile())));

            table.addCell(new Cell().add(new Paragraph("No. of Guests").setBold()));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getNoOfGuests()))));

            table.addCell(new Cell().add(new Paragraph("Property").setBold()));
            table.addCell(new Cell().add(new Paragraph(booking.getProperty().getName())));

            // Add table to the document
            document.add(table);

            // Close the document
            document.close();

            System.out.println("PDF with table created successfully: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
