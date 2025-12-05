package com.e_commerce.controller.retaurant;

import com.e_commerce.dto.ApiResponse;
import com.e_commerce.entity.Restaurant;
import com.e_commerce.service.retaurant.RestaurantService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.create(restaurant);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Restaurant created successfully", createdRestaurant, null,
                        "/restaurants"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllRestaurants() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Get all restaurants successfully", restaurantService.getAll(), null,
                        "/restaurants"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getRestaurantById(@PathVariable Integer id) {
        Restaurant restaurant = restaurantService.getById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Get restaurant successfully", restaurant, null, "/restaurants/" + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRestaurant(@PathVariable Integer id) {
        restaurantService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Restaurant deleted successfully", null, null, "/restaurants/" + id));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<?> getOrders(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantService.getOrders(id));
    }

    @GetMapping("/{id}/revenue")
    public ResponseEntity<BigDecimal> getRevenue(
            @PathVariable Integer id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        LocalDateTime start = parseStartDateTime(from);
        LocalDateTime end = parseEndDateTime(to);
        BigDecimal total = restaurantService.getTotalRevenue(id, start, end);
        return ResponseEntity.ok(total);
    }

    private LocalDateTime parseStartDateTime(String input) {
        if (input == null || input.isBlank()) return null;
        try {
            if (input.matches("^\\d{4}$")) { // year
                LocalDate d = LocalDate.of(Integer.parseInt(input), 1, 1);
                return d.atStartOfDay();
            }
            if (input.matches("^\\d{4}-\\d{2}$")) { // yyyy-MM
                YearMonth ym = YearMonth.parse(input, DateTimeFormatter.ofPattern("yyyy-MM"));
                return ym.atDay(1).atStartOfDay();
            }
            // assume yyyy-MM-dd
            LocalDate ld = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
            return ld.atStartOfDay();
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private LocalDateTime parseEndDateTime(String input) {
        if (input == null || input.isBlank()) return null;
        try {
            if (input.matches("^\\d{4}$")) { // year
                LocalDate d = LocalDate.of(Integer.parseInt(input), 12, 31);
                return d.atTime(23,59,59);
            }
            if (input.matches("^\\d{4}-\\d{2}$")) { // yyyy-MM
                YearMonth ym = YearMonth.parse(input, DateTimeFormatter.ofPattern("yyyy-MM"));
                LocalDate last = ym.atEndOfMonth();
                return last.atTime(23,59,59);
            }
            // assume yyyy-MM-dd
            LocalDate ld = LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
            return ld.atTime(23,59,59);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
