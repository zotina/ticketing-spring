package mg.itu.mapper;

import mg.itu.dto.DetailsReservationDTO;
import mg.itu.dto.ReservationDTO;
import mg.itu.model.DetailsReservation;
import mg.itu.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "vol.idVol", target = "idVol")
    @Mapping(target = "volInfo", expression = "java(reservation.getVol().getVilleDepart().getNom() + \" - \" + reservation.getVol().getVilleArrivee().getNom())")
    ReservationDTO toReservationDTO(Reservation reservation);

    @Mapping(source = "reservation.idReservation", target = "idReservation")
    DetailsReservationDTO toDetailsReservationDTO(DetailsReservation detailsReservation);

    DetailsReservation toDetailsReservation(DetailsReservationDTO detailsReservationDTO);
}