package com.model;

import java.util.Objects;

public class CustomResponse {
    private int status;
    private String response;

    public CustomResponse(int status, String response) {
        this.status = status;
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public String getResponseMessage() {
        return response;
    }

    @Override
    public String toString() {
        return "CustomResponse{" +
                "status=" + status +
                ", response='" + response + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomResponse that = (CustomResponse) o;
        return status == that.status &&
                Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, response);
    }
}
