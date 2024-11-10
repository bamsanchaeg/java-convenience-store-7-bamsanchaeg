package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import store.view.input.ConsoleReader;
import store.view.input.Reader;
import store.view.output.ConsoleWriter;
import store.view.output.Writer;

public class InputView {

    private static final Reader DEFAULT_READER = new ConsoleReader();
    private static final Writer DEFAULT_WRITER = new ConsoleWriter();

    private final Reader reader;
    private final Writer writer;

    public InputView(Reader reader, Writer writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public InputView() {
        this(DEFAULT_READER, DEFAULT_WRITER);
    }

    public Map<String, Integer> purchaseProduct() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String userInput = Console.readLine();
        return validateAndParsePurchaseRequest(userInput);
    }


    // 사용자 입력 확인 메서드
    public static boolean confirmUserInput() {
        while (true) {
            String response = Console.readLine().trim().toUpperCase();
            if ("Y".equals(response)) {
                return true;
            }
            if ("N".equals(response)) {
                return false;
            }
            System.out.println("[ERROR] 잘못된 입력입니다. Y 또는 N으로 응답해 주세요.");
        }
    }

    private Map<String, Integer> validateAndParsePurchaseRequest(String input) {
        while (true) {
            try {
                return parsePurchaseRequest(input);  // 유효한 입력이면 결과를 반환
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] 잘못된 입력 형식입니다. 올바른 형식으로 다시 입력해 주세요.");
                input = Console.readLine();  // 재입력 요청
            }
        }
    }

    private Map<String, Integer> parsePurchaseRequest(String purchaseRequest) {
        try {
            return Arrays.stream(purchaseRequest.replaceAll("[\\[\\]]", "").split(","))
                    .map(item -> {
                        String[] parts = item.split("-");
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("잘못된 입력 형식입니다: " + item);
                        }
                        return parts;
                    })
                    .collect(Collectors.toMap(
                            parts -> parts[0],
                            parts -> Integer.parseInt(parts[1])
                    ));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("수량은 숫자여야 합니다.", e);
        }
    }

    public int readQuantity() {
        int quantity;
        do {
            quantity = validateInt(Console.readLine());
        } while (quantity == -1); // 유효한 숫자가 입력될 때까지 반복
        return quantity;
    }

    public int validateInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] 숫자가 아닌 값이 입력되었습니다. 다시 입력해 주세요.");
            return -1; // 잘못된 값을 의미하는 숫자를 반환
        }
    }



}
