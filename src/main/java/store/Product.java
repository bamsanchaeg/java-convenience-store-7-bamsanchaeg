package store;

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


}
