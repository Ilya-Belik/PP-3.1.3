package ru.itmentor.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_role")
    private String name;

//    @ManyToMany(mappedBy = "role")
//    private Set<Person> persons;

    public Role() {
    }

    public Role(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Set<Person> getPersons() {
//        return persons;
//    }

//    public void setPersons(Set<Person> persons) {
//        this.persons = persons;
//    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString();

    }
}
