package usth.edu.vn.twitterclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FriendsResponseModel {
    @SerializedName("previous_cursor")
    @Expose
    private Integer previousCursor;

    @SerializedName("previous_cursor_str")
    @Expose
    private String previousCursorStr;

    @SerializedName("next_cursor_str")
    @Expose
    private String nextCursorStr;

    @SerializedName("users")
    @Expose
    private List<TwitterFriends> results = new ArrayList<TwitterFriends>();

    public Integer getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(Integer previousCursor) {
        this.previousCursor = previousCursor;
    }

    public String getPreviousCursorStr() {
        return previousCursorStr;
    }

    public void setPreviousCursorStr(String previousCursorStr) {
        this.previousCursorStr = previousCursorStr;
    }

    public String getNextCursorStr() {
        return nextCursorStr;
    }

    public void setNextCursorStr(String nextCursorStr) {
        this.nextCursorStr = nextCursorStr;
    }

    public List<TwitterFriends> getResults() {
        return results;
    }

    public void setResults(List<TwitterFriends> results) {
        this.results = results;
    }
}
