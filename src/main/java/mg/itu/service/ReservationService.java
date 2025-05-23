package mg.itu.service;

import mg.itu.dto.ReservationDTO;
import mg.itu.mapper.ReservationMapper;
import mg.itu.model.DetailsReservation;
import mg.itu.model.Reservation;
import mg.itu.repository.DetailsReservationRepository;
import mg.itu.repository.ReservationClasseRepository;
import mg.itu.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DetailsReservationRepository detailsReservationRepository;

    @Autowired
    private ReservationClasseRepository reservationClasseRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${api.base-url}")
    private String baseApiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toReservationDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReservationDTO> getReservationById(String id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::toReservationDTO);
    }

    public List<DetailsReservation> getDetailsByReservationId(String idReservation) {
        return detailsReservationRepository.findByReservationIdReservation(idReservation);
    }

    public boolean canAddDetail(String idReservation, int currentDetailCount) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(idReservation);
        if (reservationOpt.isPresent()) {
            Integer maxDetails = reservationClasseRepository.sumNombreByReservationId(idReservation);
            logger.debug("Limite pour idReservation {}: {} détails, actuel: {}", idReservation, maxDetails, currentDetailCount);
            return maxDetails != null && currentDetailCount < maxDetails;
        }
        return false;
    }

    @Transactional
    public void saveDetails(List<DetailsReservation> detailsList, String idReservation) {
        logger.info("Début de l'enregistrement des détails pour la réservation : {}", idReservation);
        Optional<Reservation> reservationOpt = reservationRepository.findById(idReservation);
        if (!reservationOpt.isPresent()) {
            logger.error("Réservation non trouvée : {}", idReservation);
            throw new IllegalArgumentException("Réservation non trouvée : " + idReservation);
        }
        Reservation reservation = reservationOpt.get();
        logger.debug("Réservation chargée : id={}", reservation.getIdReservation());

        for (DetailsReservation detail : detailsList) {
            logger.debug("Traitement du détail - nom: {}, age: {}, passport: {}, idReservation: {}", 
                        detail.getNom(), detail.getAge(), detail.getPassport(), idReservation);
            if (detail.getIdDetailsReservation() == null) {
                String newId = UUID.randomUUID().toString();
                detail.setIdDetailsReservation(newId);
                logger.debug("ID généré pour le détail : {}", newId);
            }
            detail.setReservation(reservation);
            try {
                logger.debug("Avant sauvegarde - détail: id={}, nom={}", detail.getIdDetailsReservation(), detail.getNom());
                DetailsReservation savedDetail = detailsReservationRepository.save(detail);
                detailsReservationRepository.flush();
                logger.info("Détail enregistré avec succès : id={}, nom={}", savedDetail.getIdDetailsReservation(), savedDetail.getNom());
                
                Optional<DetailsReservation> verifiedDetail = detailsReservationRepository.findById(savedDetail.getIdDetailsReservation());
                if (verifiedDetail.isPresent()) {
                    logger.info("Vérification réussie : détail présent dans la base avec id={}", savedDetail.getIdDetailsReservation());
                } else {
                    logger.error("Échec de la vérification : détail avec id={} non trouvé après insertion", savedDetail.getIdDetailsReservation());
                    throw new RuntimeException("Détail non persisté dans la base de données");
                }
            } catch (Exception e) {
                logger.error("Erreur lors de l'enregistrement du détail {}: {}", detail.getNom(), e.getMessage(), e);
                throw new RuntimeException("Erreur lors de l'enregistrement du détail : " + e.getMessage(), e);
            }
        }
        logger.info("Enregistrement des détails terminé pour la réservation : {}", idReservation);
    }

    public byte[] exportReservationPdf(String idReservation) throws Exception {
        String exportUrl = baseApiUrl.trim() + "export_pdf?reservationId=" + idReservation;
        WebClient client = webClientBuilder.baseUrl(exportUrl).build();

        String response = client.get()
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Erreur lors de la récupération du PDF");
        }

        // Parse JSON response
        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
        Boolean success = (Boolean) responseMap.get("success");
        if (!success) {
            throw new RuntimeException("Erreur API: " + responseMap.get("response"));
        }

        // Decode Base64 data
        String base64Data = (String) responseMap.get("data");
        if (base64Data == null) {
            throw new RuntimeException("Aucune donnée PDF reçue");
        }
        return Base64.getDecoder().decode(base64Data);
    }
}