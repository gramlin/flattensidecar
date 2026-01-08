package com.example.flattener;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FlattenController {

    @PostMapping(value = "/flatten", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void flatten(@RequestPart(value = "file", required = false) MultipartFile file,
                        HttpServletResponse response) throws IOException {
        if (file == null || file.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file");
            return;
        }

        File tempFile = File.createTempFile("flattened-", ".pdf");
        try {
            // TODO: Kontrollera licenskraven för iText innan produktion.
            try (InputStream inputStream = file.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(tempFile);
                 PdfDocument pdfDocument = new PdfDocument(new PdfReader(inputStream), new PdfWriter(outputStream))) {
                PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, false);
                if (form != null) {
                    form.flattenFields();
                }
            }

            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            try (var responseStream = response.getOutputStream()) {
                Files.copy(tempFile.toPath(), responseStream);
            }
        } catch (Exception e) {
            if (!response.isCommitted()) {
                response.reset();
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setHeader("X-Error", truncateError(e));
        } finally {
            if (tempFile.exists()) {
                Files.deleteIfExists(tempFile.toPath());
            }
        }
    }

    private String truncateError(Exception e) {
        String message = e.getMessage();
        String details = e.getClass().getSimpleName() + ": " + (message == null ? "" : message);
        if (details.length() > 200) {
            return details.substring(0, 200);
        }
        return details;
    }
}
