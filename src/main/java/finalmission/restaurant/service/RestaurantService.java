package finalmission.restaurant.service;

import finalmission.member.domain.Member;
import finalmission.member.exception.MemberNotExistsException;
import finalmission.member.infrastructure.MemberJpaRepository;
import finalmission.restaurant.domain.Restaurant;
import finalmission.restaurant.dto.RestaurantRequest;
import finalmission.restaurant.dto.RestaurantResponse;
import finalmission.restaurant.infrastructure.RestaurantJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public RestaurantResponse createRestaurant(final RestaurantRequest request, final String email) {
        final Member member = memberJpaRepository.findByEmail(email)
                .orElseThrow(MemberNotExistsException::new);

        final Restaurant restaurant = Restaurant.builder()
                .name(request.name())
                .member(member)
                .build();

        final Restaurant savedRestaurant = restaurantJpaRepository.save(restaurant);
        return RestaurantResponse.from(savedRestaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> findAll(){
        return restaurantJpaRepository.findAll()
                .stream()
                .map(RestaurantResponse::from)
                .toList();
    }
}
