package ge.mg.tasktrackerapi.persistence.repository;

import ge.mg.tasktrackerapi.persistence.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByOwner_Id(Long ownerId, Pageable pageable);
}
