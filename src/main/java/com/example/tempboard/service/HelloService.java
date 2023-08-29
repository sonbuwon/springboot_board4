package com.example.tempboard.service;

import com.example.tempboard.domain.FileMock;
import com.example.tempboard.repository.HelloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HelloService {

    private final HelloRepository helloRepository;

    public List<FileMock> list() {
        return this.helloRepository.findAll();
    }

    public void createBoardWithFile(FileMock file) {

        FileMock fileMock = FileMock.builder()
                .title(file.getTitle())
                .url(file.getUrl())
                .build();

        this.helloRepository.save(fileMock);

    }

    public FileMock getFile(Long id) {
        return this.helloRepository.findById(id).orElseThrow();
    }
}
