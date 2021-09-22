package ru.atomicscience.restapp.security.jwt;

public enum JwtValidationResult {
    SUCCESS,
    EXPIRED, INVALID_TOKEN_FORMAT, INVALID_SIGNATURE, NO_TOKEN_FOUND,
    UNKNOWN_FAILURE;

    public String getMessage() {
        switch (this) {
            case SUCCESS:
                return "Token is valid";
            case EXPIRED:
                return "Token is expired";
            case INVALID_TOKEN_FORMAT:
                return "Token has an invalid format";
            case INVALID_SIGNATURE:
                return "Failed to decipher the token";
            case NO_TOKEN_FOUND:
                return "Token was not provided in the request";
            case UNKNOWN_FAILURE:
                return "Failed to process JWT token due to an unknown error";
            default:
                return "Unknown token error";
        }
    }
}
