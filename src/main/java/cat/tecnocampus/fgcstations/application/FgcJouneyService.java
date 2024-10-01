package cat.tecnocampus.fgcstations.application;

import cat.tecnocampus.fgcstations.application.DTOs.JourneyDTO;
import cat.tecnocampus.fgcstations.application.exception.JourneyDoesNotExistsException;
import cat.tecnocampus.fgcstations.domain.Journey;
import cat.tecnocampus.fgcstations.domain.JourneyId;
import cat.tecnocampus.fgcstations.persistence.JourneyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FgcJouneyService {
    private final JourneyRepository journeyRepository;

    public FgcJouneyService(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    public List<Journey> getAllJourneysDomain() {
        //TODO 6: get all stations (see you return a domain Journey). Actually, you don't need to leave this file
        // in order to complete this exercise
        return journeyRepository.findAll();
    }

    public List<JourneyDTO> getAllJourneysDTO() {
        //TODO 7: get all journeys (see the returned type)
        return journeyRepository.findAllProjectedBy();
    }

    public Journey getJourneyDomain(String origin, String destination) {
        // TODO 8: get a journey by origin and destination (domain). If the journey does not exist, throw a JourneyDoesNotExistsException
        //  try no to use any sql (jpql) query, just come up with an appropriate method name
        return journeyRepository.findByOriginAndDestination(origin,destination).orElseThrow(() -> new JourneyDoesNotExistsException(origin,destination));
    }

    public JourneyId getJourneyId(String origin, String destination) {
        // TODO 9: get a journey ID by origin and destination (domain JourneyId). If the journey does not exist, throw a JourneyDoesNotExistsException
        //  try no to use any sql (jpql) query, just come up with an appropriate method name
        return journeyRepository.findIdByOriginAndDestination(origin,destination).orElseThrow(() -> new JourneyDoesNotExistsException(origin,destination));
    }
}
