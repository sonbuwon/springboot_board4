package com.example.tempboard.service;

import com.example.tempboard.domain.Board;
import com.example.tempboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void createBoardNoFile(Board board) {
        Board board1 = Board.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();

        this.boardRepository.save(board1);
    }

    public void createBoardWithFile(Board board) {
        Board board1 = Board.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .url(board.getUrl())
                .build();

        this.boardRepository.save(board1);
    }

    public Page<Board> listBoard(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").ascending());
        Page<Board> boards = this.boardRepository.findAll(pageable);
        return boards;
    }

    public Board detailBoard(Long id) {
        Optional<Board> op = this.boardRepository.findById(id);
        return op.orElseThrow();
    }

}
