package itu.auth.mg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import itu.auth.mg.model.Setting;
import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    @Query("SELECT e FROM Setting e WHERE e.daty = (SELECT MAX(e2.daty) FROM Setting e2)")
    Setting findByMaxDate();
}


