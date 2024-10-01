package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.JourneyDTO;
import cat.tecnocampus.fgcstations.domain.Journey;
import cat.tecnocampus.fgcstations.domain.JourneyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JourneyRepository extends JpaRepository<Journey, JourneyId> {
    List<JourneyDTO> findAllProjectedBy();

    @Query("SELECT j FROM Journey j WHERE j.origin.name = :origin AND j.destination.name = :destination")
    Optional<Journey> findByOriginAndDestination(@Param("origin") String origin, @Param("destination") String destination);

    @Query("SELECT j.origin.name, j.destination.name FROM Journey j WHERE j.origin.name = :origin AND j.destination.name = :destination")
    Optional<JourneyId> findIdByOriginAndDestination(@Param("origin") String origin, @Param("destination") String destination);
}
