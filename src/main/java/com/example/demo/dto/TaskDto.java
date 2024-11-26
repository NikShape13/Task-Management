package com.example.demo.dto;

import java.util.List;

import org.hibernate.annotations.Comments;

import com.example.demo.dao.TokenRepository;
import com.example.demo.models.Comment;
import com.example.demo.models.PasswordEncoder;
import com.example.demo.models.Role;
import com.example.demo.models.Task;
import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserService;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task")
public class TaskDto {
	@Schema(hidden = true)
    private Long id;
    @Schema(description = "Title")
    @Size(max = 1000, message = "Title less than 1000 symbols")
    @NotBlank(message = "Title can not be blank")
    private String title;
    @Schema(description = "Description")
    @Size(max = 1000, message = "Description less than 1000 symbols")
    @NotBlank(message = "Description can not be blank")
    private String description;
    @Schema(description = "Status: [PENDING, IN_PROGRESS, COMPLETED]")
    private TaskStatus status;
    @Schema(description = "Priority: [HIGH, MEDIUM, LOW]")
    private TaskPriority priority;
    @Schema(description = "Author id", hidden = true)
    private Long authorId; 
    @Schema(description = "List of performers")
    private List<Long> performers;
    @Schema(hidden = true)
    private List<CommentDto> comments;
}
