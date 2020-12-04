package dk.clbo.controller;

//import com.sun.tools.javap.TypeAnnotationWriter;
import dk.clbo.model.Category;
import dk.clbo.model.Ingredient;
import dk.clbo.model.Notes;
import dk.clbo.model.Recipe;
import dk.clbo.repository.CategoryRepository;
import dk.clbo.repository.IngredientRepository;
import dk.clbo.repository.NotesRepository;
import dk.clbo.repository.RecipeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
public class RestRecipeController {

    RecipeRepository recipeRepository;
    NotesRepository notesRepository;
    IngredientRepository ingredientRepository;
    CategoryRepository categoryRepository;

    // constructor injection
    public RestRecipeController(RecipeRepository recipeRepository,
                                NotesRepository notesRepository,
                                IngredientRepository ingredientRepository,
                                CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.notesRepository = notesRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    // HTTP Get List
    @GetMapping("/recipe")
    public Iterable<Recipe> findAll(){

        // find all recipes
        return recipeRepository.findAll();
    }

    // HTTP Get by ID
    @GetMapping("/recipe/{id}")
    public ResponseEntity<Optional<Recipe>> findById(@PathVariable Long id){
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if(recipe.isPresent()){
            return ResponseEntity.status(200).body(recipe); // OK
        } else {
            return ResponseEntity.status(404).body(recipe); // Not found
        }
    }

    // HTTP Post, ie. create
    @CrossOrigin(origins = "*", exposedHeaders = "Location")
    @PostMapping(value="/recipe", consumes={"application/json"})
    public ResponseEntity<String> create(@RequestBody Recipe r){
        Recipe _recipe = new Recipe(r.getDescription(), r.getPrepTime(), r.getCookTime(), r.getServings(), r.getSource(), r.getUrl(), r.getDirections(), r.getXxx());
        //gem recipe, så der er et id tilknyttet til den nye opskrift til mapning i modsat regning
        recipeRepository.save(_recipe);

        //new notes objekt
//        Notes _notes = new Notes(r.getNotes().getDescription(),_recipe);
//        _recipe.setNotes(_notes);
//        notesRepository.save(_notes);

        //brug af notes-objekt i r
        Notes _notes=r.getNotes();
        _notes.setRecipe(_recipe);
        notesRepository.save(_notes);

        Set<Ingredient> _ingredients = r.getIngredients();
        for (Ingredient ingredient : _ingredients){
            ingredient.setRecipe(_recipe);
            ingredientRepository.save(ingredient);
        }
        _recipe.setIngredients(_ingredients);

        //category - kør igennem categories på ny recipe
        //  find tilsvarende category i repository
        //  opdater category med opskrift
        Set<Category> _categories = r.getCategories();
        for (Category category : _categories){
            Optional<Category> optCategory = categoryRepository.findById(category.getId());
            if (optCategory.isPresent()) {
                Category cat = optCategory.get();
                cat.getRecipes().add(_recipe);
                categoryRepository.save(cat);
            }
            else
            {
                System.out.println("unknown category id");
            }
        }
        _recipe.setCategories(_categories);
        //skal den gemmes igen?
        recipeRepository.save(_recipe);

        return ResponseEntity.status(201).header("Location", "/recipe/" + r.getId()).body("{'Msg': 'post created'}");
    }

    // HTTP PUT, ie. update
    @PutMapping("/recipe/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody Recipe r){
        //get recipeById
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (!optionalRecipe.isPresent()){
            //Recipe id findes ikke
            return ResponseEntity.status(404).body("{'msg':'Not found'");
        }

        //opdater category, ingredient og notes sker automatisk - nu er relationen oprettet
        //save recipe
        recipeRepository.save(r);
        return ResponseEntity.status(204).body("{'msg':'Updated'}");
    }

    // HTTPDelete
    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        Optional<Recipe> recipe = recipeRepository.findById(id);
        //check at opskriften findes
        if(!recipe.isPresent()){
            return ResponseEntity.status(404).body("{'msg':'Not found'"); // Not found
        }

        Recipe r = recipe.get();
        //slet først referencerne til recipe i categories
        for (Category c: r.getCategories()){
            c.getRecipes().remove(r);
        }

        //derefter kan categories slettes fra recipe
        r.setCategories(null);

        //og opdateres (nu uden category mappings)
        recipeRepository.save(r);

        //til sidst kan recipe slettes uden at bryde referentiel integritet
        recipeRepository.deleteById(id);

        return ResponseEntity.status(200).body("{'msg':'Deleted'}");
    }
}
