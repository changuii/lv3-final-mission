package finalmission.restaurant.dto;

import finalmission.restaurant.domain.Restaurant;

public record RestaurantResponse(
        Long id,
        String name
) {

    public static RestaurantResponse from(final Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName()
        );
    }
}
