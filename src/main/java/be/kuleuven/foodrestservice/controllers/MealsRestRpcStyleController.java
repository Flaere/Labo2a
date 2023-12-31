package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/meals/largest")
    Meal getLargestMeal(){ return mealsRepository.getLargestMeal();}

    @GetMapping("/restrpc/meals/cheapest")
    Meal getCheapestMeal(){ return mealsRepository.getCheapestMeal();}

    @DeleteMapping("/restrpc/meals/delete/{id}")
    String deleteMeal(@PathVariable String id) {
        return mealsRepository.deleteMeal(id);
    }

    @PostMapping(value = "/restrpc/meals", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createMeal(@RequestBody Meal m){
        mealsRepository.addMeal(m);
    }


}