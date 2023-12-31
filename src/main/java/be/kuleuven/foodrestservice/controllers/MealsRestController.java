package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @Operation(summary = "Get a meal by its id", description = "Get a meal by id description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the meal",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Meal.class))}),
            @ApiResponse(responseCode = "404", description = "Meal not found", content = @Content)})
    @GetMapping("/rest/meals/{id}")
    ResponseEntity<?> getMealById(
            @Parameter(description = "Id of the meal", schema = @Schema(format = "uuid", type = "string"))
            @PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));
        EntityModel<Meal> mealEntityModel = mealToEntityModel(id, meal);
        return ResponseEntity.ok(mealEntityModel);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    @DeleteMapping("/rest/meals/{id}")
    ResponseEntity<?> deleteMealById(
            @Parameter(description = "Id of the meal", schema = @Schema(format = "uuid", type = "string"))
            @PathVariable String id) {
        String code = mealsRepository.deleteMeal(id);
        return ResponseEntity.ok(code);
    }  
      
    @GetMapping("/rest/meals/largest")
    EntityModel<Meal> getLargestMeal(){
        Meal largest = mealsRepository.getLargestMeal();
        return mealToEntityModel(largest.getId(), largest);
    }

    @GetMapping("/rest/meals/cheapest")
    EntityModel<Meal> getCheapestMeal(){
        Meal cheapest = mealsRepository.getCheapestMeal();
        return mealToEntityModel(cheapest.getId(), cheapest);
    }

    @PostMapping("/rest/meals")
    ResponseEntity<Meal> addMeal(@RequestBody Meal m){
        if (m.getId() != null){
            mealsRepository.addMeal(m);
            return ResponseEntity.created(URI.create("/rest/meals/"+m.getId())).build();
        }else {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/rest/meals/{id}")
    ResponseEntity<Meal> updateMeal(@RequestBody Meal m,   @PathVariable String id){
        Meal current = mealsRepository.findMeal(id).orElse(null);
        if (current != null && Objects.equals(id, m.getId())){
            mealsRepository.deleteMeal(m.getId());
            mealsRepository.addMeal(m);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("All Meals"));
    }
}
