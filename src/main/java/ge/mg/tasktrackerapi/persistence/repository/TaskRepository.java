package ge.mg.tasktrackerapi.persistence.repository;

import ge.mg.tasktrackerapi.persistence.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            select t from Task t
            where t.assignedUser.id = :userId
            and (:status is null or t.status = :status)
            and (:priority is null or t.priority = :priority)
            """)
    Page<Task> getUserTasks(Long userId,
                            String status,
                            String priority,
                            Pageable pageable);

    @Query(value = """
            select t from Task t
            where t.project.owner.id = :ownerId
            and (:status is null or t.status = :status)
            and (:priority is null or t.priority = :priority)
            """)
    Page<Task> getManagerTasks(Long ownerId,
                               String status,
                               String priority,
                               Pageable pageable);
}
