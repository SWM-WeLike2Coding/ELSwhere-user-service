package com.wl2c.elswhereuserservice.domain.user.model.entity;

import com.wl2c.elswhereuserservice.domain.user.model.AlarmStatus;
import com.wl2c.elswhereuserservice.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "interest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Long productId;

    @Enumerated(STRING)
    private AlarmStatus subscriptionEndDateAlarm;

    @Builder
    public Interest(User user,
                    @NonNull Long productId) {
        this.user = user;
        this.productId = productId;
        this.subscriptionEndDateAlarm = AlarmStatus.ACTIVE;
    }

}
