
package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_users_email",
            columnNames = "email"
        )
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 254, message = "Email must not exceed 254 characters")
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^\\+?[1-9]\\d{7,14}$",
        message = "Enter a valid phone number with 8–15 digits"
    )
    @Column(nullable = false, length = 16)
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72,
          message = "Password must contain 8–72 characters")
    @Column(nullable = false)
    private String password;
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; } 
    public Long getId() { return id; } 
    public void setId(Long id) { this.id = id; } 
    public String getName() { return name; } 
    public void setName(String name) { this.name = name; } 
    public String getEmail() { return email; } 
    public void setEmail(String email) { this.email = email; } 
    public String getPhone() { return phone; } 
    public void setPhone(String phone) { this.phone = phone; }
}