package com.nt.models;

public class JwtResponse {

    private final String jwtToken;
    private final String username;

    // Private constructor to enforce the use of the Builder
    private JwtResponse(Builder builder) {
        this.jwtToken = builder.jwtToken;
        this.username = builder.username;
    }

    // Getters
    public String getJwtToken() {
        return jwtToken;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "jwtToken='" + jwtToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    // Static inner Builder class
    public static class Builder {
        private String jwtToken;
        private String username;

        // Setter methods for Builder
        public Builder setJwtToken(String jwtToken) {
            this.jwtToken = jwtToken;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        // Build method to create the final object
        public JwtResponse build() {
            return new JwtResponse(this);
        }
    }
}
