package com.exmple.videoplayer_codewithnitish.Parsers;

import java.time.Duration;
import java.util.ArrayList;

public class StrParser {
    Duration duration;

    public static ArrayList<Str> parse(String strFileContent) {
        ArrayList<Str> list = new ArrayList<>();
        String[] lines = strFileContent.split("\n");
        ArrayList<String> tempList = new ArrayList<>();

        for (String line : lines) {
            if (!line.trim().equals("")) {
                tempList.add(line);
            } else {
                if (tempList.size() >= 2) {
                    list.add(getStr(tempList));
                }
                tempList.clear();
            }
        }
        if (tempList.size() >= 2)
            list.add(getStr(tempList));

        return list;
    }

    private static Str getStr(ArrayList<String> tempList) {
        int num = 0;
        String[] times = tempList.get(1).trim().split(" --> ");
        StringBuilder text = new StringBuilder();
        for (int i = 2; i < tempList.size(); i++) {
            text.append(tempList.get(i).trim());
        }
        return new Str(num, durationOf(times[0]), durationOf(times[1]), text.toString().trim());
    }

    private static int durationOf(String time) {
        String[] timeS = time.trim().split(":");
        int hrs = Short.parseShort(timeS[0]) * 1000 * 60 * 60;
        int min = Short.parseShort(timeS[1]) * 1000 * 60;

        String[] sec_msS = timeS[2].split(",");
        int sec = Short.parseShort(sec_msS[0]) * 1000;
        int ms = Short.parseShort(sec_msS[1]);
        return hrs + min + sec + ms;
    }
}
