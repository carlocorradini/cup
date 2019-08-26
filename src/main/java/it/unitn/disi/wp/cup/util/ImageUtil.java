package it.unitn.disi.wp.cup.util;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class for retrieving useful Application images
 *
 * @author Carlo Corradini
 */
public final class ImageUtil {

    private static final Logger LOGGER = Logger.getLogger(ImageUtil.class.getName());
    public static ServletContext SERVLET_CONTEXT = null;
    private static BufferedImage LOGO = null;
    private static BufferedImage OK = null;
    private static BufferedImage OK_NOT = null;

    /**
     * Configure the class
     *
     * @param servletContext The {@link ServletContext} get information from
     * @throws NullPointerException If servletContext is null
     */
    public static void configure(final ServletContext servletContext) throws NullPointerException {
        if (servletContext == null)
            throw new NullPointerException("ServletContext cannot be null");

        SERVLET_CONTEXT = servletContext;

        try {
            LOGO = ImageIO.read(new File(FilenameUtils.separatorsToUnix(SERVLET_CONTEXT.getRealPath("/") + "assets/_default/images/favicon/android-chrome-512x512.png")));
            OK = ImageIO.read(new File(FilenameUtils.separatorsToUnix(SERVLET_CONTEXT.getRealPath("/") + "assets/_default/images/ui/ok.png")));
            OK_NOT = ImageIO.read(new File(FilenameUtils.separatorsToUnix(SERVLET_CONTEXT.getRealPath("/") + "assets/_default/images/ui/ok_not.png")));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to retrieve images for ImageUtil", ex);
        }
    }

    private static void isConfigured() throws NullPointerException {
        if (SERVLET_CONTEXT == null)
            throw new NullPointerException("ImageUtil has not been configured");
    }

    /**
     * Return the real Image Path on the Server
     *
     * @return Image Path on the Server
     * @throws NullPointerException If ImageUtil has not been configured
     */
    public static String getImagePath() throws NullPointerException {
        isConfigured();
        return SERVLET_CONTEXT.getRealPath("/") + "/assets/_default";
    }

    /**
     * Return the {@link BufferedImage image} representing the Logo of the Application
     *
     * @return The Logo of the Application
     * @throws NullPointerException If ImageUtil has not been configured
     */
    public static BufferedImage getLOGO() throws NullPointerException {
        isConfigured();
        return LOGO;
    }

    /**
     * Return the {@link BufferedImage image} representing the OK image
     *
     * @return The OK image
     * @throws NullPointerException If ImageUtil has not been configured
     */
    public static BufferedImage getOK() throws NullPointerException {
        isConfigured();
        return OK;
    }

    /**
     * Return the {@link BufferedImage image} representing the OK NOT image
     *
     * @return The OK NOT image
     * @throws NullPointerException If ImageUtil has not been configured
     */
    public static BufferedImage getOkNot() throws NullPointerException {
        isConfigured();
        return OK_NOT;
    }
}
