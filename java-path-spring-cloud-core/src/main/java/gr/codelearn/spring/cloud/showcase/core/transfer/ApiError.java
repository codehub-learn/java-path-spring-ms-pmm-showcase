package gr.codelearn.spring.cloud.showcase.core.transfer;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class ApiError {
	Integer status;
	String message;
	String path;
}
