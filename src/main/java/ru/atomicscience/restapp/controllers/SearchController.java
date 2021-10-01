package ru.atomicscience.restapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.atomicscience.restapp.dao.UsersRepositorySearcher;
import ru.atomicscience.restapp.models.User;

@CrossOrigin(origins = "https://app.swaggerhub.com")
@RestController
@RequestMapping("/users/search")
public class SearchController {
    private final UsersRepositorySearcher searcher;

    public SearchController(UsersRepositorySearcher searcher) {
        this.searcher = searcher;
    }

    @GetMapping
    public ResponseEntity<Object> search(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        Iterable<User> searchResult;
        if(firstName != null)
            if(lastName != null)
                searchResult = searcher.searchUsersByFirstAndLastName(firstName, lastName);
            else
                searchResult = searcher.searchUsersByFirstName(firstName);
        else
            if(lastName != null)
                searchResult = searcher.searchUsersByLastName(lastName);
            else
                return ResponseEntity
                        .badRequest()
                        .body("To conduct a search, specify the firstName or/and lastName parameters");

        if(!searchResult.iterator().hasNext())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(searchResult);
    }
}
