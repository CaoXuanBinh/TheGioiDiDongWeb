package com.hutech.CAOXUANBINH.repository;

import com.hutech.CAOXUANBINH.model.PointVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PointVoucherRepository extends JpaRepository<PointVoucher, Long> {
    Optional<PointVoucher> findByCode(String code);

    @Query("SELECT COALESCE(SUM(v.redeemedPoints), 0) FROM PointVoucher v WHERE v.phoneNumber = :phoneNumber")
    int sumRedeemedPointsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT COALESCE(SUM(v.redeemedPoints), 0) FROM PointVoucher v WHERE v.username = :username")
    int sumRedeemedPointsByUsername(@Param("username") String username);

    List<PointVoucher> findByPhoneNumberOrderByIdDesc(String phoneNumber);

    List<PointVoucher> findByUsernameOrderByIdDesc(String username);
}
