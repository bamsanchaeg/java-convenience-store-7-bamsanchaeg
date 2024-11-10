package store;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private final String PRODUCT_DELIMITER = ",";
    private final int NUMBER_OF_CATEGORY = 4;

    private static List<Product> products = new ArrayList<>();
    PromotionLoader applicationCommon = new PromotionLoader();

    public Inventory(List<String> fileProducts, List<String> filePromotions) {
        applicationCommon.initializePromotions(filePromotions);
        createProducts(fileProducts);
    }

    public void createProducts(List<String> lines) {
        lines.forEach(this::processLine);
    }

    public void processLine(String line) {
        String[] parts = parseLine(line);
        if (isValidaFormat(parts)) {
            try {
                String name = parts[0].trim();
                double price = parsePrice(parts[1]);    // 가격 파싱
                int quantity = parseQuantity(parts[2]);     // 수량 파싱
                PromotionType promotionType = getPromotionType(parts[3]);
                addProduct(name, price, quantity, promotionType);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }

    }

    // 상품명을 입력받아 해당 상품을 반환하는 메서드
    public static Product findProductByName(String productName) {
        Product product = products.stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .orElse(null);

        if (product == null) {
            System.out.println("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
        return product;
    }

    public static List<Product> getProducts() {
        return products;
    }

    private boolean isValidaFormat(String[] parts) {
        return parts.length >= NUMBER_OF_CATEGORY;
    }

    private PromotionType getPromotionType(String promotionTypeStr) {
        String validPromotionTypeStr = getValidPromotionTypeStr(promotionTypeStr);
        return determinePromotionType(validPromotionTypeStr);
    }

    private void addProduct(String name, double price, int quantity, PromotionType promotionType) {
        int regularStock = calculateRegularStock(quantity, promotionType);
        int promotionStock = calculatePromotionStock(quantity, promotionType);
        products.add(new Product(name, price, regularStock, promotionStock, promotionType));
    }

    private String[] parseLine(String line) {
        String[] parts = line.split(PRODUCT_DELIMITER);
        if (parts.length < NUMBER_OF_CATEGORY) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 입력입니다.");
        }
        return parts;
    }

    private double parsePrice(String priceStr) {
        return Double.parseDouble(priceStr.trim());
    }

    private int parseQuantity(String quantityStr) {
        return Integer.parseInt(quantityStr.trim());
    }

    private String getValidPromotionTypeStr(String promotionTypeStr) {
        return "null".equals(promotionTypeStr.trim()) ? null : promotionTypeStr.trim();
    }

    private PromotionType determinePromotionType(String promotionTypeStr) {
        if (promotionTypeStr == null) {
            return PromotionType.NONE;
        }

        PromotionType promotionType = parsePromotionType(promotionTypeStr);
        if (promotionType == PromotionType.NONE) {
            return PromotionType.NONE;
        }
        return findValidPromotionType(promotionType);
    }

    private PromotionType parsePromotionType(String promotionTypeStr) {
        PromotionType promotionType = PromotionType.fromString(promotionTypeStr);
        return (promotionType != null) ? promotionType : PromotionType.NONE;
    }

    private PromotionType findValidPromotionType(PromotionType promotionType) {
        return PromotionLoader.getDefaultPromotions().stream()
                .filter(promotion -> promotion.getType() == promotionType)
                .findFirst()
                .map(Promotion::getType)
                .orElse(PromotionType.NONE);
    }


    private int calculateRegularStock(int quantity, PromotionType promotionType) {
        if (promotionType == PromotionType.NONE) {
            return quantity;
        }
        return 0;
    }

    private int calculatePromotionStock(int quantity, PromotionType promotionType) {
        if (promotionType != PromotionType.NONE) {
            return quantity;
        }
        return 0;
    }



}
