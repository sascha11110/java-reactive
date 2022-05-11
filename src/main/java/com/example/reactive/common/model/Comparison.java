package com.example.reactive.common.model;

import java.time.LocalDateTime;

public record Comparison(
    Long id,
    LocalDateTime created,
    Long ssoUserId,
    String vehicleType
) {
    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private LocalDateTime created;
        private Long ssoUserId;
        private String vehicleType;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder created(final LocalDateTime created) {
            this.created = created;
            return this;
        }

        public Builder ssoUserId(final Long ssoUserId) {
            this.ssoUserId = ssoUserId;
            return this;
        }

        public Builder vehicleType(final String vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }

        public Comparison build() {
            return new Comparison(
                id,
                created,
                ssoUserId,
                vehicleType
            );
        }
    }
}
