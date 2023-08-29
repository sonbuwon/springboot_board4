package com.example.tempboard.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "제목은 필수사항입니다.")
    @Size(max = 100)
    private String title;

    @NotEmpty(message = "내용은 필수사항입니다.")
    @Column(length = 1000)
    private String content;

    private String url;

    @CreatedDate
    private LocalDateTime createdAt;
}
