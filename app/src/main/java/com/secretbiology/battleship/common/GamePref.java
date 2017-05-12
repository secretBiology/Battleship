package com.secretbiology.battleship.common;

import android.content.Context;

import com.secretbiology.helpers.general.Preferences;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class GamePref extends Preferences {

    private static String EMAIL = "email";
    private static String NAME = "name";
    private static String UID = "uid";
    private static String AUTHENTICATE = "authenticated";

    public GamePref(Context context) {
        super(context);
    }

    public String getEmail() {
        return getString(EMAIL);
    }

    public void setEmail(String email) {
        put(EMAIL, email);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME, name);
    }

    public String getUID() {
        return getString(UID);
    }

    public void setUID(String uid) {
        put(UID, uid);
    }

    public boolean isAuthenticated() {
        return get(AUTHENTICATE, false);
    }

    public void userLoggedIn() {
        put(AUTHENTICATE, true);
    }
}
