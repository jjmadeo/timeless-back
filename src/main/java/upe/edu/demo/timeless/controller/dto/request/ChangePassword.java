package upe.edu.demo.timeless.controller.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChangePassword {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
