package gr.codelearn.spring.cloud.showcase.app.service;

import gr.codelearn.spring.cloud.showcase.app.domain.Coupon;
import gr.codelearn.spring.cloud.showcase.app.domain.Customer;
import gr.codelearn.spring.cloud.showcase.app.domain.Order;
import gr.codelearn.spring.cloud.showcase.app.domain.OrderItem;
import gr.codelearn.spring.cloud.showcase.app.domain.PaymentMethod;
import gr.codelearn.spring.cloud.showcase.app.domain.Product;
import gr.codelearn.spring.cloud.showcase.app.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends BaseServiceImpl<Order> implements OrderService {
	private final LoyaltyService loyaltyService;
	private final OrderRepository orderRepository;

	@Override
	public JpaRepository<Order, Long> getRepository() {
		return orderRepository;
	}

	@Override
	public Order initiateOrder(Customer customer) {
		return Order.builder().customer(customer).build();
	}

	@Override
	public void addItem(Order order, Product product, int quantity) {
		if (checkNullability(order, product)) {
			return;
		}

		boolean increasedQuantity = false;

		// If product is already contained in the order, don't add it again, just increase the quantity accordingly
		for (OrderItem oi : order.getOrderItems()) {
			if (oi.getProduct().getSerial().equals(product.getSerial())) {
				oi.setQuantity(oi.getQuantity() + quantity);
				increasedQuantity = true;
				break;
			}
		}

		if (!increasedQuantity) {
			order.add(newOrderItem(order, product, quantity));
		}

		logger.debug("Product[{}] added to Order[{}]", product, order);
	}

	private boolean checkNullability(Order order, Product product) {
		if (order == null) {
			logger.warn("Order is null.");
			return true;
		}
		if (product == null) {
			logger.warn("Product is null.");
			return true;
		}
		return false;
	}

	@Override
	public void updateItem(Order order, Product product, int quantity) {
		if (checkNullability(order, product)) {
			return;
		}

		order.getOrderItems().removeIf(oi -> oi.getProduct().getSerial().equals(product.getSerial()));
		order.getOrderItems().add(newOrderItem(order, product, quantity));

		logger.debug("Product[{}] updated in Order[{}]", product, order);
	}

	private OrderItem newOrderItem(Order order, Product product, int quantity) {
		//@formatter:off
		return OrderItem.builder()
				.product(product)
				.order(order)
				.quantity(quantity)
				.price(product.getPrice()).build();
		//@formatter:on
	}

	@Override
	public void deleteItem(Order order, Product product) {
		if (checkNullability(order, product)) {
			return;
		}

		order.getOrderItems().removeIf(oi -> oi.getProduct().getSerial().equals(product.getSerial()));
		logger.debug("Product[{}] removed from Order[{}]", product, order);
	}

	@Override
	public Order checkout(Order order, PaymentMethod paymentMethod) {
		if (!validate(order)) {
			logger.warn("Order should have customer, order items, and payment type defined before being able to be " +
								"checkout the order.");
			return null;
		}

		// Set all order fields with proper values
		order.setPaymentMethod(paymentMethod);
		order.setSubmitDate(new Date());

		//Check for potential loyalty actions
		var coupon = loyaltyService.apply(order);

		order.setCost(giveDiscount(order, coupon));
		if (coupon.isPresent()) {
			order.setCouponCode(coupon.get().getCode());
		}

		var savedOrder = save(order);
		if (coupon.isPresent()) {
			loyaltyService.declare(coupon.get());
		}
		return savedOrder;
	}

	private boolean validate(Order order) {
		return order != null && !order.getOrderItems().isEmpty() && order.getCustomer() != null;
	}

	private BigDecimal giveDiscount(Order order, Optional<Coupon> coupon) {
		var totalDiscount =
				order.getCustomer().getCustomerCategory().getDiscount() + order.getPaymentMethod().getDiscount();

		if (coupon.isPresent()) {
			totalDiscount += coupon.get().getDiscountPercent();
		}

		//@formatter:off
		// Calculate original order cost
		var originalCost = order.getOrderItems().stream()
				.map(oi -> oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		//@formatter:on

		// Apply discounts
		var costAfterDiscount = originalCost.multiply(BigDecimal.valueOf(1F - totalDiscount));

		logger.debug("Order[{}], originalCost: {}, totalDiscount: {}%, finalCost: {}.", order.getId(), originalCost,
					 totalDiscount * 100, costAfterDiscount);

		return costAfterDiscount;
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAllLazy();
	}

	public Order findLazy(Long id) {
		return orderRepository.findLazy(id);
	}
}
