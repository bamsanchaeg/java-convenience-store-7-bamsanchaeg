package store.view;

import store.view.input.ConsoleReader;
import store.view.input.Reader;

public class InputView {

    private static final Reader DEFAULT_READER = new ConsoleReader();

    private final Reader reader;

    public InputView(Reader reader) {
        this.reader = reader;
    }




}
