package gr.codelearn.spring.cloud.showcase.core.transfer.resource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CategoryResource extends BaseModelResource {
	protected String description;
}
