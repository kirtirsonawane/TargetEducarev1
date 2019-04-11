package com.targeteducare.Classes;

public class UserProfModel {
    String usersettings;
    int usersettingsimage;

    public String getUsersettings() {
        return usersettings;
    }

    public void setUsersettings(String usersettings) {
        this.usersettings = usersettings;
    }

    public int getUsersettingsimage() {
        return usersettingsimage;
    }

    public void setUsersettingsimage(int usersettingsimage) {
        this.usersettingsimage = usersettingsimage;
    }

    public UserProfModel(String usersettings, int usersettingsimage){
        this.usersettings = usersettings;
        this.usersettingsimage = usersettingsimage;
    }
}
