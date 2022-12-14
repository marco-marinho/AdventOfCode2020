package Day04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import static Helpers.Util.readFile;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        var data = readFile("Data/Day04.txt");

        var buffer = new StringBuilder();
        var passData = new ArrayList<String>();
        var passPorts = new ArrayList<HashMap<String, String>>();
        for (var line: data) {
            if (line.length() > 0) buffer.append(line).append(" ");
            else {
                passData.add(buffer.toString().trim().replace("  ", " "));
                buffer.setLength(0);
            }
        }
        passData.add(buffer.toString());

        for (var pass : passData){
            var elements = pass.split(" ");
            var passBuff = new HashMap<String, String>();
            for (var element: elements){
                var pieces = element.split(":");
                passBuff.put(pieces[0], pieces[1]);
            }
            passPorts.add(passBuff);
        }

        var res = passPorts.stream().filter(Main::checkValid).count();
        var res2 = passPorts.stream().filter(Main::checkValidData).count();

        out.println("Task 01: " + res);
        out.println("Task 02: " + res2);

    }

    static boolean checkValid(HashMap<String, String> passport){
        var required = new String[]{"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};
        for( var field : required){
            if (!passport.containsKey(field)) return false;
        }
        return true;
    }

    static boolean checkValidData(HashMap<String, String> passport){
        if (!checkValid(passport)) return false;
        if (!matchFourDigits(passport.get("byr"), 1920, 2002)) return false;
        if (!matchFourDigits(passport.get("iyr"), 2010, 2020)) return false;
        if (!matchFourDigits(passport.get("eyr"), 2020, 2030)) return false;
        if (!matchHeight(passport.get("hgt"))) return false;
        if (!matchHairClr(passport.get("hcl"))) return false;
        if (!matchEyeClr(passport.get("ecl"))) return false;
        if (!matchPid(passport.get("pid"))) return false;
        return true;
    }

    static boolean matchPid(String input){
        var pid = Pattern.compile("^[0-9]{9}$");
        return pid.matcher(input).find();
    }

    static boolean matchHairClr(String input){
        var hcl = Pattern.compile("^#[0-9a-f]{6}$");
        return hcl.matcher(input).find();
    }

    static boolean matchEyeClr(String input){
        var eyeClr = Pattern.compile("^(amb|blu|brn|gry|grn|hzl|oth)$");
        return eyeClr.matcher(input).find();
    }

    static boolean matchFourDigits(String input, int min, int max){
        var fourDigits = Pattern.compile("^\\d{4}$");
        if (!fourDigits.matcher(input).find()) return false;
        if (Integer.parseInt(input) > max) return false;
        if (Integer.parseInt(input) < min) return false;
        return true;
    }

    static boolean matchHeight(String input){
        var heighCm = Pattern.compile("^\\d+?cm$");
        var heighIn = Pattern.compile("^\\d+?in$");
        if (heighCm.matcher(input).find()){
            var value = Integer.parseInt(input.replace("cm", ""));
            if (value >= 150 && value <= 193) return true;
        }
        if (heighIn.matcher(input).find()){
            var value = Integer.parseInt(input.replace("in", ""));
            if (value >= 59 && value <= 76) return true;
        }
        return false;
    }

}