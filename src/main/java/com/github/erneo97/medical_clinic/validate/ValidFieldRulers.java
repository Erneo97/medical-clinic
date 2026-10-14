package com.github.erneo97.medical_clinic.validate;

public class ValidFieldRulers {
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final String  PASSWORD_LENGTH_MESSAGE = "password must be at least 8 characters long";
    public static final String PASSWORD_REGEX =
            "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\d)(?=.*[^\\p{L}\\p{N}\\s])\\S+";
    public static final String PASSWORD_PATTERN_MESSAGE =
            "Password must contain a lowercase and uppercase letter, a digit and a special character, and no whitespace";

    public static final String NAME_REGEX = "\\p{L}+([ '-]\\p{L}+)*";
    public static final String FIRST_NAME_PATTERN_MESSAGE =
            "First name may contain only letters, spaces, dash and apostrophes";
    public static final String LAST_NAME_PATTERN_MESSAGE =
            "Last name may contain only letters, spaces, dash and apostrophes";

    public static final String ID_CARD_NO_REGEX = "[A-Z]{3}\\d{6}";
    public static final String ID_CARD_NO_PATTERN_MESSAGE= "card number must look like ABC123456";

    public static final String PHONE_NUMBER_REGEX = "\\d{9}";
    public static final String PHONE_NUMBER_PATTERN_MESSAGE = "Phone number must contain exactly 9 digits";

    public static final String AGE_PAST_MESSAGE = "birth date must be in the past";

    public static final String NOT_BLANK_MESSAGE = "must not be blank";

    public static final String FILED_IS_REQUIRED_MESSAGE = "field is required";

    public static final String FIELD_IS_NOT_EMAIL = "field does not look like an email address";
}
