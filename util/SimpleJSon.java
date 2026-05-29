package util;

import java.util.ArrayList;
import java.util.HashMap;

/** A helper class to parse a JSon String */
public class SimpleJSon {
    private SimpleJSon() {}

    /**
     * Parses a JSon String into a Map/List depending on the original String (Map for JSon object, List for JSon array/list)
     * @param jsonString The JSon as a String
     * @return The JSon
     */
    public static Object parse(String jsonString) {
        jsonString = jsonString.strip(); // Strip any extraneous whitespace

        // This Counter object will be a cursor into the String
        // The String will be parsed like a stream, where the cursor gradually reads through it and parses it.
        // This Counter allows us to keep track of where we are within the stream across the various methods
        Counter stringIndex = new Counter(); 

        // Parse
        return parseNextChunk(jsonString, stringIndex);
    }

    /**
     * Parses a JSon chunk, which is any JSon object, list, or element.
     * This method will leave the Counter at the character right after the chunk
     * 
     * @param jsonString The original JSon as a String
     * @param stringIndex How far we have parsed
     * @return The chunk, which can be a Map, List, String, or primitive
     */
    private static Object parseNextChunk(String jsonString, Counter stringIndex) {
        char firstChar = goToFirstNonWhiteSpace(jsonString, stringIndex);

        if(firstChar == '{') { // JSon object
            stringIndex.step(); // Go past the opening brace
            HashMap<String, Object> data = new HashMap<>(); // The chunk as a Map

            // 'lastChar' is the last character read after a loop iteration
            // Used to check when we've reached the end of the chunk
            char lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);
            
            while (lastChar != '}') { // Closing brace
                // Get the key and value
                String key = readString(jsonString, stringIndex);
                stringIndex.step(); // Skip colon
                Object value = parseNextChunk(jsonString, stringIndex);
                data.put(key, value);

                // Check what the next character is
                lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);

                if(lastChar == ',') { // Skip commas and check the proper end
                    stringIndex.step();
                    lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);
                }
            }

            // Skip past the closing brace
            stringIndex.step();
            
            return data; // Return the chunk
        } else if(firstChar == '[') { // JSon list
            stringIndex.step(); // Go past the opening bracket
            ArrayList<Object> data = new ArrayList<>(); // The chunk as a List

            // Read the explanation for the if-statement above
            char lastChar = jsonString.charAt(stringIndex.get());

            while (lastChar != ']') {
                Object value = parseNextChunk(jsonString, stringIndex); // Current element
                data.add(value);
                lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);

                if(lastChar == ',') {
                    stringIndex.step();
                    lastChar = goToFirstNonWhiteSpace(jsonString, stringIndex);
                }
            }

            // Skip closing bracket
            stringIndex.step();

            return data;
        } else if(firstChar == '"' || firstChar == '\'') { // String
            return readString(jsonString, stringIndex);
        } else { // Primitive
            return readPrimitive(jsonString, stringIndex);
        }
    }

    /**
     * Reads a primitive value, which is either a boolean, int, double, or null.
     * This method will leave the Counter right after the last character of the primitive
     * 
     * @param jsonString The JSon as a String
     * @param stringIndex How far we have parsed
     * @return The primitive value
     */
    private static Object readPrimitive(String jsonString, Counter stringIndex) {
        int startIndex = stringIndex.get(); // Used for string slicing
        int jsonLength = jsonString.length(); // Cache this because calling .length() might be expensive (?)

        // Handle keywords
        if(jsonString.startsWith("true", startIndex)) {
            stringIndex.step(4); // true is 4 letters
            return true;
        } else if(jsonString.startsWith("false", startIndex)) {
            stringIndex.step(5); // false is 5 letters
            return false;
        } else if(jsonString.startsWith("null", startIndex)) {
            stringIndex.step(4); // null is 4 letters
            return null;
        }
        
        // Must be a number
        boolean isDouble = false;

        while (stringIndex.get() < jsonLength) { // Loop through the number characters
            char currentChar = jsonString.charAt(stringIndex.get());

            if(Character.isDigit(currentChar)) { // Number digit
                stringIndex.step();
            } else if(currentChar == '.') { // Decimal, must be a double
                stringIndex.step();
                isDouble = true;
            } else { // Not a valid character, we have fully parsed the number
                break;
            }
        }

        // Slice only what we need
        String valueStr = jsonString.substring(startIndex, stringIndex.get());
        
        Object value; // The number as an Integer or Double

        if(isDouble) 
            value = Double.parseDouble(valueStr);
        else 
            value = Integer.parseInt(valueStr);

        return value;
    }

    /**
     * Goes to the first non-whitespace character. This will leave the Counter ON the character
     * @param jsonString The JSon as a String
     * @param stringIndex How far we have parsed the JSon
     * @return The character we landed on
     */
    private static char goToFirstNonWhiteSpace(String jsonString, Counter stringIndex) {
        char currentChar = jsonString.charAt(stringIndex.get()); // Keep track of what character we are on
        
        // Increment as long as the current character is a white-space character
        while (currentChar == ' ' || currentChar == '\r' || currentChar == '\n' || currentChar == '\t') {
            stringIndex.step();
            currentChar = jsonString.charAt(stringIndex.get());
        }

        return currentChar;
    }

    /**
     * Reads a String. This method will leave the Counter AFTER the closing String quotation
     * @param jsonString The JSon as a String
     * @param stringIndex How far we have parsed the string
     * @return The String
     */
    private static String readString(String jsonString, Counter stringIndex) {
        char quotationChar = readChar(jsonString, stringIndex); // Opening quotation
        int startIndex = stringIndex.get(); // Used for string slicing
        char currentChar = readChar(jsonString, stringIndex); // Keeps track of what our current character is

        boolean isEscaped = false;

        while (currentChar != quotationChar || isEscaped) { // Until we have reached the closing quotation
            isEscaped = isEscaped ? false : currentChar == '\\';
            currentChar = readChar(jsonString, stringIndex);
        }

        // Return the slice we have read
        return jsonString.substring(startIndex, stringIndex.get() - 1);
    }

    /**
     * Reads a single character from the String. This method will leave the Counter AFTER the character that was just read
     * @param jsonString The JSon as a String
     * @param stringIndex How far we have parsed the String. This will be the index that is read
     * @return The character that was just read
     */
    private static char readChar(String jsonString, Counter stringIndex) {
        char v = jsonString.charAt(stringIndex.get());
        stringIndex.step();

        return v;
    }
}
