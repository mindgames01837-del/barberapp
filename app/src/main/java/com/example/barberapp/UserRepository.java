package com.example.barberapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String PREFS_NAME = "rollncuts_prefs";
    private static final String KEY_USERS = "users";
    private static final String KEY_LOGGED_IN = "logged_in_email";

    public enum LoginResult {
        SUCCESS,
        WRONG_PASSWORD,
        USER_NOT_FOUND
    }

    private final SharedPreferences prefs;
    private final Gson gson;

    public UserRepository(Context context) {
        prefs = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );

        gson = new Gson();
    }

    // ============================================================
    // REGISTER USER
    // ============================================================

    public boolean registerUser(User newUser) {

        List<User> users = getAllUsers();

        // Check if email already exists
        for (User user : users) {

            if (user.getEmail().equalsIgnoreCase(newUser.getEmail())) {
                return false;
            }
        }

        // Add new user
        users.add(newUser);

        // Save updated user list
        saveAllUsers(users);

        return true;
    }

    // ============================================================
    // LOGIN USER
    // ============================================================

    public LoginResult login(String email, String password) {

        List<User> users = getAllUsers();

        for (User user : users) {

            if (user.getEmail().equalsIgnoreCase(email)) {

                // Email found, check password
                if (user.getPassword().equals(password)) {

                    // Save logged-in user
                    prefs.edit()
                            .putString(KEY_LOGGED_IN, user.getEmail())
                            .apply();

                    return LoginResult.SUCCESS;
                }

                return LoginResult.WRONG_PASSWORD;
            }
        }

        return LoginResult.USER_NOT_FOUND;
    }

    // ============================================================
    // CHECK LOGIN SESSION
    // ============================================================

    public boolean isLoggedIn() {
        return prefs.contains(KEY_LOGGED_IN);
    }

    // ============================================================
    // GET LOGGED-IN USER
    // ============================================================

    public User getLoggedInUser() {

        String email = prefs.getString(
                KEY_LOGGED_IN,
                null
        );

        if (email == null) {
            return null;
        }

        for (User user : getAllUsers()) {

            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }

    // ============================================================
    // LOGOUT
    // ============================================================

    public void logout() {

        prefs.edit()
                .remove(KEY_LOGGED_IN)
                .apply();
    }

    // ============================================================
    // GET ALL USERS
    // ============================================================

    private List<User> getAllUsers() {

        String json = prefs.getString(
                KEY_USERS,
                null
        );

        if (json == null) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<User>>() {}.getType();

        List<User> users = gson.fromJson(
                json,
                type
        );

        return users != null
                ? users
                : new ArrayList<>();
    }

    // ============================================================
    // SAVE ALL USERS
    // ============================================================

    private void saveAllUsers(List<User> users) {

        prefs.edit()
                .putString(
                        KEY_USERS,
                        gson.toJson(users)
                )
                .apply();
    }
}