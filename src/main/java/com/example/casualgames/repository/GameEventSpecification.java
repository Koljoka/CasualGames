package com.example.casualgames.repository;

import com.example.casualgames.entity.GameEvent;
import com.example.casualgames.entity.Sport;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//Criteria API haku GameEventille
public class GameEventSpecification {

    //Rakennetaan dynaaminen haku annettujen ehtojen perusteella
    public static Specification<GameEvent> filterEvents(
            String keyword,
            String city,
            String sportName,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return (root, query, criteriaBuilder) -> {

            //Kerätään kaikki AND-ehdot tänne
            List<Predicate> predicates = new ArrayList<>();

            //JOIN Sport entiteettiin
            Join<GameEvent, Sport> sportJoin = root.join("sport");

            //Monimutkainen OR-ehto:
            //(title LIKE ? OR city LIKE ? OR location LIKE ?)
            if (keyword != null && !keyword.isBlank()) {
                String likeValue = "%" + keyword.toLowerCase() + "%";

                Predicate titlePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        likeValue
                );

                Predicate cityPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city")),
                        likeValue
                );

                Predicate locationPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        likeValue
                );

                Predicate keywordOrPredicate = criteriaBuilder.or(
                        titlePredicate,
                        cityPredicate,
                        locationPredicate
                );

                predicates.add(keywordOrPredicate);
            }

            //Suodatus city kentän perusteella
            if (city != null && !city.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("city")),
                                "%" + city.toLowerCase() + "%"
                        )
                );
            }

            //Suodatus sportin nimen perusteella JOIN:lla
            if (sportName != null && !sportName.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(sportJoin.get("name")),
                                "%" + sportName.toLowerCase() + "%"
                        )
                );
            }

            //Suodatus aloituspäivästä eteenpäin
            if (startDate != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDate)
                );
            }

            //Suodatus loppupäivään asti
            if (endDate != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endDate)
                );
            }

            //Lajittelu päivämäärän mukaan nousevasti
            query.orderBy(criteriaBuilder.asc(root.get("eventDate")));

            //Palautetaan kaikki ehdot AND-muodossa
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}