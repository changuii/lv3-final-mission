package finalmission.reservation.domain;

import finalmission.member.domain.Member;
import finalmission.reservation.exception.ReservationNotPendingException;
import finalmission.reservationtime.domain.ReservationTime;
import finalmission.restaurant.domain.Restaurant;
import finalmission.restaurant.exception.RestaurantNotOwnerException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationState reservationState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ReservationTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    public boolean isOwnMember(final Member member){
        return this.member.equals(member);
    }

    public void accept(final Member restaurantOwner){
        validateRestaurantOwner(restaurantOwner);
        validateReservationState();
        reservationState = ReservationState.ACCEPT;
    }

    public void reject(final Member restaurantOwner){
        validateRestaurantOwner(restaurantOwner);
        validateReservationState();
        reservationState = ReservationState.REJECT;
    }

    private void validateReservationState() {
        if(this.reservationState != ReservationState.PENDING){
            throw new ReservationNotPendingException();
        }
    }

    private void validateRestaurantOwner(final Member restaurantOwner) {
        if(!this.restaurant.isOwner(restaurantOwner)){
            throw new RestaurantNotOwnerException();
        }
    }
}
