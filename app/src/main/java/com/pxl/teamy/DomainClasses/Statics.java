package com.pxl.teamy.DomainClasses;

public  final class  Statics {

    private static boolean  isLandscape ;

    private static String eventpostId;

    public static boolean isIsLandscape() {
        return isLandscape;
    }

    public static void setIsLandscape(boolean isLandscape) {
        Statics.isLandscape = isLandscape;
    }

    public static String getEventpostId() {
        return eventpostId;
    }

    public static void setEventpostId(String eventpostId) {
        Statics.eventpostId = eventpostId;
    }
}
