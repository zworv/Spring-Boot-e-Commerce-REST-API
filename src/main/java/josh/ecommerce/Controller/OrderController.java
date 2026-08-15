package josh.ecommerce.Controller;

import jakarta.validation.Valid;
import josh.ecommerce.DTO.OrderCreateDto;
import josh.ecommerce.DTO.OrderDto;
import josh.ecommerce.DTO.OrderUpdateDto;
import josh.ecommerce.Entity.OrderStatus;
import josh.ecommerce.Entity.User;
import josh.ecommerce.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<List<OrderDto>> addOrders(@Valid @RequestBody OrderCreateDto orderCreateDto,
                                                    @AuthenticationPrincipal User customer) {
        List<OrderDto> orderDtoList = orderService.addOrders(orderCreateDto, customer);
        if(orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderDtoList, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Integer id,
                                             @AuthenticationPrincipal User user) {
        OrderDto orderDto = orderService.getOrder(id, user);
        if(orderDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(@AuthenticationPrincipal User user) {
        List<OrderDto> orderDtoList = orderService.getOrders(user);
        if(orderDtoList == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody OrderUpdateDto orderUpdateDto,
                                                @AuthenticationPrincipal User seller) {
        if(Objects.equals(OrderStatus.CANCELED, orderUpdateDto.getOrderStatus())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        OrderDto orderDto = orderService.updateOrder(orderUpdateDto, seller);
        if(orderDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<OrderDto> cancelOrder(@Valid @RequestBody OrderUpdateDto orderUpdateDto,
                                                @AuthenticationPrincipal User user) {
        if(!Objects.equals(OrderStatus.CANCELED, orderUpdateDto.getOrderStatus())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        OrderDto orderDto = orderService.updateOrder(orderUpdateDto, user);
        if(orderDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

}
