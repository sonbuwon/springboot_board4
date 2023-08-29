package com.example.tempboard.controller;

import com.example.tempboard.domain.Board;
import com.example.tempboard.domain.FileMock;
import com.example.tempboard.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록
    @GetMapping("/list")
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {

//        List<Board> boards = this.boardService.listBoard();
//        model.addAttribute("boardList", boards);
        Page<Board> boards = this.boardService.listBoard(page);
        model.addAttribute("boardPages", boards);

        return "board/list";
    }

    // 게시글 작성
    @GetMapping("/create")
    public String create(Board board) {

        return "board/create";
    }
    @PostMapping("/create")
    public String create(@Valid Board board, BindingResult bindingResult, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            return "board/create";
        }

        if(file.isEmpty()) {
            this.boardService.createBoardNoFile(board);
        } else {
            try {
                // 파일 저장 경로
                String savePath = "D:\\temp\\boardimg\\";
                // 같은 파일을 저장하더라도 이름 겹치지 않게 파일 저장
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path path = Paths.get(savePath + fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                Board uploadFile = Board.builder()
                        .title(board.getTitle())
                        .content(board.getContent())
                        .url(savePath + fileName)
                        .build();

                this.boardService.createBoardWithFile(uploadFile);

                redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        }

        return "redirect:/board/list";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> imageFile(@PathVariable Long id) {
        String filePath = this.boardService.detailBoard(id).getUrl();
            try {
                Path path = Paths.get(filePath);

                byte[] imageBytes = Files.readAllBytes(path);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);

                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    // 게시글 상세보기
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {

        Board board = this.boardService.detailBoard(id);
        model.addAttribute("board", board);

        return "board/detail";
    }
}
