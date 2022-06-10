import java.util.concurrent.TimeUnit;

class HelloWorld {
    public static void main(String[] args) {
        try {
			for (int i = 0; i < 100; i++) {
				Thread.sleep(TimeUnit.SECONDS.toMillis(1));
			}
		} catch (Exception ex) {
		}
    }
}