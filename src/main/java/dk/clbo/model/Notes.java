package dk.clbo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

// entitetsklasse i jpa - tabel kan specificeres
@Entity
@Table(name = "notes")
public class Notes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// kolonnenavn kan specificeres
    @Column( name="description" )
	private String description;

    //FetchType Lazy og Eager - hente ved tilgang til eller straks, når parent hentes
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")

	//for at undgå uendeligt loop, ignoreres parent property i childbojekt med JsonBackReference
	@JsonBackReference
	private Recipe recipe;

	public Notes() {
	}

	public Notes(String description, Recipe recipe) {
		this.description = description;
		this.recipe = recipe;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
}
