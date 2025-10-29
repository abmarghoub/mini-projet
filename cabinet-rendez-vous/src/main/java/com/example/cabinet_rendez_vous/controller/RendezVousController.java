package com.example.cabinet_rendez_vous.controller;

import com.example.cabinet_rendez_vous.entities.*;
import com.example.cabinet_rendez_vous.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class RendezVousController {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    // Helper method to format date for URL - accessible from views
    public static String formatDateForUrl(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return sdf.format(date);
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false) String statut,
                        @RequestParam(required = false) Long medecinId,
                        @RequestParam(required = false) String specialite,
                        @RequestParam(required = false) String dateFiltre) {

        // Charger toutes les données
        List<RendezVous> allRV = new ArrayList<>();
        rendezVousRepository.findAll().forEach(allRV::add);

        // Filtrer les rendez-vous
        List<RendezVous> filteredRV = new ArrayList<>();
        for (RendezVous rv : allRV) {
            boolean match = true;
            if (statut != null && !statut.isEmpty() && !rv.getStatut().equals(statut)) {
                match = false;
            }
            if (medecinId != null && rv.getMedecin().getId() != medecinId) {
                match = false;
            }
            if (specialite != null && !specialite.isEmpty() && !rv.getMedecin().getSpecialite().equals(specialite)) {
                match = false;
            }
            if (dateFiltre != null && !dateFiltre.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (!sdf.format(rv.getRv_pk().getDateHeure()).equals(dateFiltre)) {
                    match = false;
                }
            }
            if (match) {
                filteredRV.add(rv);
            }
        }

        model.addAttribute("rendezVous", filteredRV);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("medecins", medecinRepository.findAll());

        // Statistiques globales
        long totalRV = allRV.size();
        long confirmedRV = 0;
        long cancelledRV = 0;

        for (RendezVous rv : allRV) {
            if ("Confirmé".equals(rv.getStatut())) {
                confirmedRV++;
            } else if ("Annulé".equals(rv.getStatut())) {
                cancelledRV++;
            }
        }

        model.addAttribute("statTotal", totalRV);
        model.addAttribute("statConfirmed", confirmedRV);
        model.addAttribute("statCancelled", cancelledRV);

        // Statistiques par spécialité
        Map<String, Long> rvBySpecialite = new HashMap<>();
        for (RendezVous rv : allRV) {
            String spec = rv.getMedecin().getSpecialite();
            rvBySpecialite.put(spec, rvBySpecialite.getOrDefault(spec, 0L) + 1);
        }
        model.addAttribute("rvBySpecialite", rvBySpecialite);

        // Convert to Chart.js format for JSON
        List<String> specialiteLabels = new ArrayList<>(rvBySpecialite.keySet());
        List<Long> specialiteValues = new ArrayList<>();
        for (String label : specialiteLabels) {
            specialiteValues.add(rvBySpecialite.get(label));
        }

        Map<String, Object> specialiteChartData = new HashMap<>();
        specialiteChartData.put("labels", specialiteLabels);
        Map<String, Object> specialiteDataset = new HashMap<>();
        specialiteDataset.put("label", "Nombre de RDV");
        specialiteDataset.put("data", specialiteValues);
        specialiteDataset.put("backgroundColor", "rgba(54, 162, 235, 0.5)");
        specialiteDataset.put("borderColor", "rgba(54, 162, 235, 1)");
        specialiteDataset.put("borderWidth", 1);
        specialiteChartData.put("datasets", Collections.singletonList(specialiteDataset));
        ObjectMapper mapper = new ObjectMapper();
        try {
            model.addAttribute("specialiteDataJSON", mapper.writeValueAsString(specialiteChartData));
        } catch (Exception e) {
            model.addAttribute("specialiteDataJSON", "{}");
        }

        // Statistiques par mois
        Map<String, Long> rvByMonth = new LinkedHashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        for (RendezVous rv : allRV) {
            String month = monthFormat.format(rv.getRv_pk().getDateHeure());
            rvByMonth.put(month, rvByMonth.getOrDefault(month, 0L) + 1);
        }
        model.addAttribute("rvByMonth", rvByMonth);

        // Convert to Chart.js format for JSON
        List<String> monthLabels = new ArrayList<>(rvByMonth.keySet());
        List<Long> monthValues = new ArrayList<>();
        for (String label : monthLabels) {
            monthValues.add(rvByMonth.get(label));
        }

        Map<String, Object> monthChartData = new HashMap<>();
        monthChartData.put("labels", monthLabels);
        Map<String, Object> monthDataset = new HashMap<>();
        monthDataset.put("label", "Nombre de RDV");
        monthDataset.put("data", monthValues);
        monthDataset.put("backgroundColor", "rgba(75, 192, 192, 0.5)");
        monthDataset.put("borderColor", "rgba(75, 192, 192, 1)");
        monthDataset.put("borderWidth", 1);
        monthChartData.put("datasets", Collections.singletonList(monthDataset));
        try {
            model.addAttribute("monthDataJSON", mapper.writeValueAsString(monthChartData));
        } catch (Exception e) {
            model.addAttribute("monthDataJSON", "{}");
        }

        // Statistiques par jour (patients par jour)
        Map<String, Long> patientsByDay = new LinkedHashMap<>();
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (RendezVous rv : allRV) {
            String day = dayFormat.format(rv.getRv_pk().getDateHeure());
            patientsByDay.put(day, patientsByDay.getOrDefault(day, 0L) + 1);
        }
        model.addAttribute("patientsByDay", patientsByDay);

        // Convert to Chart.js format for JSON
        List<String> dayLabels = new ArrayList<>(patientsByDay.keySet());
        List<Long> dayValues = new ArrayList<>();
        for (String label : dayLabels) {
            dayValues.add(patientsByDay.get(label));
        }

        Map<String, Object> dayChartData = new HashMap<>();
        dayChartData.put("labels", dayLabels);
        Map<String, Object> dayDataset = new HashMap<>();
        dayDataset.put("label", "Nombre de Patients");
        dayDataset.put("data", dayValues);
        dayDataset.put("backgroundColor", "rgba(153, 102, 255, 0.5)");
        dayDataset.put("borderColor", "rgba(153, 102, 255, 1)");
        dayDataset.put("borderWidth", 1);
        dayChartData.put("datasets", Collections.singletonList(dayDataset));
        try {
            model.addAttribute("dayDataJSON", mapper.writeValueAsString(dayChartData));
        } catch (Exception e) {
            model.addAttribute("dayDataJSON", "{}");
        }

        // Taux de no-show (annulés)
        double noShowRate = totalRV > 0 ? (double) cancelledRV / totalRV * 100 : 0;
        model.addAttribute("noShowRate", String.format("%.2f", noShowRate));

        // Spécialités distinctes pour le filtre
        List<String> specialites = new ArrayList<>();
        for (Medecin medecin : medecinRepository.findAll()) {
            if (!specialites.contains(medecin.getSpecialite())) {
                specialites.add(medecin.getSpecialite());
            }
        }
        Collections.sort(specialites);
        model.addAttribute("specialites", specialites);

        return "index";
    }

    // ==========================================
    // GESTION DES PATIENTS
    // ==========================================

    @PostMapping("/patients/add")
    public String addPatient(@RequestParam String nom,
                             @RequestParam String cin,
                             @RequestParam String dateNaissance,
                             @RequestParam(required = false, defaultValue = "false") boolean assurance,
                             @RequestParam String email,
                             RedirectAttributes redirectAttributes) {
        try {
            if (patientRepository.findByEmail(email).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Cet email est déjà utilisé");
                return "redirect:/?tab=patients";
            }
            Patient patient = new Patient(nom, cin, java.time.LocalDate.parse(dateNaissance),
                    assurance, email, "default123");
            patientRepository.save(patient);
            redirectAttributes.addFlashAttribute("success", "Patient ajouté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/?tab=patients";
    }

    @PostMapping("/patients/update")
    public String updatePatient(@RequestParam Long patientId,
                                @RequestParam String nom,
                                @RequestParam String cin,
                                @RequestParam String dateNaissance,
                                @RequestParam(required = false, defaultValue = "false") boolean assurance,
                                @RequestParam String email,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<Patient> optPatient = patientRepository.findById(patientId);
            if (optPatient.isPresent()) {
                Patient patient = optPatient.get();
                patient.setNom(nom);
                patient.setCin(cin);
                patient.setDateNaissance(java.time.LocalDate.parse(dateNaissance));
                patient.setAssurance(assurance);
                patient.setEmail(email);
                patientRepository.save(patient);
                redirectAttributes.addFlashAttribute("success", "Patient modifié avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/?tab=patients";
    }

    // ==========================================
    // GESTION DES MÉDECINS
    // ==========================================

    @PostMapping("/medecins/add")
    public String addMedecin(@RequestParam String nom,
                             @RequestParam String specialite,
                             @RequestParam String email,
                             RedirectAttributes redirectAttributes) {
        try {
            if (medecinRepository.findByEmail(email).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Cet email est déjà utilisé");
                return "redirect:/?tab=medecins";
            }
            Medecin medecin = new Medecin(nom, specialite, email, "default123");
            medecinRepository.save(medecin);
            redirectAttributes.addFlashAttribute("success", "Médecin ajouté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/?tab=medecins";
    }

    @PostMapping("/medecins/update")
    public String updateMedecin(@RequestParam Long medecinId,
                                @RequestParam String nom,
                                @RequestParam String specialite,
                                @RequestParam String email,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<Medecin> optMedecin = medecinRepository.findById(medecinId);
            if (optMedecin.isPresent()) {
                Medecin medecin = optMedecin.get();
                medecin.setNom(nom);
                medecin.setSpecialite(specialite);
                medecin.setEmail(email);
                medecinRepository.save(medecin);
                redirectAttributes.addFlashAttribute("success", "Médecin modifié avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/?tab=medecins";
    }

    // ==========================================
    // GESTION DES RENDEZ-VOUS
    // ==========================================

    @PostMapping("/rendez-vous/add")
    public String addRendezVous(@RequestParam String nom,
                                @RequestParam String statut,
                                @RequestParam String motif,
                                @RequestParam Long patientId,
                                @RequestParam Long medecinId,
                                @RequestParam String dateHeure,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<Patient> patient = patientRepository.findById(patientId);
            Optional<Medecin> medecin = medecinRepository.findById(medecinId);

            if (patient.isPresent() && medecin.isPresent()) {
                String dateTimeStr = dateHeure.replace("T", " ");
                if (dateTimeStr.split(":").length == 2) {
                    dateTimeStr += ":00";
                }
                Timestamp timestamp = Timestamp.valueOf(dateTimeStr);
                Date date = new Date(timestamp.getTime());

                RV_pk rv_pk = new RV_pk(patientId, medecinId, date);
                RendezVous rv = new RendezVous(nom, statut, motif, patient.get(), medecin.get(), date);
                rv.setRv_pk(rv_pk);
                rendezVousRepository.save(rv);
                redirectAttributes.addFlashAttribute("success", "Rendez-vous créé avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Patient ou médecin introuvable");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création: " + e.getMessage());
        }
        return "redirect:/?tab=rendez-vous";
    }
    @PostMapping("/rendez-vous/update")
    public String updateRendezVous(
            @RequestParam Long oldPatientId,
            @RequestParam Long oldMedecinId,
            @RequestParam String oldDateHeure,
            @RequestParam String newDateHeure,
            @RequestParam String statut,
            @RequestParam String nom,
            @RequestParam String motif,
            RedirectAttributes redirectAttributes
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date oldDate = sdf.parse(oldDateHeure);
            Date newDate = sdf.parse(newDateHeure);

            RV_pk oldPk = new RV_pk(oldPatientId, oldMedecinId, oldDate);
            Optional<RendezVous> optRV = rendezVousRepository.findById(oldPk);

            if (optRV.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Rendez-vous introuvable");
                return "redirect:/?tab=rendez-vous";
            }

            RendezVous rv = optRV.get();
            rv.setNom(nom);
            rv.setMotif(motif);
            rv.setStatut(statut);

            if (!oldDate.equals(newDate)) {
                rendezVousRepository.delete(rv);
                rv.setRv_pk(new RV_pk(oldPatientId, oldMedecinId, newDate));
            }

            rendezVousRepository.save(rv);
            redirectAttributes.addFlashAttribute("success", "Rendez-vous mis à jour avec succès");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/?tab=rendez-vous";
    }




    @GetMapping("/rendez-vous/edit")
    public String editRendezVousPage(@RequestParam Long patientId,
                                     @RequestParam Long medecinId,
                                     @RequestParam String dateHeure,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Convertir le paramètre dateHeure en Date
            String dateTimeStr = dateHeure.replace("T", " ");
            if (dateTimeStr.split(":").length == 2) {
                dateTimeStr += ":00"; // Ajouter les secondes si manquantes
            }

            Timestamp timestamp = Timestamp.valueOf(dateTimeStr);
            Date date = new Date(timestamp.getTime());

            // Chercher le rendez-vous par clé composite
            Optional<RendezVous> optRV = rendezVousRepository.findByCompositeKey(
                    patientId,
                    medecinId,
                    date
            );

            if (optRV.isPresent()) {
                RendezVous rv = optRV.get();
                model.addAttribute("rv", rv);
                model.addAttribute("patients", patientRepository.findAll());
                model.addAttribute("medecins", medecinRepository.findAll());
                model.addAttribute("dateHeureParam", dateTimeStr.replace(" ", "T"));
                return "modif-rdv";
            } else {
                redirectAttributes.addFlashAttribute("error", "Rendez-vous introuvable");
                return "redirect:/?tab=rendez-vous";
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
            return "redirect:/?tab=rendez-vous";
        }
    }


    @GetMapping("/rendez-vous/delete")
    public String deleteRendezVous(@RequestParam String patientId,
                                   @RequestParam String medecinId,
                                   @RequestParam String dateHeure,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Accepter plusieurs formats de date
            String dateTimeStr = dateHeure.replace("%20", " ").replace("T", " ");

            // Vérifier si le format contient des secondes (HH:mm:ss ou seulement HH:mm)
            // Format attendu par Timestamp: "yyyy-MM-dd HH:mm:ss"
            String[] timeParts = dateTimeStr.split(" ");
            if (timeParts.length >= 2) {
                String timePart = timeParts[1];
                // Si le format est HH:mm (2 chiffres après le dernier :), ajouter :00
                if (timePart.matches("\\d{2}:\\d{2}$")) {
                    dateTimeStr += ":00";
                }
            }

            Timestamp timestamp = Timestamp.valueOf(dateTimeStr);
            Date date = new Date(timestamp.getTime());
            RV_pk rv_pk = new RV_pk(Long.parseLong(patientId), Long.parseLong(medecinId), date);

            Iterable<RendezVous> allRV = rendezVousRepository.findAll();
            for (RendezVous rv : allRV) {
                if (rv.getRv_pk().equals(rv_pk)) {
                    rendezVousRepository.delete(rv);
                    redirectAttributes.addFlashAttribute("success", "Rendez-vous supprimé avec succès");
                    return "redirect:/?tab=rendez-vous";
                }
            }
            redirectAttributes.addFlashAttribute("error", "Rendez-vous introuvable pour les paramètres fournis");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/?tab=rendez-vous";
    }

    @GetMapping("/patients/edit")
    public String editPatientPage(@RequestParam Long id, Model model) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "edit-patient";
        }
        return "redirect:/";
    }

    @GetMapping("/patients/delete")
    public String deletePatient(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            // Supprimer d'abord tous les rendez-vous liés pour éviter les contraintes d'intégrité
            Iterable<RendezVous> allRV = rendezVousRepository.findAll();
            for (RendezVous rv : allRV) {
                if (rv.getRv_pk().getPatientId() == id) {
                    rendezVousRepository.delete(rv);
                }
            }

            // Supprimer ensuite le patient
            patientRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Patient supprimé avec succès. Ses rendez-vous ont été annulés.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/?tab=patients";
    }

    @GetMapping("/medecins/edit")
    public String editMedecinPage(@RequestParam Long id, Model model) {
        Optional<Medecin> medecin = medecinRepository.findById(id);
        if (medecin.isPresent()) {
            model.addAttribute("medecin", medecin.get());
            return "edit-medecin";
        }
        return "redirect:/";
    }

    @GetMapping("/medecins/delete")
    public String deleteMedecin(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            // Supprimer d'abord tous les rendez-vous liés pour éviter les contraintes d'intégrité
            Iterable<RendezVous> allRV = rendezVousRepository.findAll();
            for (RendezVous rv : allRV) {
                if (rv.getRv_pk().getMedecinId() == id) {
                    rendezVousRepository.delete(rv);
                }
            }

            // Supprimer ensuite le médecin
            medecinRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Médecin supprimé avec succès. Ses rendez-vous ont été annulés.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/?tab=medecins";
    }
}

