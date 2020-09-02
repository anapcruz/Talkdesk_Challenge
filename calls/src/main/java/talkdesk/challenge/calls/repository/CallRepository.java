package talkdesk.challenge.calls.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import talkdesk.challenge.calls.model.Call;

import java.util.List;

/**
 * Call Repository
 * allow persistence into the database
 */
@RepositoryRestResource
public interface CallRepository extends JpaRepository<Call, Long> {
}
