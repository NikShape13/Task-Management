package com.example.demo.dto;

import java.util.List;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Performers")
public class PerformersDto {
    @Schema(description = "List of performers")
    private List<Long> performers; 

}
