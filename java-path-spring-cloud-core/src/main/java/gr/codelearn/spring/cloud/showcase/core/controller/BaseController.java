package gr.codelearn.spring.cloud.showcase.core.controller;

import gr.codelearn.spring.cloud.showcase.core.base.BaseComponent;
import gr.codelearn.spring.cloud.showcase.core.base.BaseMapper;
import gr.codelearn.spring.cloud.showcase.core.domain.BaseModel;
import gr.codelearn.spring.cloud.showcase.core.service.BaseService;
import gr.codelearn.spring.cloud.showcase.core.transfer.ApiResponse;
import gr.codelearn.spring.cloud.showcase.core.transfer.resource.BaseModelResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public abstract class BaseController<T extends BaseModel, R extends BaseModelResource> extends BaseComponent {
	protected abstract BaseService<T, Long> getBaseService();

	protected abstract BaseMapper<T, R> getBaseMapper();

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<R>> findById(@PathVariable("id") final Long id) {
		return ResponseEntity.ok(
				ApiResponse.<R>builder().data(getBaseMapper().toResource(getBaseService().findById(id))).build());
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<R>>> findAll() {
		return ResponseEntity.ok(
				ApiResponse.<List<R>>builder().data(getBaseMapper().toResource(getBaseService().findAll())).build());
	}

	@PostMapping
	public ResponseEntity<ApiResponse<R>> create(@Valid @RequestBody final R resource) {
		//@formatter:off
		return new ResponseEntity<>(
				ApiResponse.<R>builder().
						data(
								getBaseMapper().toResource(
										getBaseService().save(
												getBaseMapper().toDomain(resource))
										)
						).build(),
				getNoCacheHeaders(),
				HttpStatus.CREATED);
		//@formatter:on
	}

	@PutMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@Valid @RequestBody final R resource) {
		getBaseService().update(getBaseMapper().toDomain(resource));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") final Long id) {
		getBaseService().deleteById(id);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@Valid @RequestBody final R resource) {
		if (getBaseService().exists(getBaseMapper().toDomain(resource))) {
			getBaseService().delete(getBaseMapper().toDomain(resource));
		}
	}

	protected HttpHeaders getNoCacheHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return headers;
	}

	protected HttpHeaders getDownloadHeaders(final String filename) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + filename);
		return headers;
	}
}
