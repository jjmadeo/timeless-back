package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Builder
@Data
@ToString
public class Error implements Serializable {

    private static final long serialVersionUID = 7650611080506011916L;
    private HttpStatus status;
    private String code;
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> details;

    public static final String PREFIX = "MS-TimeLess-";

    public Error(HttpStatus status, String code, String title, List<String> details) {
        this.status = status;
        this.code = StringUtils.isNumeric(code) ? PREFIX + code : code;
        this.title = title;
        this.details = details;
    }


}