package com.mukho.mini.post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {

    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=50)
    private String subject;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}