package util;

import util.TerminalTextFormatter.ANSIFlag;

public final class OutputFormatter {
    private OutputFormatter() {} // Prevent this class from being instantiated

    /**
     * Generates a table as a string from the provided provided 2d array. All rows must be of the same length.
     * @param table a list of table rows. Each row is an array of Strings
     * @return the table as a string
     */
    public static String generateTable(String[][] table) {
        if(table.length < 1) return "";

        int[] columnWidths;

        // Calculate how wide each column has to be
        {
            columnWidths = new int[table[0].length];

            for(String[] row: table) {
                for(int cellIndex = 0; cellIndex < row.length; cellIndex++) {
                    columnWidths[cellIndex] = Math.max(columnWidths[cellIndex], row[cellIndex].length());
                }
            }
        }

        // The string representation of the table
        StringBuilder tableBuilder = new StringBuilder();

        boolean isFirstRow = true;

        for(String[] row : table) {
            if(isFirstRow) tableBuilder.append(ANSIFlag.UNDERLINE.ANSI_CODE);

            for(int cellIndex = 0; cellIndex < row.length; cellIndex++) { // Create a new cell
                tableBuilder.append("|" + asPaddedString(row[cellIndex], columnWidths[cellIndex] + 2));
            }

            tableBuilder.append("|\n"); // End off the row
            
            if(isFirstRow) tableBuilder.append(ANSIFlag.RESET.ANSI_CODE);

            isFirstRow = false;
        }

        return tableBuilder.toString(); // Return the created table
    }

    /**
     * Pads a string to a certain length with whitespace evenly on both the left and right side.<br>
     * For example, calling asPaddedString("Test", 6) will return " Test "
     * @param string The original string
     * @param targetLength how long the padded string should be
     * @return The padded string
     */
    private static String asPaddedString(String string, int targetLength) {
        int originalLength = string.length();

        // Don't pad at all if the original string is too long
        if(targetLength <= originalLength) return string;

        int paddingLeft = (targetLength - originalLength) / 2;
        int paddingRight = targetLength - paddingLeft - originalLength;

        // Return padded string
        return " ".repeat(paddingLeft) + string + " ".repeat(paddingRight);
    }
}
