package uz.test.abitur.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppErrorDTO {
    private final String errorPath;
    private final String errorMessage;
    private final Object errorBody;
    private final Long timestamp;

    public AppErrorDTO(String errorPath, String errorMessage) {
        this(errorPath, errorMessage, null);
    }

    public AppErrorDTO(String errorPath, String errorMessage, Object errorBody) {
        this.errorPath = errorPath;
        this.errorMessage = errorMessage;
        this.errorBody = errorBody;
        this.timestamp = System.currentTimeMillis();
    }
}

