package com.bappi.pms.repository;

import com.bappi.pms.model.entity.PatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo,Long> {

    PatientInfo findByCode(String code);

}
