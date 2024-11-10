package store;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Promotion {

    private PromotionType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private int triggerQuantity;
    private int bonusQuantity;

    public Promotion(PromotionType type, int triggerQuantity, int bonusQuantity, LocalDate startDate,
                     LocalDate endDate) {
        this.type = type;
        this.triggerQuantity = triggerQuantity;
        this.bonusQuantity = bonusQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PromotionType getType() {
        return this.type;
    }

    public int getTriggerQuantity() {
        return this.triggerQuantity;
    }

    public int getBonusQuantity() {
        return this.bonusQuantity;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }
    


}
