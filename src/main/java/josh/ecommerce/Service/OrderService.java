package josh.ecommerce.Service;

import josh.ecommerce.DTO.*;
import josh.ecommerce.Entity.*;
import josh.ecommerce.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    private OrderDto deductInventoryAndSaveOrder(User seller, User customer,
                                                  List<CartItem> cartItems,
                                                  OrderCreateDto orderCreateDto) {
//        save order

        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();

        float price = 0F;

        order.setSeller(seller);
        order.setCustomer(customer);
        order.setAddress(orderCreateDto.getAddress());
        order.setCreditCard(orderCreateDto.getCreditCard());

        for(CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId()).orElse(null);
            if(product == null) {
                throw new RuntimeException("Cannot find non-exist product");
            }

            price += cartItem.getPrice() * cartItem.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setName(product.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
        }

        order.setPrice(price);

        order.setOrderStartDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PROCESSING);

        List<OrderItem> addedOrderItems = orderItems
                .stream()
                .map(orderItem -> orderItemRepository.save(orderItem))
                .toList();
        Order addedOrder = orderRepository.save(order);


//        deduct inventory

        for(CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getId()).orElse(null);
            if(product == null) {
                throw new RuntimeException("Something wrong, when find product by id");
            }

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            ProductUpdateDto productUpdateDto = new ProductUpdateDto();
            productUpdateDto.setId(product.getId());
            productUpdateDto.setName(product.getName());
            productUpdateDto.setDescription(product.getDescription());
            productUpdateDto.setQuantity(product.getQuantity());
            productUpdateDto.setPrice(product.getPrice());

            ProductDto productDto = productService.updateProduct(productUpdateDto, product.getSeller());
            if(productDto == null) {
                throw new RuntimeException("Something wrong, when update product");
            }
        }

//        clear cartItem

        cartService.clearCartItem(customer.getId());

//        return

        List<OrderItemDto> orderItemDtoList = addedOrderItems
                .stream()
                .map(OrderItemDto::new)
                .toList();
        OrderDto orderDto = new OrderDto(addedOrder);
        orderDto.setOrderItemDtoList(orderItemDtoList);
        return orderDto;
    }

    public List<OrderDto> addOrders(OrderCreateDto orderCreateDto, User customer) {
//        check cartItem validation
        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElse(null);
        if(cart == null) {
            return null;
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if(cartItems.isEmpty()) {
            return null;
        }

        for(CartItem cartItem : cartItems) {
            if(!productRepository.existsById(cartItem.getProduct().getId())) {
                return null;
            }
        }

        for(CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId()).orElse(null);
            if(product == null) {
                throw new RuntimeException("Something wrong, when find product by id");
            }

            if(product.getQuantity() <= 0 || product.getQuantity() < cartItem.getQuantity()) {
                return null;
            }
        }

//        same product seller as group

        Map<User, List<CartItem>> sellerAndCartItems = new HashMap<>();

        for(CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId()).orElse(null);
            if(product == null || product.getSeller() == null) {
                throw new RuntimeException("Cannot find non-exist product/seller");
            }

            User seller = userRepository.findByRoleAndId(Role.SELLER, product.getSeller().getId()).orElse(null);
            if(seller == null) {
                throw new RuntimeException("Cannot find non-exist user");
            }

            List<CartItem> subCartItems = sellerAndCartItems.getOrDefault(seller, new ArrayList<>());
            subCartItems.add(cartItem);
            sellerAndCartItems.put(seller, subCartItems);
        }

//        process each group of product

        List<OrderDto> orderDtoList = new ArrayList<>();
        for(Map.Entry<User, List<CartItem>> entry : sellerAndCartItems.entrySet()) {
            orderDtoList.add(
                    deductInventoryAndSaveOrder(entry.getKey(), customer, entry.getValue(), orderCreateDto)
            );
        }
        return orderDtoList;
    }

    public OrderDto getOrder(Integer id, User user) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null || !(order.getSeller().equals(user) || order.getCustomer().equals(user))) {
            return null;
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);

        OrderDto orderDto = new OrderDto(order);
        orderDto.setOrderItemDtoList(
                orderItems.stream().map(OrderItemDto::new).toList()
        );
        return orderDto;
    }

    public List<OrderDto> getOrders(User user) {
        if(Objects.equals(user.getRole(), Role.SELLER)) {
            List<OrderDto> orderDtoList = new ArrayList<>();

            List<Order> orders = orderRepository.findBySellerId(user.getId());

            for(Order order : orders) {
                List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

                OrderDto orderDto = new OrderDto(order);
                orderDto.setOrderItemDtoList(
                        orderItems.stream().map(OrderItemDto::new).toList()
                );

                orderDtoList.add(orderDto);
            }

            return orderDtoList;
        }
        else if(Objects.equals(user.getRole(), Role.CUSTOMER)) {
            List<OrderDto> orderDtoList = new ArrayList<>();

            List<Order> orders = orderRepository.findByCustomerId(user.getId());

            for(Order order : orders) {
                List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

                OrderDto orderDto = new OrderDto(order);
                orderDto.setOrderItemDtoList(
                        orderItems.stream().map(OrderItemDto::new).toList()
                );

                orderDtoList.add(orderDto);
            }

            return orderDtoList;
        }
        else {
            return null;
        }
    }

    public OrderDto updateOrder(OrderUpdateDto orderUpdateDto, User user) {
//        check order, orderUpdate and user validation
        Order order = orderRepository.findById(orderUpdateDto.getId()).orElse(null);
        if(order == null) {
            return null;
        }
        if(!Objects.equals(OrderStatus.PROCESSING, order.getOrderStatus())) {
            return null;
        }

        if(Objects.equals(Role.SELLER, user.getRole())) {
            if(Objects.equals(OrderStatus.PROCESSING, orderUpdateDto.getOrderStatus()) ||
                    !Objects.equals(order.getSeller().getId(), user.getId())) {
                return null;
            }
        }
        if(Objects.equals(Role.CUSTOMER, user.getRole())) {
            if(!Objects.equals(OrderStatus.CANCELED, orderUpdateDto.getOrderStatus()) ||
                    !Objects.equals(order.getCustomer().getId(), user.getId())) {
                return null;
            }
        }

//        update order

        order.setOrderStatus(orderUpdateDto.getOrderStatus());

        if(Objects.equals(OrderStatus.COMPLETED, orderUpdateDto.getOrderStatus())) {
            order.setOrderCompleteDate(LocalDate.now());
        }
        if(Objects.equals(OrderStatus.CANCELED, orderUpdateDto.getOrderStatus())) {
            order.setOrderCompleteDate(LocalDate.now());

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderUpdateDto.getId());

            for(OrderItem orderItem : orderItems) {
                Product product = productRepository.findById(orderItem.getProduct().getId()).orElse(null);
                if(product == null) {
                    throw new RuntimeException("Cannot find the non-exist product");
                }

                product.setQuantity(product.getQuantity() + orderItem.getQuantity());

                ProductUpdateDto productUpdateDto = new ProductUpdateDto();
                productUpdateDto.setId(product.getId());
                productUpdateDto.setName(product.getName());
                productUpdateDto.setDescription(product.getDescription());
                productUpdateDto.setQuantity(product.getQuantity());
                productUpdateDto.setPrice(product.getPrice());

                ProductDto productDto = productService.updateProduct(productUpdateDto, product.getSeller());
                if(productDto == null) {
                    throw new RuntimeException("Something wrong, when update product");
                }
            }
        }

        Order addedOrder = orderRepository.save(order);

        OrderDto orderDto = new OrderDto(addedOrder);
        orderDto.setOrderItemDtoList(
                orderItemRepository.findByOrderId(orderUpdateDto.getId())
                        .stream()
                        .map(OrderItemDto::new)
                        .toList()
        );
        return orderDto;
    }

}
