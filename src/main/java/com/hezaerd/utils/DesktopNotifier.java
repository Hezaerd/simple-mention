package com.hezaerd.utils;

import java.awt.*;

/**
 * Utility class for showing desktop notifications.
 * Handles system tray notifications with proper error handling and resource management.
 */
public final class DesktopNotifier {
    private static boolean isSupported;
    private static TrayIcon trayIcon;

    static {
        initializeSystemTray();
    }

    private static void initializeSystemTray() {
        try {
            if (!SystemTray.isSupported()) {
                ModLib.LOGGER.warn("System tray is not supported on this platform");
                isSupported = false;
                return;
            }

            SystemTray tray = SystemTray.getSystemTray();
            Image img = Toolkit.getDefaultToolkit().createImage(
                    DesktopNotifier.class.getResource("/assets/simple-mention/icon.png"));

            if (img == null) {
                ModLib.LOGGER.warn("Could not load notification icon");
                // Create a simple fallback icon
                img = createFallbackIcon();
            }

            trayIcon = new TrayIcon(img, "Simple Mention");
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
            isSupported = true;

            ModLib.LOGGER.debug("Desktop notifications initialized successfully");
        } catch (AWTException e) {
            ModLib.LOGGER.warn("Failed to initialize system tray", e);
            isSupported = false;
        } catch (Exception e) {
            ModLib.LOGGER.warn("Unexpected error initializing desktop notifications", e);
            isSupported = false;
        }
    }

    private static Image createFallbackIcon() {
        // Create a simple 16x16 icon as fallback
        return Toolkit.getDefaultToolkit().createImage(new java.awt.image.MemoryImageSource(16, 16,
            new int[256], 0, 16));
    }

    /**
     * Shows a desktop notification.
     *
     * @param title The notification title
     * @param message The notification message
     */
    public static void notify(String title, String message) {
        if (!isSupported || trayIcon == null) {
            return;
        }

        try {
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
        } catch (Exception e) {
            ModLib.LOGGER.warn("Failed to show desktop notification", e);
        }
    }

    /**
     * Cleans up system tray resources.
     * Should be called when the mod is being unloaded.
     */
    public static void cleanup() {
        if (isSupported && trayIcon != null) {
            try {
                SystemTray.getSystemTray().remove(trayIcon);
                ModLib.LOGGER.debug("Desktop notification cleanup completed");
            } catch (Exception e) {
                ModLib.LOGGER.warn("Failed to cleanup desktop notifications", e);
            }
        }
    }

    /**
     * Checks if desktop notifications are supported and available.
     *
     * @return true if desktop notifications are available, false otherwise
     */
    public static boolean isAvailable() {
        return isSupported && trayIcon != null;
    }
}
