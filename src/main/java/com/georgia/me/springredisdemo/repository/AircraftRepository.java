package com.georgia.me.springredisdemo.repository;

import com.georgia.me.springredisdemo.dto.Aircraft;
import org.springframework.data.repository.CrudRepository;

public interface AircraftRepository extends CrudRepository<Aircraft, Long> {
}
