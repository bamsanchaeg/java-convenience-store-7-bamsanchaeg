package store.view;

import store.view.output.ConsoleWriter;
import store.view.output.Writer;

public class OutputView {

    private static final Writer DEFAULT_WRITER = new ConsoleWriter();

    private final Writer writer;

    public OutputView() {
        this(DEFAULT_WRITER);
    }
    
    public OutputView(Writer writer) {
        this.writer = writer;
    }


}
