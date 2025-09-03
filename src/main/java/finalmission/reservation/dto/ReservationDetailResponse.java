package finalmission.reservation.dto;

import finalmission.member.dto.MemberResponse;
import finalmission.reservation.domain.Reservation;
import finalmission.reservationtime.dto.ReservationTimeResponse;
import finalmission.restaurant.dto.RestaurantResponse;
import java.time.LocalDate;

public record ReservationDetailResponse(
        Long id,
        LocalDate date,
        String state,
        ReservationTimeResponse time,
        MemberResponse member,
        RestaurantResponse restaurant
){

    public static ReservationDetailResponse from(final Reservation reservation){
        return new ReservationDetailResponse(
                reservation.getId(),
                reservation.getDate(),
                reservation.getReservationState().getMessage(),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                MemberResponse.from(reservation.getMember()),
                RestaurantResponse.from(reservation.getRestaurant())
        );
    }
}
