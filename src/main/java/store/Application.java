package store;

import store.io.IOHandler;
import store.io.input.InputHandler;
import store.io.input.provider.PromotionProvider;
import store.io.input.provider.StockProvider;
import store.io.input.validation.UserInputValidation;
import store.io.output.OutputHandler;

public class Application {
	public static void main(String[] args) {
		ConvenienceStore convenienceStore = new ConvenienceStore(
			new IOHandler(new InputHandler(new UserInputValidation()), new OutputHandler()),
			new PromotionProvider(),
			new StockProvider()
		);
		convenienceStore.run();
	}

}
