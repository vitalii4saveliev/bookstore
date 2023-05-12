package com.example.bookstore.entity.specification;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BookSpecification {
    public Specification<Book> hasField(String field, String operator, String value) {
        return (root, query, builder) -> {
            Path<String> fieldPath = root.get(field);
            String lowerValue = value.toLowerCase();
            switch (operator) {
                case "equals":
                    return builder.equal(fieldPath, value);
                case "like":
                    return builder.like(builder.lower(fieldPath), "%" + lowerValue + "%");
                case "greaterThan":
                    return builder.greaterThan(fieldPath.as(Date.class), getDateValue(value));
                case "lessThan":
                    return builder.lessThan(fieldPath.as(Date.class), getDateValue(value));
                case "greaterThanOrEqual":
                    return builder.greaterThanOrEqualTo(fieldPath.as(Date.class), getDateValue(value));
                case "lessThanOrEqual":
                    return builder.lessThanOrEqualTo(fieldPath.as(Date.class), getDateValue(value));
                default:
                    return null;
            }
        };
    }

    private static Date getDateValue(String value) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

}
