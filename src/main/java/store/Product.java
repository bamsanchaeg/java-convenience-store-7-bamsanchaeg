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

        // 프로모션이 활성 상태일 경우에만 프로모션 적용
        if (isPromotionActive()) {
            bonusQuantity = applyPromotionDiscount(remainingQuantity);
            remainingQuantity -= calculatePromotionReduction(remainingQuantity, bonusQuantity);
        } else {
            // 프로모션이 비활성화된 경우, 동일한 이름의 일반 재고 상품으로 변경하여 결제
            Product generalProduct = findGeneralProductByName(this.name);
            if (generalProduct != null) {
                remainingQuantity = generalProduct.reduceRegularStock(remainingQuantity);
            } else {
                remainingQuantity = reduceRegularStock(remainingQuantity);
            }
        }

        int purchasedQuantity = requestedQuantity - remainingQuantity;
        return List.of(purchasedQuantity, bonusQuantity);
    }


    // 동일 이름의 일반 재고 상품을 찾는 메서드 추가
    private Product findGeneralProductByName(String name) {
        return Inventory.getProducts().stream()
                .filter(product -> product.getName().equals(name) && !product.isPromotionActive() && product.getRegularStock() > 0)
                .findFirst()
                .orElse(null);
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
        boolean active = promotion != null && promotion.isActive() && promotionStock > 0;

        if (!active) {
            // 프로모션 비활성화 시 프로모션 재고 0으로 설정
            promotionStock = 0;
        }

        return active;
    }


    @Override
    public String toString() {
        String promotionText = promotion != null ? promotion.getType().toString() : "없음";
        return String.format("%s %,.0f원 %d개 %s", name, price, regularStock, promotionStock, promotionText);
    }


}
