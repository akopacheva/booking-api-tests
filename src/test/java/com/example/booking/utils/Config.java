package com.example.booking.utils;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;
import net.thucydides.model.environment.SystemEnvironmentVariables;

/** Reads environment-specific values from serenity.conf (switch with -Denvironment=local). */
public final class Config {

    private static final EnvironmentVariables ENV = SystemEnvironmentVariables.createEnvironmentVariables();

    private Config() {}

    public static String baseUrl() {
        return get("restapi.baseurl");
    }

    public static String username() {
        return get("auth.username");
    }

    public static String password() {
        return get("auth.password");
    }

    private static String get(String key) {
        return EnvironmentSpecificConfiguration.from(ENV).getProperty(key);
    }
}
