package ru.m4oma.repositories.specifications;

import ru.m4oma.models.Mistress;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MistressSpecifications {
    public static Specification<Mistress> getSpecification(String name, LocalDate from, LocalDate to) {
        Specification<Mistress> spec = Specification.where(null);

        if (name != null && !name.isBlank()) {
            spec = spec.and(nameContains(name));
        }
        if (from != null) {
            spec = spec.and(birthDateAfter(from));
        }
        if (to != null) {
            spec = spec.and(birthDateBefore(to));
        }

        return spec;
    }

    public static Specification<Mistress> nameContains(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isBlank()) return null;
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Mistress> birthDateAfter(LocalDate date) {
        return (root, query, builder) -> {
            if (date == null) return null;
            return builder.greaterThanOrEqualTo(root.get("birthDate"), date);
        };
    }

    public static Specification<Mistress> birthDateBefore(LocalDate date) {
        return (root, query, builder) -> {
            if (date == null) return null;
            return builder.lessThanOrEqualTo(root.get("birthDate"), date);
        };
    }
}
