/* 
 * Copyright (C) 2021 shadow
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
package otd.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author
 */
public class IProperty {
    String value;
    public IProperty(String value) {
        this.value = value;
    }
    
    public int getInt() {
        try {
            return Integer.parseInt(value);
        } catch(Exception ex) {
            return 0;
        }
    }
    
    public boolean getBoolean(Boolean bool) {
        try {
            return Boolean.parseBoolean(value);
        } catch(Exception ex) {
            return bool;
        }
    }
    
    public String[] getStringList() {
        List<String> list = new ArrayList<>();
        String[] array = value.split(",");
        for(String sub : array) {
            try {
                list.add(sub);
            } catch(Exception ex) {
                
            }
        }
        String[] res = new String[list.size()];
        for(int i = 0; i < res.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }
    
    public int[] getIntList() {
        List<Integer> list = new ArrayList<>();
        String[] array = value.split(",");
        for(String sub : array) {
            try {
                list.add(Integer.parseInt(sub));
            } catch(Exception ex) {
                
            }
        }
        int[] res = new int[list.size()];
        for(int i = 0; i < res.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }
}
