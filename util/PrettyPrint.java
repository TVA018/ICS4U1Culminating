package util;

import java.util.List;
import java.util.Map;

/** A helper class to print a JSon Map/List with proper indenting and new-lines */
public class PrettyPrint {
    /**
     * Pretty-prints a JSon Map/List with the specified indent and nesting level
     * @param json The JSon as a Map or List
     * @param indentStr The indent as a String
     * @param nestingLevel How nested this object is. The outermost Map/List starts at 1
     */
    @SuppressWarnings("unchecked")
    private static void pprint(Object json, String indentStr, int nestingLevel) {
        if(json instanceof Map) { // Map
            Map<String, Object> jsonMap = (Map<String, Object>) json;
            System.out.println("{"); // Opening brace

            int entryIndex = 0; // The current entry index
            int mapSize = jsonMap.size();

            for (var entry : jsonMap.entrySet()) { // Loop through the entries in the Map
                String key = entry.getKey();
                Object value = entry.getValue();

                // Print the key indented
                System.out.printf("%s%s: ", indentStr.repeat(nestingLevel), key);

                if(value instanceof Map || value instanceof List)
                    // Recursively call this function to pretty print the nested Map/List. Increments the indent level by 1 for this entry
                    pprint(value, indentStr, nestingLevel + 1); 
                else if(value instanceof String)
                    System.out.printf("\"%s\"", value); // Wrap the String in quotation marks
                else
                    System.out.print(value); // Print like normal

                // Add a comma if this isn't the last entry
                if(entryIndex < mapSize - 1)
                    System.out.print(",");

                System.out.println(); // Add a newline to cap off the line
                entryIndex++;
            }

            // Closing brace with the proper indent
            System.out.printf("%s}", indentStr.repeat(nestingLevel - 1));
        } else if(json instanceof List) { // List
            List<Object> jsonList = (List<Object>) json;
            System.out.println("["); // Opening bracket

            int elementIndex = 0; // The current element index
            int listSize = jsonList.size();

            for (Object value : jsonList) {
                System.out.print(indentStr.repeat(nestingLevel)); // Indent

                if(value instanceof Map || value instanceof List)
                    // Recursively pretty print the Map/List indented further in
                    pprint(value, indentStr, nestingLevel + 1);
                else if(value instanceof String)
                    System.out.printf("\"%s\"", value); // Print Strings with quotation marks
                else
                    System.out.print(value); // Print it like normal

                // Add a comma if this isn't the last element
                if(elementIndex < listSize - 1)
                    System.out.print(",");

                System.out.println(); // Cap off the current line
                elementIndex++;
            }

            // Closing bracket with proper indenting
            System.out.printf("%s]", indentStr.repeat(nestingLevel - 1));
        } else {
            // Handle non-Maps and non-Lists because this object accepts any Object
            throw new RuntimeException("Pretty print only works for maps and lists; pprint received: " + json.toString());
        }
    }

    /**
     * Pretty-prints a JSon Map/List with the specified indent
     * @param json The JSon as a Map/List
     * @param indent How many whitespaces to indent with per level
     */
    public static void pprint(Object json, int indent) {
        pprint(json, " ".repeat(indent), 1);
    }

    /**
     * Pretty-prints a JSon Map/List with an indent of 2 white spaces
     * @param json The JSon as a Map/List
     */
    public static void pprint(Object json) {
        pprint(json, 2);
    }
}
