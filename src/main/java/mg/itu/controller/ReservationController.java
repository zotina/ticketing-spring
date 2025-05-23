package mg.itu.controller;

import mg.itu.dto.ReservationDTO;
import mg.itu.model.DetailsReservation;
import mg.itu.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/reservations")
@SessionAttributes("detailsList")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    private static final String UPLOAD_DIR = "uploads/passports/";

    @ModelAttribute("detailsList")
    public List<DetailsReservation> detailsList() {
        return new ArrayList<>();
    }

    @GetMapping
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "views/reservations/list";
    }

    @GetMapping("/details/{id}")
    public String showDetailsForm(@PathVariable("id") String idReservation, Model model,
                                  @ModelAttribute("detailsList") List<DetailsReservation> detailsList) {
        ReservationDTO reservation = reservationService.getReservationById(idReservation)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));
        model.addAttribute("reservation", reservation);
        model.addAttribute("detail", new DetailsReservation());
        model.addAttribute("idReservation", idReservation);
        model.addAttribute("detailsList", detailsList);
        return "views/reservations/detailsForm";
    }

    @PostMapping("/details/add")
    public String addDetail(@ModelAttribute("detail") @Valid DetailsReservation detail,
                            BindingResult result,
                            @RequestParam("idReservation") String idReservation,
                            @RequestParam("passportFile") MultipartFile passportFile,
                            @ModelAttribute("detailsList") List<DetailsReservation> detailsList,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("reservation", reservationService.getReservationById(idReservation)
                    .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée")));
            model.addAttribute("idReservation", idReservation);
            model.addAttribute("detailsList", detailsList);
            return "views/reservations/detailsForm";
        }

        String filePath = null;
        if (!passportFile.isEmpty()) {
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String originalFileName = passportFile.getOriginalFilename();
                String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                Path path = Paths.get(UPLOAD_DIR + uniqueFileName);
                Files.write(path, passportFile.getBytes());
                filePath = path.toString();
                logger.info("Fichier téléchargé : {}", filePath);
            } catch (IOException e) {
                logger.error("Erreur lors du téléchargement du fichier : {}", e.getMessage());
                model.addAttribute("error", "Erreur lors du téléchargement du fichier : " + e.getMessage());
                model.addAttribute("reservation", reservationService.getReservationById(idReservation)
                        .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée")));
                model.addAttribute("detail", new DetailsReservation());
                model.addAttribute("idReservation", idReservation);
                model.addAttribute("detailsList", detailsList);
                return "views/reservations/detailsForm";
            }
        }

        if (reservationService.canAddDetail(idReservation, detailsList.size())) {
            detail.setId_reservation(idReservation);
            detail.setPassport(filePath);
            detailsList.add(detail);
            logger.info("Détail ajouté au panier pour idReservation: {}, nom: {}, fichier: {}", idReservation, detail.getNom(), filePath);
        } else {
            model.addAttribute("error", "Nombre maximum de passagers atteint pour cette réservation.");
            model.addAttribute("reservation", reservationService.getReservationById(idReservation)
                    .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée")));
            model.addAttribute("detail", new DetailsReservation());
            model.addAttribute("idReservation", idReservation);
            model.addAttribute("detailsList", detailsList);
            return "views/reservations/detailsForm";
        }
        return "redirect:/reservations/details/" + idReservation;
    }

    @PostMapping("/details/confirm")
    public String confirmDetails(@RequestParam("idReservation") String idReservation,
                                 @ModelAttribute("detailsList") List<DetailsReservation> detailsList,
                                 Model model) {
        logger.info("Confirmation des détails pour idReservation: {}, nombre de détails: {}", idReservation, detailsList.size());
        try {
            reservationService.saveDetails(detailsList, idReservation);
            detailsList.clear();
            logger.info("Détails confirmés et panier vidé pour idReservation: {}", idReservation);
            return "redirect:/reservations";
        } catch (Exception e) {
            logger.error("Erreur lors de la confirmation des détails: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de la confirmation des détails : " + e.getMessage());
            model.addAttribute("reservation", reservationService.getReservationById(idReservation)
                    .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée")));
            model.addAttribute("detail", new DetailsReservation());
            model.addAttribute("idReservation", idReservation);
            model.addAttribute("detailsList", detailsList);
            return "views/reservations/detailsForm";
        }
    }

    @GetMapping("/view/{id}")
    public String viewDetails(@PathVariable("id") String idReservation, Model model) {
        ReservationDTO reservation = reservationService.getReservationById(idReservation)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));
        List<DetailsReservation> details = reservationService.getDetailsByReservationId(idReservation);
        model.addAttribute("reservation", reservation);
        model.addAttribute("details", details);
        return "views/reservations/viewDetails";
    }
    @GetMapping("/export/{id}")
    public ResponseEntity<Resource> exportPdf(@PathVariable("id") String idReservation) {
        try {
            byte[] pdfBytes = reservationService.exportReservationPdf(idReservation);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reservation_" + idReservation + ".pdf")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Erreur lors de l'export PDF: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}