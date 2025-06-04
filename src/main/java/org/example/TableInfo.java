package org.example;

/**
 * Represents metadata for a database table.
 *
 * @param name    the name of the table
 * @param columns an array of column names in the table
 */
 public record TableInfo(String name, String[] columns) {
}
