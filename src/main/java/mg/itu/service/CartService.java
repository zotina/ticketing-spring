package mg.itu.service;

import mg.itu.dto.DetailsReservationDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    private static final String CART_ATTRIBUTE = "cartDetails";

    public void addToCart(HttpSession session, DetailsReservationDTO detail) {
        List<DetailsReservationDTO> cart = getCart(session);
        detail.setIdDetailsReservation(UUID.randomUUID().toString());
        cart.add(detail);
        session.setAttribute(CART_ATTRIBUTE, cart);
    }

    public List<DetailsReservationDTO> getCart(HttpSession session) {
        List<DetailsReservationDTO> cart = (List<DetailsReservationDTO>) session.getAttribute(CART_ATTRIBUTE);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_ATTRIBUTE, cart);
        }
        return cart;
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_ATTRIBUTE);
    }
}