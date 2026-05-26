package util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SimpleJSon {
    private final Object jsonData;

    public SimpleJSon(String jsonString) {
        jsonString = jsonString.strip();
        int stringLength = jsonString.length();
        Counter stringIndex = new Counter();

        this.jsonData = parseNextChunk(jsonString, stringIndex);

        System.out.println(jsonData);
    }

    private Object parseNextChunk(String jsonString, Counter stringIndex) {
        char firstChar = goToFirstNonWhiteSpace(jsonString, stringIndex);

        stringIndex.step();

        if(firstChar == '{') {
            HashMap<String, Object> data = new HashMap<>();
            
            while (jsonString.charAt(stringIndex.get()) != '}') {
                String key = readString(jsonString, stringIndex);
                stringIndex.step(); // Skip colon
                Object value = parseNextChunk(jsonString, stringIndex);

                data.put(key, value);
            }

            return data;
        } else if(firstChar == '[') {
            return null;
        } else if(firstChar == '"' || firstChar == '\'') {
            return readString(jsonString, stringIndex);
        } else {
            return null;
        }
    }

    private char goToFirstNonWhiteSpace(String jsonString, Counter stringIndex) {
        char currentChar = jsonString.charAt(stringIndex.get());
        
        while (currentChar == ' ' || currentChar == '\n') {
            stringIndex.step();
            currentChar = jsonString.charAt(stringIndex.get());
        }

        return currentChar;
    }

    private String readString(String jsonString, Counter stringIndex) {
        StringBuilder builder = new StringBuilder();

        char quotationChar = readChar(jsonString, stringIndex);
        char currentChar = readChar(jsonString, stringIndex);

        while (currentChar != quotationChar) {
            builder.append(currentChar);
            stringIndex.step();
            currentChar = readChar(jsonString, stringIndex);
        }

        stringIndex.step(); // Go past the closing quotation mark

        return builder.toString();
    }

    private char readChar(String jsonString, Counter stringIndex) {
        char v = jsonString.charAt(stringIndex.get());
        stringIndex.step();

        return v;
    }
}
