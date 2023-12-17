package ru.itmentor.spring.boot_security.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.Person;
import ru.itmentor.spring.boot_security.demo.util.PersonErrorResponce;
import ru.itmentor.spring.boot_security.demo.util.PersonNotCreatedException;
import ru.itmentor.spring.boot_security.demo.util.PersonValidator;
import ru.itmentor.spring.boot_security.demo.services.PersonService;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final PersonValidator personValidator;
    private final PersonService personService;
    @Autowired
    public AdminController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/adminpanel")
    public List<Person> getAll(){
        return personService.getAll();
    }

    @PostMapping("/reg")
    public ResponseEntity<HttpStatus> performAddition(@RequestBody @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error:errors) {
                errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw  new PersonNotCreatedException(errorMsg.toString());
        }
        personService.addNewPerson(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> updateUser(@PathVariable int id, @RequestBody Person person) {
        Person updatedUser = personService.update(person, id);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
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