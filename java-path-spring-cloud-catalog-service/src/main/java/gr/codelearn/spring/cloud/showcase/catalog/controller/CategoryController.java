package gr.codelearn.spring.cloud.showcase.catalog.controller;

import gr.codelearn.spring.cloud.showcase.catalog.domain.Category;
import gr.codelearn.spring.cloud.showcase.catalog.service.CategoryService;
import gr.codelearn.spring.cloud.showcase.core.controller.BaseController;
import gr.codelearn.spring.cloud.showcase.core.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController extends BaseController<Category> {
	private final CategoryService categoryService;

	@Override
	public BaseService<Category, Long> getBaseService() {
		return categoryService;
	}
}
