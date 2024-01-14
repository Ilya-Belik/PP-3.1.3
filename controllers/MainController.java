package ru.itmentor.spring.boot_security.demo.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.Person;
import ru.itmentor.spring.boot_security.demo.services.PersonService;
import ru.itmentor.spring.boot_security.demo.util.PersonErrorResponce;
import ru.itmentor.spring.boot_security.demo.util.PersonNotFoundException;
import java.util.List;
@RestController
@RequestMapping("/user")
public class MainController {

    private final PersonService personService;

    public MainController(PersonService personService) {
        this.personService = personService;
    }
    @GetMapping("")
    public List<Person> sayHello(){
        return personService.getAll();
    }
    @GetMapping("/{id}")
    public Person showById(@PathVariable("id") int id){
        return personService.findById(id);
    }
    @ExceptionHandler
    private ResponseEntity<PersonErrorResponce> handleException(PersonNotFoundException e){
        PersonErrorResponce response = new PersonErrorResponce(
                "Нет пользователя с таким ID",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
