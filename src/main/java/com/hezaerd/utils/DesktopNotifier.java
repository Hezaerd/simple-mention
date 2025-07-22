package com.hezaerd.utils;

import net.minecraft.client.MinecraftClient;

import java.awt.*;

public final class DesktopNotifier {
    private static boolean isSupported;
    
    static {
        if (SystemTray.isSupported()) {
            isSupported = true;
            SystemTray tray = SystemTray.getSystemTray();
            Image img = Toolkit.getDefaultToolkit().createImage(
                    DesktopNotifier.class.getResource("/assets/simplemention/icon.png"));
            TrayIcon trayIcon = new TrayIcon(img, "Simple Mention");
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                isSupported = false;
            }
        }
    }
    
    public static void notify(String title, String message) {
        if (!isSupported || MinecraftClient.getInstance().isWindowFocused()) return;
        TrayIcon[] icons = SystemTray.getSystemTray().getTrayIcons();
        if (icons.length > 0) {
            icons[0].displayMessage(title, message, TrayIcon.MessageType.INFO);
        }
    }
}
