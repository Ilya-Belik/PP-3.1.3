package ru.itmentor.spring.boot_security.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.Person;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.repositories.PersonRepository;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.util.PersonErrorResponce;
import ru.itmentor.spring.boot_security.demo.util.PersonNotCreatedException;
import ru.itmentor.spring.boot_security.demo.util.PersonValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmentor.spring.boot_security.demo.services.PersonService;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final PersonValidator personValidator;
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;

    @Autowired
    public AdminController(PersonValidator personValidator, PersonService personService, PasswordEncoder passwordEncoder, RoleRepository roleRepository, PersonRepository personRepository) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
    }


    @GetMapping("/adminpanel")
    public ResponseEntity<List<Person>> getAll(){
        List<Person> people = personService.getAll();
        return ResponseEntity.ok(people);
    }

    @PostMapping("/reg")
    public ResponseEntity<HttpStatus> performAddition(@RequestBody @Valid Person person, BindingResult bindingResult, @RequestParam("roleNames") List<String> roleNames) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        personService.addNewPerson(person, roleNames);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> updateUser(@PathVariable int id, @RequestBody Person person) {
        Person existingPerson = personService.findById(id);
        if (existingPerson == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingPerson.setName(person.getName());
        existingPerson.setUsername(person.getUsername());
        existingPerson.setPassword(person.getPassword());
        existingPerson.setRoles(person.getRoles());

        Person updatedPerson = personService.update(existingPerson);

        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        Person person = personService.findById(id);
        personService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponce> handleException(PersonNotCreatedException e){
        PersonErrorResponce response = new PersonErrorResponce(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
