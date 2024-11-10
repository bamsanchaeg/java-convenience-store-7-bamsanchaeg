package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import store.view.input.ConsoleReader;
import store.view.input.Reader;

public class InputView {

    private static final Reader DEFAULT_READER = new ConsoleReader();

    private final Reader reader;

    public InputView(Reader reader) {
        this.reader = reader;
    }

    public Map<String, Integer> purchaseProduct() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return parsePurchaseRequest(Console.readLine());
    }


    // 사용자 입력 확인 메서드
    public static boolean confirmUserInput() {
        String response = Console.readLine().trim().toUpperCase();
        return "Y".equals(response);
    }

    private Map<String, Integer> parsePurchaseRequest(String purchaseRequest) {
        return Arrays.stream(purchaseRequest.replaceAll("[\\[\\]]", "").split(","))
                .map(item -> item.split("-"))
                .collect(Collectors.toMap(
                        parts -> parts[0],
                        parts -> Integer.parseInt(parts[1])
                ));
    }


}
