package com.example.tempboard.controller;

import com.example.tempboard.domain.FileMock;
import com.example.tempboard.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/hello")
public class HelloController {

    private final HelloService helloService;

    @GetMapping
    public String hello(Model model) {

        List<FileMock> fileMocks = this.helloService.list();
        model.addAttribute("fileMocks", fileMocks);

        return "temp/hello";
    }

    @GetMapping("/upload")
    public String fileIndex() {
        return "temp/upload";
    }
    // 파일업로드
    @PostMapping("/upload")
    public String handleFileUpload(FileMock fileMock, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if (!file.isEmpty()) {
            try {
                // 파일 저장 경로
                String savePath = "D:\\temp\\img\\";
                // 같은 파일을 저장하더라도 이름 겹치지 않게 파일 저장
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path path = Paths.get(savePath + fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                FileMock uploadFile = FileMock.builder()
                        .title(fileMock.getTitle())
                        .url(savePath + fileName)
                        .build();

                this.helloService.createBoardWithFile(uploadFile);

                redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " because it was empty");
        }

        return "redirect:/hello";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> imageFile(@PathVariable Long id) {
        String filePath = this.helloService.getFile(id).getUrl();

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
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "temp/detail";
    }
}
