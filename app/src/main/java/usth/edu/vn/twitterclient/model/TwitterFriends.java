package usth.edu.vn.twitterclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TwitterFriends {
        @SerializedName("id")
        @Expose
        private long id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("profile_image_url_https")
        @Expose
        private String profilePictureUrl;

        @SerializedName("screen_name")
        @Expose
        private String screenName;

        public long getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePictureUrl() {
            return profilePictureUrl;
        }

        public void setProfilePictureUrl(String profilePictureUrl) {
            this.profilePictureUrl = profilePictureUrl;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }
}
