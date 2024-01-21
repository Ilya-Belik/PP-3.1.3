package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.itmentor.spring.boot_security.demo.models.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    void addNewPerson(Person person, List<String> roleNames);
    List<Person> getAll();
    Person findById(int id);
    void delete(int id);
    Person update(Person updatedPerson);
    Optional<Person> findByUsername(String username);
}
