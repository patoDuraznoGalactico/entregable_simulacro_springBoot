package riwi.riwi.riwi_education.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import riwi.riwi.riwi_education.domain.entities.Lessons;

@Repository
public interface LessonsRepository extends JpaRepository<Lessons, Integer>{
    
}