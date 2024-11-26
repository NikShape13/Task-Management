package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	    private Long id;
	    private String content;
	    private Long task_id;
	    private Long user_id;
}
