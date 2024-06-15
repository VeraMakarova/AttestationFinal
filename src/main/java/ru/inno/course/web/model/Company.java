package ru.inno.course.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Company(int id, boolean isActive, String name, String description) {
}
