package gr.codelearn.spring.cloud.showcase.catalog.domain;

import gr.codelearn.spring.cloud.showcase.core.domain.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CATEGORIES")
@SequenceGenerator(name = "idGenerator", sequenceName = "CATEGORIES_SEQ", initialValue = 1, allocationSize = 1)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Category extends BaseModel {
	@NotNull
	@Column(length = 50, nullable = false)
	private String description;
}
