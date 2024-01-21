package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.models.Person;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.repositories.PersonRepository;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.util.PersonNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

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
        Set<Role> roles = person.getRoles();
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new User(person.getUsername(), person.getPassword(), authorities);
    }

    @Transactional
    @Override
    public void addNewPerson(Person person, List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                throw new IllegalArgumentException("Роль не найдена: " + roleName);
            }
            roles.add(role);
        }
        person.setRoles(roles);
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
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException());
        personRepository.delete(person);
    }

    @Transactional
    @Override
    public Person update(Person updatedPerson) {
        Person personForUpdating = personRepository.findById(updatedPerson.getId())
                .orElseThrow(() -> new PersonNotFoundException());
        personForUpdating.setName(updatedPerson.getName());
        personForUpdating.setUsername(updatedPerson.getUsername());

        if (updatedPerson.getPassword() != null) {
            PasswordEncoder passwordEncoder = getPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(updatedPerson.getPassword());
            personForUpdating.setPassword(encodedPassword);
        }

        if (updatedPerson.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            for (Role role : updatedPerson.getRoles()) {
                Role existingRole = roleRepository.findById(role.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Роль не найдена: " + role.getId()));
                roles.add(existingRole);
            }
            personForUpdating.getRoles().clear();
            personForUpdating.getRoles().addAll(roles);
        }

        personRepository.save(personForUpdating);
// изменения в контроллере
        return personForUpdating;
    }

    private PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
