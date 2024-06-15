package ru.inno.course.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Employee(int id, boolean isActive, String firstName, String lastName, String email) {
}
