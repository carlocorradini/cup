package it.unitn.disi.wp.cup.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * QR Code Utility class for generating QR Codes
 *
 * @author Carlo Corradini
 */
public final class QRCodeUtil {

    private static final Logger LOGGER = Logger.getLogger(QRCodeUtil.class.getName());
    private static final int DEFAULT_DIMENSION = 100;
    private static final float QR_IMAGE_OVERLAY_RATIO = 0.2f;

    /**
     * Generate the QR Code as an {@link BufferedImage image} given {@code message} and {@code dimension}.
     * Add the {@code overlay} in center of the QR Code.
     * The resulted {@link BufferedImage image} will be {@code dimension} * {@code dimension} px.
     *
     * @param message   The message of the QR Code
     * @param dimension The dimension of the QR Code
     * @param overlay   The centered overlay image
     * @return An {@link BufferedImage image} representing the generated QR Code
     * @throws NullPointerException If {@code message} is null
     */
    public static BufferedImage generate(String message, int dimension, BufferedImage overlay) throws NullPointerException {
        if (message == null || overlay == null)
            throw new NullPointerException("Message and overlay cannot be null");
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix;
        BufferedImage qrImage;
        BufferedImage output = null;

        // Set Hints
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        try {
            // Create QR Code
            bitMatrix = writer.encode(message, BarcodeFormat.QR_CODE, dimension, dimension, hints);
            // Load QR Code Image
            qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(QRCodeColor.WHITE.getArgb(), QRCodeColor.BLACK.getArgb()));
            // Add Image Overlay to QR Code
            output = addOverlayImage(qrImage, overlay);
        } catch (WriterException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generate QRCode", ex);
        }

        return output;
    }

    /**
     * Generate the QR Code as an {@link BufferedImage image} given {@code message} and {@code dimension}.
     * The resulted {@link BufferedImage image} will be {@code dimension} * {@code dimension} px.
     *
     * @param message   The message of the QR Code
     * @param dimension The dimension of the QR Code
     * @return An {@link BufferedImage image} representing the generated QR Code
     * @throws NullPointerException If {@code message} is null
     */
    public static BufferedImage generate(String message, int dimension) throws NullPointerException {
        return generate(message, dimension, ImageUtil.getLOGO());
    }

    /**
     * Generate the QR Code as an {@link BufferedImage image} given the {@code message} only.
     * The resulted {@link BufferedImage image} will be {@code DEFAULT_DIMENSION} * {@code DEFAULT_DIMENSION} px.
     *
     * @param message The message of the QR Code
     * @return An {@link BufferedImage image} representing the generated QR Code
     * @throws NullPointerException If {@code message} is null
     */
    public static BufferedImage generate(String message) throws NullPointerException {
        return generate(message, DEFAULT_DIMENSION);
    }

    private static BufferedImage addOverlayImage(BufferedImage qrCodeImage, BufferedImage overlayImage) throws NullPointerException, IOException {
        if (qrCodeImage == null || overlayImage == null)
            throw new NullPointerException("QR Code Image or Overlay Image cannot be null");

        BufferedImage combined;
        BufferedImage scaledOverlayImage = scaleOverlayImage(qrCodeImage, overlayImage);
        int deltaWidth = qrCodeImage.getWidth() - scaledOverlayImage.getWidth();
        int deltaHeight = qrCodeImage.getHeight() - scaledOverlayImage.getHeight();
        combined = new BufferedImage(qrCodeImage.getWidth(), qrCodeImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) combined.getGraphics();
        g2.drawImage(qrCodeImage, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawImage(scaledOverlayImage, Math.round(deltaWidth / 2), Math.round(deltaHeight / 2), null);

        return combined;
    }

    private static BufferedImage scaleOverlayImage(BufferedImage qrCodeImage, BufferedImage overlayImage) throws NullPointerException, IOException {
        if (qrCodeImage == null || overlayImage == null)
            throw new NullPointerException("QR Code Image or Overlay Image cannot be null");

        int scaledWidth = Math.round(qrCodeImage.getWidth() * QR_IMAGE_OVERLAY_RATIO);
        int scaledHeight = Math.round(qrCodeImage.getHeight() * QR_IMAGE_OVERLAY_RATIO);
        BufferedImage imageBuff = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = imageBuff.createGraphics();
        g.drawImage(overlayImage.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH), 0, 0, new Color(0, 0, 0), null);
        g.dispose();
        return imageBuff;
    }

    /**
     * Available QR Code Colors
     */
    public enum QRCodeColor {
        BLUE(0xFF40BAD0),
        RED(0xFFE91C43),
        PURPLE(0xFF8A4F9E),
        ORANGE(0xFFF4B13D),
        WHITE(0xFFFFFFFF),
        BLACK(0xFF000000);

        private final int argb;

        QRCodeColor(final int argb) {
            this.argb = argb;
        }

        public int getArgb() {
            return argb;
        }
    }
}
