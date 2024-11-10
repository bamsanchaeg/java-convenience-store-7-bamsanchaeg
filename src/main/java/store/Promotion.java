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

    public static Promotion getPromotionByType(PromotionType type) {
        return PromotionLoader.getDefaultPromotions().stream()
                .filter(promotion -> promotion.getType() == type)
                .findFirst()
                .orElse(null);
    }

    //해결하지 못함
    public boolean isActive() {
        LocalDateTime datetime = DateTimes.now();
        LocalDate today = LocalDate.now();
        return (today.isEqual(startDate) || today.isAfter(startDate)) &&
                (today.isEqual(endDate) || today.isBefore(endDate));
    }

    public int calculateBonusQuantity(int remainingQuantity) {
        return type.calculateBonusQuantity(remainingQuantity);
    }

    public int calculatePromotionReduction(int remainingQuantity, int bonusQuantity) {
        return type.calculatePromotionReduction(remainingQuantity, bonusQuantity);
    }



}
