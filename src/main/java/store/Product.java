package store;

import java.util.List;

public class Product {

    private String name;
    private double price;
    private int regularStock; // 일반 재고
    private int promotionStock; // 프로모션 재고
    private Promotion promotion;

    public Product(String name, double price, int regularStock, int promotionStock, PromotionType promotionType) {
        this.name = name;
        this.price = price;
        this.regularStock = regularStock;
        this.promotionStock = promotionStock;
        this.promotion = Promotion.getPromotionByType(promotionType);
    }

    // 상품 이름 getter
    public String getName() {
        return name;
    }

    // 가격 getter
    public double getPrice() {
        return price;
    }

    // 프로모션 getter
    public Promotion getPromotion() {
        return promotion;
    }


    // 재고 수량 getter
    public int getRegularStock() {
        return regularStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }


    public boolean hasPromotion() {
        return promotion != null && promotion.getType() != PromotionType.NONE;
    }

    public int reducePromotionStock(int quantity) {
        int reduction = Math.min(promotionStock, quantity);  // 차감할 수 있는 최대 수량 계산
        promotionStock -= reduction;
        return reduction;
    }


    public List<Integer> reduceStock(int requestedQuantity) {
        int remainingQuantity = requestedQuantity;
        int bonusQuantity = 0;
        if (isPromotionActive()) {
            bonusQuantity = applyPromotionDiscount(remainingQuantity);
            remainingQuantity -= calculatePromotionReduction(remainingQuantity, bonusQuantity);
        }
        remainingQuantity = reduceRegularStock(remainingQuantity);

        int purchasedQuantity = requestedQuantity - remainingQuantity;
        return List.of(purchasedQuantity, bonusQuantity);
    }


    private int applyPromotionDiscount(int remainingQuantity) {
        return promotion.calculateBonusQuantity(remainingQuantity);
    }

    //프로모션에서 차감할 수량을 계산하고 프로모션 재고에서 차감
    private int calculatePromotionReduction(int remainingQuantity, int bonusQuantity) {
        return promotion.calculatePromotionReduction(remainingQuantity, bonusQuantity);
    }

    private int reduceRegularStock(int remainingQuantity) {
        int reduction = Math.min(regularStock, remainingQuantity);
        regularStock -= reduction;
        return remainingQuantity - reduction;
    }

    private boolean isPromotionActive() {
        return promotion != null && promotion.isActive() && promotionStock > 0;
    }


}
