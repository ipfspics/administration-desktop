package ipfs.pics.administration;

public class Main {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Administration window = new Administration();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
