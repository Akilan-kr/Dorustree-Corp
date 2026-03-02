package com.dorustree.dorustree_corp.Controller;


import com.dorustree.dorustree_corp.Dto.ApiResponse;
import com.dorustree.dorustree_corp.Dto.DashboardDTO;
import com.dorustree.dorustree_corp.Service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@Slf4j
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Stats data for admin dashboard - ADMIN", description = "return the stat data of the project")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardDTO>> getDashboardStats() {
        log.info("C: Admin dashboard is called by admin");
        DashboardDTO stats = dashboardService.getDashboardStats();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Stat data for Dashboard", stats));
    }
}
