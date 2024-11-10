package store;

public enum PromotionType {

    NONE(""),
    MD_RECOMMENDATION("MD추천상품"),
    BUY_2_GET_1("탄산2+1"),
    BUY_1_GET_1("1_1"),
    FLASH_SALE("반짝할인");

    private final String displayName;

    PromotionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
