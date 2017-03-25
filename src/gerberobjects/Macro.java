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
package gerberobjects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GY
 */
public class Macro {
    private String macroName;
    private List<String> macroLines;

    public Macro() {
        macroLines = new ArrayList<>();
    }

    public String getMacroName() {
        return macroName;
    }

    public void setMacroName(String macroName) {
        this.macroName = macroName;
    }
    
    public void addMacroLine(String line) {
        macroLines.add(line);
    }
    
    public String getMacroLine(int index) {
        return macroLines.get(index);
    }
}
