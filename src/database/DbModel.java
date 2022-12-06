package database;


public interface DbModel {

    /**
     * Validate if the record can be inserted
     * @return true can be inserted, false otherwise
     */
    boolean isValid();

    /**
     * Save a new record into database
     * @return id of newly inserted record
     */
    int save();

    /**
     * Delete a record from database
     */
    void delete();

    /**
     * Update the record into database
     * @return true if updated, false otherwise
     */
    int update();
}
