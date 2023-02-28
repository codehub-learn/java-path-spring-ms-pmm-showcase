package gr.codelearn.spring.cloud.showcase.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "PRODUCTS")
@SequenceGenerator(name = "idGenerator", sequenceName = "PRODUCTS_SEQ", initialValue = 1, allocationSize = 1)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseModel {
	@NotNull
	@Column(length = 50, nullable = false, unique = true)
	private String serial;

	@NotNull
	@Column(length = 50, nullable = false)
	private String name;

	@NotNull
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal price;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Category category;
}
