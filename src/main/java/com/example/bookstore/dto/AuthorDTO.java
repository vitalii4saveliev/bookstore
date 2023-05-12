package com.example.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO implements Serializable {

    private static final long serialVersionUID = 3787211051915693854L;

    private Long id;

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2)
    private String lastName;

    @NotNull(message = "Birthday cannot be null")
    private Date birthday;

}
