package util;

import java.util.List;
import java.util.Map;

public class PrettyPrint {
    @SuppressWarnings("unchecked")
    private static void pprint(Object json, String tabStr, int nestingLevel) {
        if(json instanceof Map) { // Map
            Map<String, Object> jsonMap = (Map<String, Object>) json;
            System.out.println("{");

            int i = 0;
            int mapSize = jsonMap.size();

            for (var entry : jsonMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                System.out.printf("%s%s: ", tabStr.repeat(nestingLevel), key);

                if(value instanceof Map || value instanceof List)
                    pprint(value, tabStr, nestingLevel + 1);
                else if(value instanceof String)
                    System.out.printf("\"%s\"", value);
                else
                    System.out.print(value);

                if(i < mapSize - 1)
                    System.out.print(",");

                System.out.println();
                i++;
            }

            System.out.printf("%s}", tabStr.repeat(nestingLevel - 1));
        } else if(json instanceof List) { // List
            List<Object> jsonList = (List<Object>) json;
            System.out.println("[");

            int i = 0;
            int listSize = jsonList.size();

            for (Object value : jsonList) {
                System.out.print(tabStr.repeat(nestingLevel));

                if(value instanceof Map || value instanceof List)
                    pprint(value, tabStr, nestingLevel + 1);
                else if(value instanceof String)
                    System.out.printf("\"%s\"", value);
                else
                    System.out.print(value);

                if(i < listSize - 1)
                    System.out.print(",");

                System.out.println();
                i++;
            }

            System.out.printf("%s]", tabStr.repeat(nestingLevel - 1));
        } else {
            throw new RuntimeException("Pretty print only works for maps and lists; pprint received: " + json.toString());
        }
    }

    public static void pprint(Object json, int indent) {
        pprint(json, " ".repeat(indent), 1);
    }

    public static void pprint(Object json) {
        pprint(json, 2);
    }
}
