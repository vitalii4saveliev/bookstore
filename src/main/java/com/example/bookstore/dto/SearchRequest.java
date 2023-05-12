package com.example.bookstore.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String field;
    private String operator;
    private String value;
    private String sortField;
    private String sortDirection;
    private int page;
    private int pageSize;
}
