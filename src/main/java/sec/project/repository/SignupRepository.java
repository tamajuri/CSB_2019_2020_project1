package sec.project.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sec.project.domain.Signup;

public interface SignupRepository extends JpaRepository<Signup, Long> {

    @Query(
            value = "SELECT * FROM SIGNUPS u WHERE u.name = 'Kalle'",
            nativeQuery = true)
    Collection<Signup> findAllSignupsNative();
    
}
