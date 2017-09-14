package com.fintech.adl.dto;


/**
 * Created by tungn on 9/7/2017.
 */
public class Request {
    private Placement placement;
    private String userId;

    public Request() {
    }

    public static class Builder {
        private Placement placement;

        public Builder() {
        }

        public Builder setPlacement(Placement placement) {
            this.placement= placement;
            return this;
        }

        public Request build() {
            if (placement == null) {
                throw new IllegalStateException("Requests must have at least one placement");
            }
            return new Request(this);
        }
    }

    private Request(Builder builder) {
        setPlacement(builder.placement);
    }

    private void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public Placement getPlacement() {
        return placement;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
