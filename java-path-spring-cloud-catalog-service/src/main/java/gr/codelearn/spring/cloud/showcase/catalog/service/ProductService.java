package gr.codelearn.spring.cloud.showcase.catalog.service;

import gr.codelearn.spring.cloud.showcase.catalog.domain.Product;
import gr.codelearn.spring.cloud.showcase.core.service.BaseService;
import gr.codelearn.spring.cloud.showcase.core.transfer.KeyValue;

import java.util.List;

public interface ProductService extends BaseService<Product, Long> {
	Product findBySerial(String serial);

	List<KeyValue<String, Integer>> findProductSaleFrequency();
}
