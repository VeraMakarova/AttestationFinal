package ru.inno.course.db.model;

import java.sql.Timestamp;

public record CompanyDB(int id, boolean isActive, Timestamp createdAt, String name, String description, Timestamp deletedAt) {
}
