package gr.codelearn.spring.cloud.showcase.catalog.controller;

import gr.codelearn.spring.cloud.showcase.catalog.domain.Product;
import gr.codelearn.spring.cloud.showcase.catalog.mapper.ProductMapper;
import gr.codelearn.spring.cloud.showcase.catalog.service.ProductService;
import gr.codelearn.spring.cloud.showcase.core.base.BaseMapper;
import gr.codelearn.spring.cloud.showcase.core.controller.BaseController;
import gr.codelearn.spring.cloud.showcase.core.service.BaseService;
import gr.codelearn.spring.cloud.showcase.core.transfer.ApiResponse;
import gr.codelearn.spring.cloud.showcase.core.transfer.resource.ProductResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController extends BaseController<Product, ProductResource> {
	private final ProductService productService;
	private final ProductMapper productMapper;

	@Override
	public BaseService<Product, Long> getBaseService() {
		return productService;
	}

	@Override
	protected BaseMapper<Product, ProductResource> getBaseMapper() {
		return productMapper;
	}

	@GetMapping(params = {"serial"})
	public ResponseEntity<ApiResponse<ProductResource>> findBySerial(@RequestParam(name = "serial") String serial) {
		return ResponseEntity.ok(ApiResponse.<ProductResource>builder()
										 .data(getBaseMapper().toResource(productService.findBySerial(serial)))
										 .build());
	}
}
