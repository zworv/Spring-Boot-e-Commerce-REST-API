package josh.ecommerce.Service;

import josh.ecommerce.DTO.*;
import josh.ecommerce.Entity.*;
import josh.ecommerce.Repository.CartItemRepository;
import josh.ecommerce.Repository.CartRepository;
import josh.ecommerce.Repository.ProductRepository;
import josh.ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public CartDto addCart(Integer customerId) {
        User customer = userRepository.findByRoleAndId(Role.CUSTOMER, customerId).orElse(null);
        if(customer == null) {
            throw new UsernameNotFoundException("Cannot find the non-exist user");
        }

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setPrice(0F);

        return new CartDto(cartRepository.save(cart));
    }

    public CartDto getCart(Integer customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
        if(cart == null) {
            throw new RuntimeException("Cannot find the non-exist cart");
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        CartDto cartDto = new CartDto(cart);
        cartDto.setCartItemDtoList(cartItems.stream().map(CartItemDto::new).toList());
        return cartDto;
    }

    @Transactional
    public void deleteCart(Integer customerId) {
        clearCartItem(customerId);
        cartRepository.deleteByCustomerId(customerId);
    }

    @Transactional
    public CartDto updateCartItem(CartItemUpdateDto cartItemUpdate, Integer customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
        if(cart == null) {
            throw new RuntimeException("Cannot find the non-exist cart");
        }

        Product product = productRepository.findById(cartItemUpdate.getProductId()).orElse(null);
        if(product == null) {
            return null;
        }

//        Update cartItem

        if(cartItemUpdate.getQuantity() <= 0) {
            cartItemRepository.deleteByCartIdAndProductId(cart.getId(), product.getId());
        }
        else {
            Optional<CartItem> optionalCartItem = cartItemRepository
                    .findByCartIdAndProductId(cart.getId(), product.getId());

            CartItem cartItem;
            if(optionalCartItem.isPresent()) {
                cartItem = optionalCartItem.get();
            }
            else {
                cartItem = new CartItem();
                cartItem.setName(product.getName());
                cartItem.setPrice(product.getPrice());
                cartItem.setProduct(product);
                cartItem.setCart(cart);
            }
            cartItem.setQuantity(Math.min(cartItemUpdate.getQuantity(), product.getQuantity()));

            cartItemRepository.save(cartItem);
        }

//        Calculate price

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        float sum = 0F;

        for(CartItem cartItem : cartItems) {
            sum += cartItem.getPrice() * cartItem.getQuantity();
        }

        cart.setPrice(sum);

        Cart addedCart = cartRepository.save(cart);

//        Get cartDto

        CartDto cartDto = new CartDto(addedCart);
        cartDto.setCartItemDtoList(cartItems.stream().map(CartItemDto::new).toList());
        return cartDto;
    }

    @Transactional
    public CartDto clearCartItem(Integer customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
        if(cart == null) {
            throw new RuntimeException("Cannot find the non-exist cart");
        }

        cartItemRepository.deleteByCartId(cart.getId());

        cart.setPrice(0F);

        CartDto cartDto = new CartDto(cartRepository.save(cart));
        cartDto.setCartItemDtoList(
                cartItemRepository.findByCartId(cartDto.getId())
                        .stream()
                        .map(CartItemDto::new)
                        .toList()
        );
        return cartDto;
    }

}
