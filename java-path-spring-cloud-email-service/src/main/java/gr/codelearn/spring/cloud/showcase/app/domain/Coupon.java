package gr.codelearn.spring.cloud.showcase.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "COUPONS")
@SequenceGenerator(name = "idGenerator", sequenceName = "COUPONS_SEQ", initialValue = 1, allocationSize = 1)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Coupon extends BaseModel {
	@NotNull
	@Column(length = 36, nullable = false)
	private String code;

	private Float discountPercent;

	@Column(precision = 10, scale = 2)
	private BigDecimal discountAmount;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date generatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expiresAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date usedAt;
}
