package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.StationDTO;
import cat.tecnocampus.fgcstations.application.DTOs.StationTopFavoriteJourney;
import cat.tecnocampus.fgcstations.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, String> {
    
    List<StationDTO> findAllProjectedBy();

   Station findStationByName(String name);

   StationDTO findProjectedByName(String name);
}
