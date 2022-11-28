package com.info5059.casestudy.purchaseorder;

import com.info5059.casestudy.product.Product;
import com.info5059.casestudy.product.ProductRepository;
import com.info5059.casestudy.product.QRCodeGenerator;
import com.info5059.casestudy.vendor.Vendor;
import com.info5059.casestudy.vendor.VendorRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class PurchaseOrderPDFGenerator extends AbstractPdfView {
        public static ByteArrayInputStream generatePurchaseOrder(String repid,
                        PurchaseOrderRepository purchaseOrderRepository,
                        VendorRepository vendorRepository,
                        ProductRepository productRepository)
                        throws IOException {
                URL imageUrl = PurchaseOrderPDFGenerator.class.getResource("/static/images/logo.jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(baos);
                PdfDocument pdf = new PdfDocument(writer);
                // Document is the main object
                Document document = new Document(pdf);
                PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                Image img = new Image(ImageDataFactory.create(imageUrl)).scaleAbsolute(120, 120)
                                .setFixedPosition(50, 700);
                document.add(img);
                document.add(new Paragraph("\n"));
                Locale locale = new Locale("en", "US");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
                BigDecimal tot = new BigDecimal(0.0);
                BigDecimal ExtPricetot = new BigDecimal(0.0);
                BigDecimal tax = new BigDecimal(0.13);
                BigDecimal subTax = new BigDecimal(0.0);
                BigDecimal POtot = new BigDecimal(0.0);
                try {
                        document.add(new Paragraph("\n"));
                        Optional<PurchaseOrder> optPurchaseOrder = purchaseOrderRepository
                                        .findById(Long.parseLong(repid));
                        if (optPurchaseOrder.isPresent()) {
                                PurchaseOrder purchaseOrder = optPurchaseOrder.get();
                                document.add(new Paragraph("Purchase Order# " + repid).setFont(font).setFontSize(18)
                                                .setBold()
                                                .setMarginTop(-30)
                                                .setMarginLeft(150)
                                                .setMarginRight(100)
                                                .setTextAlignment(TextAlignment.RIGHT));
                                document.add(new Paragraph("\n\n"));
                                Optional<Vendor> optVendor = vendorRepository.findById(purchaseOrder.getVendorid());
                                if (optVendor.isPresent()) {
                                        Vendor vendor = optVendor.get();
                                        Image qrcode = addSummaryQRCode(vendor, purchaseOrder);
                                        document.add(qrcode);
                                        Table vendorInfoTable = new Table(2);
                                        vendorInfoTable.setWidth(new UnitValue(UnitValue.PERCENT, 30));
                                        vendorInfoTable.setMarginTop(20);

                                        Cell cell = new Cell().add(new Paragraph("Vendor:")
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getName())
                                                        .setFontSize(12)
                                                        .setFont(font)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(" ")
                                                        .setFont(font)
                                                        .setFontSize(12))
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getAddress1())
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(" ")
                                                        .setFont(font)
                                                        .setFontSize(12))
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getCity())
                                                        .setFontSize(12)
                                                        .setFont(font)
                                                        .setBold())
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(" ")
                                                        .setFont(font)
                                                        .setFontSize(12))
                                                        .setTextAlignment(TextAlignment.LEFT)
                                                        .setBorder(Border.NO_BORDER);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getProvince())
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(" ")
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.RIGHT);
                                        vendorInfoTable.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getEmail())
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.RIGHT)
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                                        vendorInfoTable.addCell(cell);
                                        document.add(vendorInfoTable);
                                }
                                document.add(new Paragraph("\n\n"));

                                Table productInfoTable = new Table(5);
                                productInfoTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));

                                Cell cell = new Cell().add(new Paragraph("Product Code")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold())
                                                .setTextAlignment(TextAlignment.CENTER);
                                productInfoTable.addCell(cell);
                                cell = new Cell().add(new Paragraph("Description")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold())
                                                .setTextAlignment(TextAlignment.CENTER);
                                productInfoTable.addCell(cell);
                                cell = new Cell().add(new Paragraph("Qty Sold")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold())
                                                .setTextAlignment(TextAlignment.CENTER);
                                productInfoTable.addCell(cell);
                                cell = new Cell().add(new Paragraph("Price")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold())
                                                .setTextAlignment(TextAlignment.CENTER);
                                productInfoTable.addCell(cell);
                                cell = new Cell().add(new Paragraph("Ext. Price")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold())
                                                .setTextAlignment(TextAlignment.CENTER);
                                productInfoTable.addCell(cell);
                                for (PurchaseOrderLineitem line : purchaseOrder.getItems()) {
                                        Optional<Product> optx = productRepository.findById(line.getProductid());
                                        if (optx.isPresent()) {
                                                Product product = optx.get();

                                                cell = new Cell().add(new Paragraph(product.getId())
                                                                .setFont(font)
                                                                .setFontSize(12))
                                                                .setTextAlignment(TextAlignment.CENTER);
                                                productInfoTable.addCell(cell);

                                                cell = new Cell().add(new Paragraph(product.getName())
                                                                .setFont(font)
                                                                .setFontSize(12))
                                                                .setTextAlignment(TextAlignment.CENTER);
                                                productInfoTable.addCell(cell);
                                                cell = new Cell().add(new Paragraph(Integer.toString(line.getQty()))
                                                                .setFont(font)
                                                                .setFontSize(12))
                                                                .setTextAlignment(TextAlignment.RIGHT);
                                                productInfoTable.addCell(cell);
                                                cell = new Cell().add(
                                                                new Paragraph(formatter.format(product.getCostprice()))
                                                                                .setFont(font)
                                                                                .setFontSize(12))
                                                                .setTextAlignment(TextAlignment.RIGHT);
                                                productInfoTable.addCell(cell);

                                                ExtPricetot = product.getCostprice().multiply(
                                                                BigDecimal.valueOf(line.getQty()),
                                                                new MathContext(8, RoundingMode.UP));
                                                cell = new Cell().add(
                                                                new Paragraph(formatter.format(ExtPricetot))
                                                                                .setFont(font)
                                                                                .setFontSize(12))
                                                                .setTextAlignment(TextAlignment.RIGHT);
                                                productInfoTable.addCell(cell);
                                                tot = tot.add(ExtPricetot, new MathContext(8, RoundingMode.UP));
                                                subTax = tot.multiply(tax, new MathContext(8, RoundingMode.UP));
                                                POtot = tot.add(subTax, new MathContext(8, RoundingMode.UP));

                                        } // end if
                                } // end for

                                cell = new Cell(1, 4).add(new Paragraph("Sub Total:"))
                                                .setBorder(Border.NO_BORDER)
                                                .setTextAlignment(TextAlignment.RIGHT);
                                productInfoTable.addCell(cell);
                                cell = new Cell().add(new Paragraph(formatter.format(tot)))
                                                .setTextAlignment(TextAlignment.RIGHT)
                                                .setBackgroundColor(ColorConstants.YELLOW);
                                productInfoTable.addCell(cell);

                                cell = new Cell(1, 4).add(new Paragraph("Tax:"))
                                                .setBorder(Border.NO_BORDER)
                                                .setTextAlignment(TextAlignment.RIGHT);
                                productInfoTable.addCell(cell);
                                cell = new Cell().add(new Paragraph(formatter.format(subTax)))
                                                .setTextAlignment(TextAlignment.RIGHT)
                                                .setBackgroundColor(ColorConstants.YELLOW);
                                productInfoTable.addCell(cell);

                                cell = new Cell(1, 4).add(new Paragraph("PO Total:"))
                                                .setBorder(Border.NO_BORDER)
                                                .setTextAlignment(TextAlignment.RIGHT);
                                productInfoTable.addCell(cell);
                                cell = new Cell().add(new Paragraph(formatter.format(POtot)))
                                                .setTextAlignment(TextAlignment.RIGHT)
                                                .setBackgroundColor(ColorConstants.YELLOW);
                                productInfoTable.addCell(cell);
                                document.add(productInfoTable);
                                document.add(new Paragraph("\n\n"));
                                DateTimeFormatter dateFormatter = DateTimeFormatter
                                                .ofPattern("yyyy-MM-dd h:mm a");
                                document.add(new Paragraph(dateFormatter.format(purchaseOrder.getDatecreated()))
                                                .setTextAlignment(TextAlignment.CENTER));
                                document.close();
                        }
                } catch (Exception ex) {
                        Logger.getLogger(PurchaseOrderPDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }

                return new ByteArrayInputStream(baos.toByteArray());
        }

        private static Image addSummaryQRCode(Vendor ven, PurchaseOrder po){
                QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
                Locale locale = new Locale("en", "US");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
                byte[] qrcodebin = qrCodeGenerator.generateQRCode(
                        "Summary for Purchase Order:" + po.getId() + "\nDate:"
                        + dateFormatter.format(po.getPodate()) + "\nVendor:"
                        + ven.getName()
                        + "\nTotal:" + formatter.format(po.getAmount()));

        
                return new Image(ImageDataFactory.create(qrcodebin))
                        .scaleAbsolute(100, 100)
                        .setFixedPosition(460, 60);
        }
}
