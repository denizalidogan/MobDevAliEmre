package com.pxl.teamy.DomainClasses;

public  final class  Statics {

    private static boolean  isLandscape ;

    private static String eventId;

    public static boolean isIsLandscape() {
        return isLandscape;
    }

    public static void setIsLandscape(boolean isLandscape) {
        Statics.isLandscape = isLandscape;
    }


    public static String getEventId() {
        return eventId;
    }

    public static void setEventId(String eventId) {
        Statics.eventId = eventId;
    }




}
