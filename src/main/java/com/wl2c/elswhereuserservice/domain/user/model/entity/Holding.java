package com.wl2c.elswhereuserservice.domain.user.model.entity;

import com.wl2c.elswhereuserservice.domain.user.model.AlarmStatus;
import com.wl2c.elswhereuserservice.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Holding extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "holdings_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Long productId;

    @NotNull
    @Column(precision = 25, scale = 5)
    private BigDecimal price;

    @Enumerated(STRING)
    private AlarmStatus redemptionAlarm;

    @Enumerated(STRING)
    private AlarmStatus maturityAlarm;

    @Builder
    private Holding(User user,
                     @NonNull Long productId,
                     @NonNull BigDecimal price) {
        this.user = user;
        this.productId = productId;
        this.price = price;
        this.redemptionAlarm = AlarmStatus.ACTIVE;
        this.maturityAlarm = AlarmStatus.ACTIVE;
    }

}
