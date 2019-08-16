package com.mindtree.webstarter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindtree.webstarter.model.Greeting;

@Repository
public interface GreetingRepository extends JpaRepository<Greeting, Long> {

}
