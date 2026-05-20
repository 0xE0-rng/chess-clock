package fr.kazalox.android.gameclockdeluxe.utils;

import fr.kazalox.android.gameclockdeluxe.C;

public class TimeUtils {
    public static int[] splitTime(int time) {
        int r = time % 3600;
        int[] t = {time / 3600, r / 60, r % 60};
        return t;
    }

    public static int[] splitTimeMillis(long time) {
        int r = (int) (time % C.HOUR);
        int s = r % 60000;
        int[] t = {(int) (time / C.HOUR), r / 60000, s / 1000, s % 1000};
        return t;
    }

    public static int joinTime(int h, int m, int s) {
        return (h * 3600) + (m * 60) + s;
    }

    public static String toClockString(long millis, boolean showSemicolon) {
        boolean bNeg = false;
        if (millis < 0) {
            bNeg = true;
            millis = Math.abs(millis);
        }
        StringBuilder timeFormatted = new StringBuilder();
        StringBuilder minutesText = new StringBuilder();
        StringBuilder secondsText = new StringBuilder();
        StringBuilder centisecondsText = new StringBuilder();
        int seconds = (int) (millis / 1000);
        int milliseconds = (int) (millis % 1000);
        int centiseconds = milliseconds / 10;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int seconds2 = seconds % 60;
        String separator = showSemicolon ? ":" : " ";
        if (hours > 0) {
            minutesText.append(minutes < 10 ? "0" + minutes : "" + minutes);
            timeFormatted.append(hours + separator).append((CharSequence) minutesText);
        } else if (minutes > 0) {
            secondsText.append(seconds2 < 10 ? "0" + seconds2 : "" + seconds2);
            timeFormatted.append(minutes + separator).append((CharSequence) secondsText);
        } else {
            centisecondsText.append(centiseconds < 10 ? "0" + centiseconds : "" + centiseconds);
            timeFormatted.append(seconds2 + separator).append((CharSequence) centisecondsText);
        }
        if (bNeg) {
            timeFormatted.insert(0, "-");
        }
        return timeFormatted.toString();
    }

    public static String timeToString(int time) {
        int h = time / 3600;
        int r = time % 3600;
        int m = r / 60;
        int s = r % 60;
        StringBuilder sb = new StringBuilder();
        if (h > 0) {
            sb.append(h).append(':');
        }
        if (m < 10) {
            sb.append('0');
        }
        sb.append(m).append(':');
        if (s < 10) {
            sb.append('0');
        }
        sb.append(s);
        return sb.toString();
    }

    public static int stringToTime(String timeString) {
        int m;
        int s;
        String[] tab = timeString.split(":");
        int h = 0;
        if (tab.length == 2) {
            m = Integer.parseInt(tab[0]);
            s = Integer.parseInt(tab[1]);
        } else {
            h = Integer.parseInt(tab[0]);
            m = Integer.parseInt(tab[1]);
            s = Integer.parseInt(tab[2]);
        }
        return joinTime(h, m, s);
    }
}
