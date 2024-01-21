package ru.itmentor.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmentor.spring.boot_security.demo.models.Person;

import java.util.Optional;
@Repository
public interface PersonRepository extends JpaRepository<Person,Integer>{

    Optional<Person> findByUsername(String username);
}
