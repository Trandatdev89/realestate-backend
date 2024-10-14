package com.project01.reactspring.respository;


import com.project01.reactspring.entity.InValidateToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InvalidateTokenRepository extends JpaRepository<InValidateToken,String> {

    @Transactional
    @Modifying
    @Query("DELETE from InValidateToken t where t.expirytime<=CURRENT_TIMESTAMP")
    void deleteExpiryTime();
}
