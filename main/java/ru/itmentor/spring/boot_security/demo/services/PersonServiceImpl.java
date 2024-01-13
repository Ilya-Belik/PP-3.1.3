package ru.itmentor.spring.boot_security.demo.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.models.Person;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.repositories.PersonRepository;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.util.PersonNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonServiceImpl implements PersonService {

    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(RoleRepository roleRepository, PersonRepository personRepository) {
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
    }
    @Override
    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> personOptional = personRepository.findByUsername(username);
        if (!personOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        Person person = personOptional.get();
        Hibernate.initialize(person.getRole()); // Инициализируем коллекцию ролей
        return new User(person.getUsername(), person.getPassword(), person.getRole());
    }

    @Transactional
    @Override
    public void addNewPerson(Person person) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        person.setRole(roles);
        personRepository.save(person);
    }

    @Transactional
    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Transactional
    @Override
    public Person findById(int id) {
        return personRepository.findById(id)
                .orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    @Override
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Person update(Person updatedPerson, int id) {
        Optional<Person> personOptional = personRepository.findById(id);
        Person personForUpdating = personOptional.get();
        personForUpdating.setName(updatedPerson.getName());
        personForUpdating.setUsername(updatedPerson.getUsername());
        personForUpdating.setPassword(updatedPerson.getPassword());
        personRepository.save(personForUpdating);

        return personForUpdating;
    }
}