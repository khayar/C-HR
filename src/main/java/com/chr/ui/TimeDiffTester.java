package com.chr.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

/* Name of the class has to be "Main" only if the class is public. */

class TimeDiffTester
{
    public static String show(long value, String showAs) {
        if(value == 0) {
            return "";
        } else {
            return Math.abs(value) +" "+showAs+" ";
        }
    }
    public static void getDifferenceInTime(String time1, String time2) throws java.lang.Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        Date d1 = formatter.parse(time1);
        Date d2 = formatter.parse(time2);
        long timeDiff = d2.getTime() - d1.getTime();

        long diffDays = timeDiff / (24 * 60 * 60 * 1000);
        long diffHours = timeDiff / (60 * 60 * 1000) % 24;
        long diffMinutes = timeDiff / (60 * 1000) % 60;
        long diffSeconds = timeDiff / 1000 % 60;

        String difference = show(diffDays, "days") + show(diffHours, "hours") + show(diffMinutes, "minutes") + show(diffSeconds, "seconds");
        if(diffDays < 0 || diffHours < 0 || diffMinutes < 0 || diffSeconds < 0) {
            System.out.println("-"+difference); 
        } else {
            System.out.println("+"+difference); 
        }
    }
    public static void main (String[] args) throws java.lang.Exception
    {

    	String dateStart = "01/14/2012 09:00:00";
        String dateStop = "01/15/2012 20:09:00";

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
