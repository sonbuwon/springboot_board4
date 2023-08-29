package com.example.tempboard.repository;

import com.example.tempboard.domain.FileMock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<FileMock, Long> {


}
