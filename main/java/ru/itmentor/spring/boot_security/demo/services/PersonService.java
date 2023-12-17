package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.models.Person;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.repositories.PeopleRepository;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.util.PersonNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonService {

    private final RoleRepository roleRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonService(RoleRepository roleRepository, PeopleRepository peopleRepository) {
        this.roleRepository = roleRepository;
        this.peopleRepository = peopleRepository;
    }


    public boolean loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);
        return person.isPresent();
    }

    @Transactional
    public void addNewPerson(Person person) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        person.setRole(roles);
        peopleRepository.save(person);
    }

    @Transactional
    public List<Person> getAll() {
        return peopleRepository.findAll();
    }

    @Transactional
    public Person findById(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public Person update(Person updatedPerson, int id) {
        Optional<Person> personOptional = peopleRepository.findById(id);
        Person personForUpdating = personOptional.get();
            personForUpdating.setName(updatedPerson.getName());
            personForUpdating.setUsername(updatedPerson.getUsername());
            personForUpdating.setPassword(updatedPerson.getPassword());
            peopleRepository.save(personForUpdating);

        return personForUpdating;
    }


}
