package ru.itmentor.spring.boot_security.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name="person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min=2, max=50, message = "Псевдоним не должен быть короче 2 или длиннее 50 символов")
    @Column(name="username")
    private String username;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min=2, max=50, message = "Имя не должно быть короче 2 или длиннее 50 символов")
    @Column(name="name")
    private String name;

    @NotEmpty(message = "Поле пароль обязательно к заполнению")
    @Size(min=5, max=100, message = "Пароль должен быть не короче 5 или длиннее 50 символов, состоять должен из латинских символов разного регистра")
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public Person() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Person(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("Person{%s\n%s\n%s\n%s\n", this.id, this.username, this.name, this.password);
    }
}
