package com.example.tempboard;

import com.example.tempboard.domain.Board;
import com.example.tempboard.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardTest {

    @Autowired
    BoardService boardService;

    @Test
    public void testCreate() {

    }

}
