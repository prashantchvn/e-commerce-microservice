package com.exampleAuth.AuthService.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    private String password;

    private String otp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role Role = com.exampleAuth.AuthService.Entity.Role.USER;

    @Column(name = "is_verified")
    private Boolean isVerified;

    private List<String> cartItems = new ArrayList<>();
    private List<String> orders = new ArrayList<>();
    private List<String> products = new ArrayList<>();

    private String buisnessAddress;
    private String businessId;

    @Column(name = "contact_number")
    private String contactNumber;

}

