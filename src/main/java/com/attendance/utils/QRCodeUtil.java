package com.attendance.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

/**
 * QRCodeUtil - QR code generation using ZXing library
 * 
 * Usage:
 *   String qrData = "SESSION|ATT_20251017_5_123|abc123";
 *   byte[] qrImage = QRCodeUtil.generateQRCodeImage(qrData, 300, 300);
 *   String base64QR = QRCodeUtil.generateQRCodeBase64(qrData, 300, 300);
 * 
 * @author QR Attendance System
 * @version 1.0
 */
public class QRCodeUtil {

    // Default QR code dimensions
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    /**
     * Generate QR code as byte array (PNG image)
     * 
     * @param data Data to encode in QR code
     * @param width QR code width in pixels
     * @param height QR code height in pixels
     * @return PNG image as byte array
     * @throws Exception if QR generation fails
     */
    public static byte[] generateQRCodeImage(String data, int width, int height) throws Exception {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("QR code data cannot be null or empty");
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    /**
     * Generate QR code as Base64 encoded string (for embedding in HTML)
     * 
     * @param data Data to encode in QR code
     * @param width QR code width in pixels
     * @param height QR code height in pixels
     * @return Base64 encoded PNG image string
     * @throws Exception if QR generation fails
     */
    public static String generateQRCodeBase64(String data, int width, int height) throws Exception {
        byte[] qrImageBytes = generateQRCodeImage(data, width, height);
        return Base64.getEncoder().encodeToString(qrImageBytes);
    }

    /**
     * Generate QR code with default dimensions (300x300)
     * 
     * @param data Data to encode in QR code
     * @return Base64 encoded PNG image string
     * @throws Exception if QR generation fails
     */
    public static String generateQRCodeBase64(String data) throws Exception {
        return generateQRCodeBase64(data, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Save QR code to file
     * 
     * @param data Data to encode in QR code
     * @param filePath Path where to save QR code image
     * @param width QR code width in pixels
     * @param height QR code height in pixels
     * @throws Exception if QR generation or file write fails
     */
    public static void saveQRCodeToFile(String data, String filePath, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    /**
     * Generate QR code data payload for attendance session
     * Format: sessionCode|checksum|timestamp
     * 
     * @param sessionCode Session code (e.g., ATT_20251017_5_123)
     * @param checksum SHA-256 checksum for validation
     * @param timestamp Current timestamp
     * @return Formatted QR data string
     */
    public static String formatQRData(String sessionCode, String checksum, long timestamp) {
        return String.format("%s|%s|%d", sessionCode, checksum, timestamp);
    }

    /**
     * Parse QR code data payload
     * 
     * @param qrData Scanned QR code data
     * @return Array [sessionCode, checksum, timestamp] or null if invalid
     */
    public static String[] parseQRData(String qrData) {
        if (qrData == null || !qrData.contains("|")) {
            return null;
        }

        String[] parts = qrData.split("\\|");
        if (parts.length >= 2) {
            return parts;
        }

        return null;
    }

    /**
     * Validate QR code data format
     * 
     * @param qrData QR code data to validate
     * @return true if format is valid
     */
    public static boolean isValidQRData(String qrData) {
        String[] parts = parseQRData(qrData);
        return parts != null && parts.length >= 2;
    }
}
