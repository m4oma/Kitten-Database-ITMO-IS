package ru.m4oma.repositories.specifications;

import ru.m4oma.models.Breed;
import ru.m4oma.models.Colour;
import ru.m4oma.models.Kitten;

import org.springframework.data.jpa.domain.Specification;



public class KittenSpecifications {
    public static Specification<Kitten> getSpecification(String name, String breed, String colour, double tailLengthMin, double tailLengthMax, Integer mistressId) {
        Specification<Kitten> spec = Specification.where(null);

        if (name != null && !name.isBlank()) {
            spec = spec.and(hasName(name));
        }
        if (breed != null && !breed.isBlank()) {
            spec = spec.and(hasBreed(breed));
        }
        if (colour != null && !colour.isBlank()) {
            spec = spec.and(hasColour(colour));
        }
        if (tailLengthMin >= 0) {
            spec = spec.and(tailLengthMin(tailLengthMin));
        }
        if (tailLengthMax != 0) {
            spec = spec.and(tailLengthMax(tailLengthMax));
        }
        if (mistressId != null) {
            spec = spec.and(hasMistress(mistressId));
        }
        return spec;
    }
    public static Specification<Kitten> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Kitten> hasBreed(String breed) {
        return (root, query, cb) ->
                breed == null ? null : cb.equal(root.get("breed"), Breed.valueOf(breed.toUpperCase()));
    }

    public static Specification<Kitten> hasColour(String colour) {
        return (root, query, cb) ->
                colour == null ? null : cb.equal(root.get("colour"), Colour.valueOf(colour.toUpperCase()));
    }

    public static Specification<Kitten> tailLengthMin(Double value) {
        return (root, query, cb) ->
                value == null ? null : cb.greaterThanOrEqualTo(root.get("tailLength"), value);
    }

    public static Specification<Kitten> tailLengthMax(Double value) {
        return (root, query, cb) ->
                value == null ? null : cb.lessThanOrEqualTo(root.get("tailLength"), value);
    }

    public static Specification<Kitten> hasMistress(Integer mistressId) {
        return (root, query, cb) ->
                mistressId == null ? null : cb.equal(root.get("mistress").get("id"), mistressId);
    }
}
