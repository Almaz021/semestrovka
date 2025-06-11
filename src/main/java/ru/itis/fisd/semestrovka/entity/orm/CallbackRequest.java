package ru.itis.fisd.semestrovka.entity.orm;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "callback_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallbackRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String status = "NEW";

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;
}
