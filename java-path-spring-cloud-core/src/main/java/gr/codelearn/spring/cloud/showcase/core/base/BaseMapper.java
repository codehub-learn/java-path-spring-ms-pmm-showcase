package gr.codelearn.spring.cloud.showcase.core.base;

import gr.codelearn.spring.cloud.showcase.core.domain.BaseModel;
import gr.codelearn.spring.cloud.showcase.core.transfer.resource.BaseModelResource;

import java.util.List;

public interface BaseMapper<D extends BaseModel, R extends BaseModelResource> {
	R toResource(D domain);

	List<R> toResource(List<D> domains);

	D toDomain(R resource);

	List<D> toDomain(List<R> resources);
}
