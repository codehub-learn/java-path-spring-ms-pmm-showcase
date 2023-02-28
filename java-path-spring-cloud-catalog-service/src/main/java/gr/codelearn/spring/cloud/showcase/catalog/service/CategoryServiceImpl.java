package gr.codelearn.spring.cloud.showcase.catalog.service;

import gr.codelearn.spring.cloud.showcase.catalog.domain.Category;
import gr.codelearn.spring.cloud.showcase.catalog.repository.CategoryRepository;
import gr.codelearn.spring.cloud.showcase.core.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public JpaRepository<Category, Long> getRepository() {
		return categoryRepository;
	}
}
