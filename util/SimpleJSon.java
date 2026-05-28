package util;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleJSon {
    private SimpleJSon() {}

    public static Object parse(String jsonString) {
        jsonString = jsonString.strip();
        Counter stringIndex = new Counter();

        return parseNextChunk(jsonString, stringIndex);
    }

    private static Object parseNextChunk(String jsonString, Counter stringIndex) {
        char firstChar = goToFirstNonWhiteSpace(jsonString, stringIndex);

        if(firstChar == '{') {
            stringIndex.step();
            HashMap<String, Object> data = new HashMap<>();

            goToFirstNonWhiteSpace(jsonString, stringIndex);
            char lastChar = jsonString.charAt(stringIndex.get());
            
            while (lastChar != '}') {
                String key = readString(jsonString, stringIndex);
                stringIndex.step(); // Skip colon
                Object value = parseNextChunk(jsonString, stringIndex);
                data.put(key, value);

                lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);
                // System.out.println(value.toString() + lastChar);

                if(lastChar == ',') {
                    stringIndex.step();
                    lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);
                }
            }
            stringIndex.step();
            
            return data;
        } else if(firstChar == '[') {
            stringIndex.step();
            ArrayList<Object> data = new ArrayList<>();
            char lastChar = jsonString.charAt(stringIndex.get());

            while (lastChar != ']') {
                Object value = parseNextChunk(jsonString, stringIndex);
                data.add(value);
                lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);

                if(lastChar == ',') {
                    stringIndex.step();
                    lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);
                }
            }

            stringIndex.step();

            return data;
        } else if(firstChar == '"' || firstChar == '\'') {
            return readString(jsonString, stringIndex);
        } else {
            return readPrimitive(jsonString, stringIndex);
        }
    }

    private static Object readPrimitive(String jsonString, Counter stringIndex) {
        int startIndex = stringIndex.get();
        int jsonLength = jsonString.length();

        // Handle keywords
        if(jsonString.startsWith("true", startIndex)) {
            stringIndex.step(4);
            return true;
        } else if(jsonString.startsWith("false", startIndex)) {
            stringIndex.step(5);
            return false;
        } else if(jsonString.startsWith("null", startIndex)) {
            stringIndex.step(4);
            return null;
        }
        
        // Must be a number
        boolean isDouble = false;

        // char currentChar = jsonString.charAt(startIndex);

        while (stringIndex.get() < jsonLength) {
            char currentChar = jsonString.charAt(stringIndex.get());

            if(Character.isDigit(currentChar)) {
                stringIndex.step();
            } else if(currentChar == '.') {
                stringIndex.step();
                isDouble = true;
            } else {
                break;
            }
        }

        String valueStr = jsonString.substring(startIndex, stringIndex.get());
        
        Object value;

        if(isDouble) 
            value = Double.parseDouble(valueStr);
        else 
            value = Integer.parseInt(valueStr);

        return value;
    }

    private static char goToFirstNonWhiteSpace(String jsonString, Counter stringIndex) {
        char currentChar = jsonString.charAt(stringIndex.get());
        
        while (currentChar == ' ' || currentChar == '\r' || currentChar == '\n' || currentChar == '\t') {
            stringIndex.step();
            currentChar = jsonString.charAt(stringIndex.get());
        }

        return currentChar;
    }

    private static String readString(String jsonString, Counter stringIndex) {
        char quotationChar = readChar(jsonString, stringIndex);
        int startIndex = stringIndex.get();
        char currentChar = readChar(jsonString, stringIndex);

        while (currentChar != quotationChar) {
            currentChar = readChar(jsonString, stringIndex);
        }

        return jsonString.substring(startIndex, stringIndex.get() - 1);
    }

    private static char readChar(String jsonString, Counter stringIndex) {
        char v = jsonString.charAt(stringIndex.get());
        stringIndex.step();

        return v;
    }
}
