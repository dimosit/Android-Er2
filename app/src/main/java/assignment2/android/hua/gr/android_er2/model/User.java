package assignment2.android.hua.gr.android_er2.model;

public class User {
    /**
     * The user's id
     */
    private int useid;
    /**
     * The user's name
     */
    private String username;
    /**
     * The user's current location
     */
    private String current_location;

    /**
     * Getter for useid
     * @return the user's id
     */
    public int getUseid() {
        return useid;
    }

    /**
     * Setter for useid
     * @param useid the user's id
     */
    public void setUseid(int useid) {
        this.useid = useid;
    }

    /**
     * Getter for username
     * @return the user's name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     * @param username the user's name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for user's current location
     * @return the user's current location
     */
    public String getCurrent_location() {
        return current_location;
    }

    /**
     * Setter for user's current location
     * @param current_location the user's current location
     */
    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

}
