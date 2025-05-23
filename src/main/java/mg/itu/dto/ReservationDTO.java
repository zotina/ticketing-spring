package mg.itu.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationDTO {

    private String idReservation;
    private LocalDateTime dateReservation;
    private BigDecimal prix;
    private String idVol;
    private String volInfo; 

    
    public ReservationDTO() {}

    
    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public String getIdVol() {
        return idVol;
    }

    public void setIdVol(String idVol) {
        this.idVol = idVol;
    }

    public String getVolInfo() {
        return volInfo;
    }

    public void setVolInfo(String volInfo) {
        this.volInfo = volInfo;
    }
}