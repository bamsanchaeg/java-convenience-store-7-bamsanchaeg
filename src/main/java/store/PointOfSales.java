package store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.view.InputView;

public class PointOfSales {

    public Map<String, Map<String, Integer>> processPurchase(Map<String, Integer> purchaseRequest,
                                                             InputView inputView) {
        Map<String, Integer> purchaseDetails = new HashMap<>();
        Map<String, Integer> bonusDetails = new HashMap<>();
        for (Map.Entry<String, Integer> entry : purchaseRequest.entrySet()) {
            processProduct(entry, purchaseDetails, bonusDetails, inputView);
        }

        return createResultMap(purchaseDetails, bonusDetails);
    }

    private List<Integer> processProductPurchase(Product product, int requestedQuantity, InputView inputView) {
        int remainingQuantity = validateAndAdjustStock(product, requestedQuantity);
        remainingQuantity = handlePromotion(product, remainingQuantity, inputView);
        return finalizePurchase(product, remainingQuantity);
    }

    //재고초과 확인 및 조정
    private int validateAndAdjustStock(Product product, int requestedQuantity) {
        int totalAvailableStock = product.getRegularStock() + product.getPromotionStock();
        validateOverStock(requestedQuantity, totalAvailableStock);
        return requestedQuantity;
    }

    private List<Integer> finalizePurchase(Product product, int remainingQuantity) {
        return product.reduceStock(remainingQuantity);
    }

    private int handlePromotion(Product product, int remainingQuantity, InputView inputView) {
        if (!isValidatePromotion(product)) {
            return remainingQuantity;
        }

        int triggeredQuantity = product.getPromotion().getTriggerQuantity();
        int additionalRequired = calculateAdditionalRequired(remainingQuantity, triggeredQuantity);

        if (additionalRequired > 0 && confirmAdditionalPurchase(product, additionalRequired, inputView)) {
            remainingQuantity += additionalRequired;
        }

        return remainingQuantity;
    }

    //추가 수량이 필요한지 계산하는 메서드
    private int calculateAdditionalRequired(int remainingQuantity, int triggerQuantity) {
        return remainingQuantity < triggerQuantity ? triggerQuantity - remainingQuantity : 0;
    }

    private boolean confirmAdditionalPurchase(Product product, int additionalRequired, InputView inputView) {
        System.out.printf("현재 %s은(는) %d개를 더 가져오면 1개를 무료로 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n",
                product.getName(), additionalRequired);
        return InputView.confirmUserInput();
    }

    private void validateOverStock(int remainQuantity, int totalAvailableStock) {
        if (remainQuantity > totalAvailableStock) {
            System.out.println("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private boolean isValidatePromotion(Product product) {
        return product.hasPromotion() && product.getPromotion().isActive();
    }

    private void processProduct(Map.Entry<String, Integer> entry, Map<String, Integer> purchaseDetails,
                                Map<String, Integer> bonusDetails, InputView inputView) {
        String productName = entry.getKey();
        int requestQuantity = entry.getValue();
        Product product = Inventory.findProductByName(productName);
        if (product == null) {
            return;
        }

        List<Integer> purchaseAndBonus = getPurchaseAndBonusDetails(product, requestQuantity, inputView);
        updatePurchaseAndBonusRecords(productName, purchaseAndBonus, purchaseDetails, bonusDetails);
    }

    private List<Integer> getPurchaseAndBonusDetails(Product product, int requestQuantity, InputView inputView) {
        return processProductPurchase(product, requestQuantity, inputView);
    }

    private void updatePurchaseAndBonusRecords(String productName, List<Integer> purchaseAndBonus,
                                               Map<String, Integer> purchaseDetails,
                                               Map<String, Integer> bonusDetails) {
        purchaseDetails.put(productName, purchaseAndBonus.getFirst());
        if (purchaseAndBonus.get(1) > 0) {
            bonusDetails.put(productName, purchaseAndBonus.get(1));
        }
    }

    private Map<String, Map<String, Integer>> createResultMap(Map<String, Integer> purchaseDetails,
                                                              Map<String, Integer> bonusDetails) {
        Map<String, Map<String, Integer>> result = new HashMap<>();
        result.put("purchaseDetails", purchaseDetails);
        result.put("bonusDetails", bonusDetails);
        return result;
    }
}
