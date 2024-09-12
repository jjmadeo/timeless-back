package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
public class MultiEntityResponse<T> {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<T> data;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private java.lang.Error error;
}
