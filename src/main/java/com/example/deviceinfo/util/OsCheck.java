package com.example.deviceinfo.util;

public final class OsCheck {
    /**

     * types of Operating Systems

     */
    public enum OSType {
        Windows, MacOS, Linux, Other
    }
    protected static OSType detectedOS;
    /**

     * detected the operating system from the os.name System property and cache

     * the result

     *

     * @returns - the operating system detected

     */
    public static OSType getOperatingSystemType() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase();
            if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                detectedOS = OSType.MacOS;
            } else if (OS.indexOf("nux") >= 0) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}