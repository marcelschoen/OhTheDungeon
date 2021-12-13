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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author
 */
public class Configuration {
    public List<String> Category = new ArrayList<>();
    public List<Map<String, String>> Content = new ArrayList<>();
    public File file;
    public Configuration(File file) {
        this.file = file;
    }
    
    public void addCustomCategoryComment(String name, String comment) {
        if(Category.contains(name)) return;
        Category.add(name);
        Content.add(new HashMap<>());
    }
    
    public IProperty get(String category, String key, int dvalue, String comment) {
        if(!Category.contains(category)) {
            Category.add(category);
            Map<String, String> sub = new HashMap<>();
            sub.put(key, Integer.toString(dvalue));
            Content.add(sub);
            return new IProperty(Integer.toString(dvalue));
        }
        int index = Category.indexOf(category);
        Map<String, String> dict = Content.get(index);
        if(!dict.containsKey(key)) {
            dict.put(key, Integer.toString(dvalue));
            return new IProperty(Integer.toString(dvalue));
        }
        return new IProperty(dict.get(key));
    }
    
    public IProperty get(String category, String key, int[] array, String comment) {
        if(!Category.contains(category)) {
            Category.add(category);
            Map<String, String> sub = new HashMap<>();
            sub.put(key, intArrayToString(array));
            Content.add(sub);
            return new IProperty(intArrayToString(array));
        }
        int index = Category.indexOf(category);
        Map<String, String> dict = Content.get(index);
        if(!dict.containsKey(key)) {
            dict.put(key, intArrayToString(array));
            return new IProperty(intArrayToString(array));
        }
        return new IProperty(dict.get(key));
    }
    
    public IProperty get(String category, String key, boolean bool, String comment) {
        if(!Category.contains(category)) {
            Category.add(category);
            Map<String, String> sub = new HashMap<>();
            sub.put(key, Boolean.toString(bool));
            Content.add(sub);
            return new IProperty(Boolean.toString(bool));
        }
        int index = Category.indexOf(category);
        Map<String, String> dict = Content.get(index);
        if(!dict.containsKey(key)) {
            dict.put(key, Boolean.toString(bool));
            return new IProperty(Boolean.toString(bool));
        }
        return new IProperty(dict.get(key));
    }
    
    public IProperty get(String category, String key, String[] array, String comment) {
        if(!Category.contains(category)) {
            Category.add(category);
            Map<String, String> sub = new HashMap<>();
            sub.put(key, StringArrayToString(array));
            Content.add(sub);
            return new IProperty(StringArrayToString(array));
        }
        int index = Category.indexOf(category);
        Map<String, String> dict = Content.get(index);
        if(!dict.containsKey(key)) {
            dict.put(key, StringArrayToString(array));
            return new IProperty(StringArrayToString(array));
        }
        return new IProperty(dict.get(key));
    }
    
    private static String intArrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for(int value : array) {
            sb.append(value);
            sb.append(',');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
    
    private static String StringArrayToString(String[] array) {
        StringBuilder sb = new StringBuilder();
        for(String value : array) {
            sb.append(value);
            sb.append(',');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
    
    public int getInt(String name, String category, int defaultValue, int minValue, int maxValue, String comment) {
        if(!Category.contains(category)) {
            Category.add(category);
            Map<String, String> sub = new HashMap<>();
            sub.put(name, Integer.toString(defaultValue));
            Content.add(sub);
            return defaultValue;
        }
        int index = Category.indexOf(category);
        Map<String, String> dict = Content.get(index);
        if(!dict.containsKey(name)) {
            dict.put(name, Integer.toString(defaultValue));
            return defaultValue;
        }
        String tmp = dict.get(name);
        int res;
        try {
            res = Integer.parseInt(tmp);
        } catch (NumberFormatException ex) {
            res = defaultValue;
        }
        
        if(res < minValue) res = minValue;
        if(res > maxValue) res = maxValue;
        return res;
    }
    
    public void load() {
        Configuration cf;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            cf = (new Gson()).fromJson(sb.toString(), Configuration.class);
        } catch (Exception ex) {
            return;
        }
        if(cf == null) return;
        this.Category = cf.Category;
        this.Content = cf.Content;
    }
    
    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        try(OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8")) {
            oStreamWriter.append(json);
            oStreamWriter.close();
        } catch (IOException ex) {
            
        }
    }
}
