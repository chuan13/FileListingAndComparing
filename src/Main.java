import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		String mode = "compare";  // "list" 或 "compare"
		String dir1PathString = "（比對目標資料夾 1 的絕對路徑）";
		String dir2PathString = "（比對目標資料夾 2 的絕對路徑）";
		
		Util util = new Util();
		
		if ("compare".equals(mode)) {
			Comparing comparing = new Comparing(util);
			try {
				comparing.compare(dir1PathString, dir2PathString);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IsNotDirectoryException e) {
				System.out.println("錯誤：這不是資料夾。");
				e.printStackTrace();
			}
		} else if ("list".equals(mode)) {
			Listing listing = new Listing(util);
			// TODO
		} else {
			System.out.println("錯誤：未正確選擇模式。");
		}
	}

}
