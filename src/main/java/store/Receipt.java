package store;

import java.util.Map;

public class Receipt {

    private double totalCost;
    private double promoDiscount;
    private double regularStockCost;
    private Map<String, Integer> purchaseDetails;
    private Map<String, Integer> bonusDetails;

    public Receipt(Map<String, Integer> purchaseDetails, Map<String, Integer> bonusDetails) {
        this.purchaseDetails = purchaseDetails;
        this.bonusDetails = bonusDetails;
        this.totalCost = 0;
        this.promoDiscount = 0;
        this.regularStockCost = 0;
    }

    public void calculateCosts(Inventory inventory) {
        for (Map.Entry<String, Integer> entry : purchaseDetails.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            Product product = inventory.findProductByName(productName);
            double itemCost = product.getPrice() * quantity;
            totalCost += itemCost;

            if (bonusDetails.containsKey(productName)) {
                int promoQuantity = bonusDetails.get(productName);
                promoDiscount += product.getPrice() * promoQuantity;
            } else {
                regularStockCost += itemCost;
            }
        }
    }

    public double applyMembershipDiscount(MembershipDiscount membershipDiscount, boolean hasMembershipDiscount) {
        return hasMembershipDiscount ? Math.min(membershipDiscount.applyDiscount(regularStockCost), 8000) : 0;
    }


    public void displayReceipt(double membershipDiscountAmount) {
        System.out.println("===========W 편의점=============");
        System.out.println("상품명\t\t수량\t금액");

        purchaseDetails.forEach((productName, quantity) ->
                System.out.printf("%s\t\t%d\t%,.0f\n", productName, quantity, totalCost)
        );

        System.out.println("=============증\t정===============");
        bonusDetails.forEach((productName, quantity) ->
                System.out.printf("%s\t\t%d\t증정\n", productName, quantity)
        );

        System.out.println("====================================");
        System.out.printf("총구매액\t\t%,.0f\n", totalCost);
        System.out.printf("행사할인\t\t\t-%,.0f\n", promoDiscount);
        System.out.printf("멤버십할인\t\t-%,.0f\n", membershipDiscountAmount);
        System.out.printf("내실돈\t\t\t%,.0f\n", totalCost - membershipDiscountAmount);
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요?");
    }

}
