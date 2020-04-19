package codility_extra;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Formats a number with grouping separators.
 */
public class Problem03 {
    private static final int GROUP_LENGTH = 3;

    public String solve(int number) {
//      lazy answer:
        DecimalFormat decimalFormat = new DecimalFormat("", DecimalFormatSymbols.getInstance(Locale.US));
        return decimalFormat.format(number);
        
        // work-in-progress
//        StringBuilder result = new StringBuilder();
//        for (;number > 0; number = number / 1000) {
//            String group = Integer.toString(number % 1000);
//            result.insert(0, group);
//            if (number > 1000) {
//                for (int i = 0; i < GROUP_LENGTH - group.length(); i++) {
//                    result.insert(0, "0");    
//                }s
//                result.insert(0, ",");
//            }
//        }
//        return result.toString();
    }
}
