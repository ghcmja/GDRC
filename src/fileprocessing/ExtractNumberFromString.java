/*
 * Copyright (C) 2017 GY
 *
 * Source code is based on Dave Gonner's free gerber2png work.
 * see gerber2png at <https://github.com/dgonner/gerber2png>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fileprocessing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GY
 */
public class ExtractNumberFromString {

    private List<String> extract = new ArrayList<>();

    /**
     *
     * @param extract
     * @param isXYD
     */
    
    // if extract is a line, List will fill with its items
    // else it has only one element: the number.
    // yes, it is easier to use regex and matches, but i think
    // this method is much faster.
    public ExtractNumberFromString(String extract) {
        String currentVal = "";

        for (int i = 0; i < extract.length(); i++) {
            if (extract.toUpperCase().charAt(i) == 'X') {
                if (!currentVal.isEmpty()) {
                    this.extract.add(currentVal);
                }
                currentVal = "X";
                continue;
            }
            if (extract.toUpperCase().charAt(i) == 'Y') {
                if (!currentVal.isEmpty()) {
                    this.extract.add(currentVal);
                }
                currentVal = "Y";
                continue;
            }
            if (extract.toUpperCase().charAt(i) == 'D') {
                if (!currentVal.isEmpty()) {
                    this.extract.add(currentVal);
                }
                currentVal = "D";
                continue;
            }

            switch (extract.charAt(i)) {
                // valid characters
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '+':
                case '-':
                case '.':
                    currentVal += extract.charAt(i);
                    break;

                // end of line
                //case '*':
                default:
                    // end of data
                    this.extract.add(currentVal);
                    currentVal = "";
                    break;
            }
        }
    }

    public List<String> extracted() {
        return this.extract;
    }
    
    public String extractedNumberAsString() {
        if (this.extract.size() == 1) {
            return this.extract.get(0);
        }
        return null;
    }

    // Double wrapper used!!
    public Double extractedNumberAsDouble() {
        if (this.extract.size() == 1) {
            return Double.valueOf(this.extract.get(0));
        }
        return null;
    }
}
