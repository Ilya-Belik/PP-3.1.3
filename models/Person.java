package ru.itmentor.spring.boot_security.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Псевдоним не должен быть короче 2 или длиннее 50 символов")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя не должно быть короче 2 или длиннее 50 символов")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Поле пароль обязательно к заполнению")
    @Size(min = 5, max = 200, message = "Пароль должен быть не короче 5 или длиннее 50 символов, состоять должен из латинских символов разного регистра")
    @Column(name = "password")
    private String password;

    @ManyToMany // Я честно сдаюсь, сколько бы не бился с аннотацией @OneToMany не смог, звал на помощь студентов, тоже не поняли, так как большинство использовали @ManyToMany.
    // Именно с @ManyToMany пользователь может иметь несколько ролей, а с @OneToMany в таблице всегда выдает ошибку будто я создаю новую роль или что
    @JoinTable(name = "person_roles",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    @JsonIgnore
    private Set<Role> roles;

    public Person() {
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Person(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return String.format("Person{%s\n%s\n%s\n%s}\n", this.id, this.username, this.name, this.password);
    }
}
