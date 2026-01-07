package com.example.flattener;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FlattenerApplication {
  private FlattenerApplication() {
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java -jar flattener.jar <input.pdf> <output.pdf>");
      System.exit(1);
    }

    Path input = Path.of(args[0]);
    Path output = Path.of(args[1]);

    if (!Files.exists(input)) {
      System.err.println("Input file does not exist: " + input);
      System.exit(1);
    }

    try {
      flatten(input, output);
      System.out.println("Flattened PDF written to: " + output);
    } catch (IOException e) {
      System.err.println("Failed to flatten PDF: " + e.getMessage());
      System.exit(1);
    }
  }

  private static void flatten(Path input, Path output) throws IOException {
    try (PdfReader reader = new PdfReader(input.toFile());
         PdfWriter writer = new PdfWriter(output.toFile());
         PdfDocument pdfDocument = new PdfDocument(reader, writer)) {
      PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDocument, false);
      if (acroForm != null) {
        acroForm.flattenFields();
      }
    }
  }
}
